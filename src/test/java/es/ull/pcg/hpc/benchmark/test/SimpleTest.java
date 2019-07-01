package es.ull.pcg.hpc.benchmark.test;

import es.ull.pcg.hpc.benchmark.OutputPrinter;
import es.ull.pcg.hpc.benchmark.Parameters;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.analyzers.*;
import es.ull.pcg.hpc.benchmark.benchmark.*;
import es.ull.pcg.hpc.benchmark.loggers.HumanReadableResultsLogger;
import es.ull.pcg.hpc.benchmark.loggers.JsonResultsLogger;
import es.ull.pcg.hpc.benchmark.meters.ExecutionTimeMeter;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.printers.PrintStreamOutputPrinter;
import es.ull.pcg.hpc.benchmark.stopconditions.AndStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.ErrorStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.FixedIterationsStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.OrStopCondition;
import es.ull.pcg.hpc.benchmark.benchmark.ModularBenchmarkRunner;
import es.ull.pcg.hpc.benchmark.benchmark.actions.CompositeAction;
import es.ull.pcg.hpc.benchmark.benchmark.actions.ConditionalAction;
import es.ull.pcg.hpc.benchmark.benchmark.actions.PrintAction;
import es.ull.pcg.hpc.benchmark.benchmark.actions.SleepAction;
import java9.util.function.Predicate;
import org.junit.Test;

public class SimpleTest {
    private static abstract class MyBenchmark extends BenchmarkImplementation {
        protected int w, h;
        protected double n;

        public MyBenchmark (String title) {
            super(title);
        }

        @Override
        public void setupBenchmark (Parameters parameters) {
            super.setupBenchmark(parameters);
            this.w = parameters.getParameter("Width");
            this.h = parameters.getParameter("Height");
        }

        @Override
        public void initParameters () {
            super.initParameters();
            this.n = 0.0;
        }

        @Override
        public boolean handleException (RuntimeException e) {
            return true;
        }
    }

    private BenchmarkImplementation fwdImplementation = new MyBenchmark("Forward Implementation") {
        @Override
        public void instrumentedRun () {
            if (Math.random() < 0.25)
                throw new RuntimeException("Oh no.");

            for (int i = 0; i < w; ++i)
                for (int j = 0; j < h; ++j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    };

    private BenchmarkImplementation revImplementation = new MyBenchmark("Reverse Implementation") {
        @Override
        public void instrumentedRun () {
            for (int i = w - 1; i >= 0; --i)
                for (int j = h - 1; j >= 0; --j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    };

    @Test
    public void test () {
        OutputPrinter printer = new PrintStreamOutputPrinter(System.out);
        final int warmup = 10;

        // Create stop condition
        AndStopCondition andCondition = new AndStopCondition(
            new FixedIterationsStopCondition(20 + warmup),
            new ErrorStopCondition(ExecutionTimeMeter.TITLE, warmup, 0.05));

        StopCondition stopCondition = new OrStopCondition(andCondition, new FixedIterationsStopCondition(1000));

        // Configure runner
        ModularBenchmarkRunner runner = new ModularBenchmarkRunner(stopCondition);

        CompositeAction ifActions = new CompositeAction();
        ifActions.addAction(new PrintAction<Void>(printer, "This is executed before sleep"));
        ifActions.addAction(new SleepAction(1000));
        ifActions.addAction(new PrintAction<Void>(printer, "This is executed afer sleep"));

        runner.addAction(BenchmarkStage.PRE_BENCHMARK,
                         new ConditionalAction<>(ifActions, runner, new Predicate<BenchmarkRunner>() {
                             @Override
                             public boolean test (BenchmarkRunner r) {
                                 return (Integer) r.getCurrentParameters().getParameter("Width") < 500;
                             }
                         }));

        // Create benchmark configurations and benchmark
        BenchmarkConfiguration fwdBenchmark = new BenchmarkConfiguration(runner, fwdImplementation);
        BenchmarkConfiguration revBenchmark = new BenchmarkConfiguration(runner, revImplementation);

        Benchmark loopBenchmark = new Benchmark("Loop Benchmark");
        loopBenchmark.addConfiguration(fwdBenchmark);
        loopBenchmark.addConfiguration(revBenchmark);

        // Set parameters
        for (int w = 100; w <= 1000; w *= 10) {
            for (int h = 100; h <= 1000; h *= 10) {
                Parameters params = new Parameters(w + "x" + h);
                params.addParameter("Width", w);
                params.addParameter("Height", h);
                loopBenchmark.addParameterSet(params);
            }
        }

        // Create benchmark manager
        BenchmarkManager mgr = new BenchmarkManager();
        mgr.addBenchmark(loopBenchmark);

        // Configure benchmark manager
        mgr.addMeter(new SuccessfulRunsMeter());
        mgr.addMeter(new ExecutionTimeMeter());

        mgr.addRunAnalyzer(new WarmupIterationsFilter(warmup));
        mgr.addRunAnalyzer(new SumAnalyzer(SuccessfulRunsMeter.TITLE));
        mgr.addRunAnalyzer(new AverageAnalyzer(SuccessfulRunsMeter.TITLE));
        mgr.addRunAnalyzer(new InvalidRunsFilter());
        mgr.addRunAnalyzer(new AverageAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunAnalyzer(new StdDeviationAnalyzer(ExecutionTimeMeter.TITLE));

        mgr.addOnlineLogger(new HumanReadableResultsLogger(printer));
        mgr.addGlobalLogger(new JsonResultsLogger(printer));

        //mgr.addProgressListener(new RelativeProgressListener() {
        //    @Override
        //    public void updateProgress (double globalProgress, double benchmarksProgress, double parametersProgress) {
        //        System.out.println("('" + mBenchmarkTitle + "' '" + mParametersTitle + "') " +
        //                           (100 * globalProgress) + "% completed");
        //    }
        //});
        mgr.runBenchmarks();
    }
}
