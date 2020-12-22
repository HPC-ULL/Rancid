package es.ull.pcg.hpc.rancid.test;

import es.ull.pcg.hpc.rancid.OutputPrinter;
import es.ull.pcg.hpc.rancid.Parameters;
import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.analyzers.*;
import es.ull.pcg.hpc.rancid.benchmark.*;
import es.ull.pcg.hpc.rancid.benchmark.actions.CompositeAction;
import es.ull.pcg.hpc.rancid.benchmark.actions.ConditionalAction;
import es.ull.pcg.hpc.rancid.benchmark.actions.PrintAction;
import es.ull.pcg.hpc.rancid.benchmark.actions.SleepAction;
import es.ull.pcg.hpc.rancid.filters.WindowIterationsFilter;
import es.ull.pcg.hpc.rancid.loggers.HumanReadableResultsLogger;
import es.ull.pcg.hpc.rancid.loggers.JsonResultsLogger;
import es.ull.pcg.hpc.rancid.meters.ExecutionTimeMeter;
import es.ull.pcg.hpc.rancid.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.rancid.printers.PrintStreamOutputPrinter;
import es.ull.pcg.hpc.rancid.removers.InvalidRunsRemover;
import es.ull.pcg.hpc.rancid.removers.ResultsRemover;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.stopconditions.AndStopCondition;
import es.ull.pcg.hpc.rancid.stopconditions.ErrorWindowStopCondition;
import es.ull.pcg.hpc.rancid.stopconditions.FixedIterationsStopCondition;
import es.ull.pcg.hpc.rancid.stopconditions.OrStopCondition;
import java9.util.function.Predicate;
import org.junit.Test;

public class CompleteTest {
    @Test
    public void test() {
        OutputPrinter printer = new PrintStreamOutputPrinter(System.out);
        final int warmup = 10;
        final int window = 20;

        // Create stop condition
        AndStopCondition andCondition = new AndStopCondition(
                new FixedIterationsStopCondition(20 + warmup),
                new ErrorWindowStopCondition(ExecutionTimeMeter.TITLE, warmup, 0.05));

        StopCondition stopCondition = new OrStopCondition(andCondition, new FixedIterationsStopCondition(1000));

        // Configure runner
        ModularBenchmarkRunner runner = new ModularBenchmarkRunner(stopCondition);

        CompositeAction ifActions = new CompositeAction();
        ifActions.addAction(new PrintAction<Void>(printer, "This is executed before sleep"));
        ifActions.addAction(new SleepAction(1000));
        ifActions.addAction(new PrintAction<Void>(printer, "This is executed after sleep"));

        runner.addAction(BenchmarkStage.PRE_BENCHMARK,
                new ConditionalAction<>(ifActions, runner, new Predicate<BenchmarkRunner>() {
                    @Override
                    public boolean test(BenchmarkRunner r) {
                        return (Integer) r.getCurrentParameters().getParameter("Width") < 500;
                    }
                }));

        // Create benchmark configurations and benchmark
        BenchmarkConfiguration fwdBenchmark = new BenchmarkConfiguration(runner, new TestImplementations.ForwardImplementation());
        BenchmarkConfiguration revBenchmark = new BenchmarkConfiguration(runner, new TestImplementations.ReverseImplementation());

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

        // Meters
        mgr.addMeter(new SuccessfulRunsMeter());
        mgr.addMeter(new ExecutionTimeMeter());

        // Processors
        // - Add total number and proportion of successful runs analysis
        mgr.addRunProcessor(new SumAnalyzer(SuccessfulRunsMeter.TITLE));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(SuccessfulRunsMeter.TITLE));

        // - Remove invalid runs data, and the successful runs metric raw data
        mgr.addRunProcessor(new InvalidRunsRemover());
        mgr.addRunProcessor(new ResultsRemover(ResultTypes.Metric, SuccessfulRunsMeter.TITLE));

        // - Calculate histogram, min, max, average and stddev of all successful runs
        mgr.addRunProcessor(new HistogramAnalyzer(ExecutionTimeMeter.TITLE, 10));
        mgr.addRunProcessor(new MinAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new MaxAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new StdDeviationAnalyzer(ExecutionTimeMeter.TITLE));

        // - Filter run data within the window used for the stop condition, and remove the rest of results
        mgr.addRunProcessor(new WindowIterationsFilter(window));
        mgr.addRunProcessor(new ResultsRemover(ResultTypes.Metric, ExecutionTimeMeter.TITLE));

        // - Calculate histogram, min, max, average and stddev of runs inside the window
        mgr.addRunProcessor(new HistogramAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.TITLE)));
        mgr.addRunProcessor(new MinAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.TITLE)));
        mgr.addRunProcessor(new MaxAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.TITLE)));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.TITLE)));
        mgr.addRunProcessor(new StdDeviationAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.TITLE)));

        // Loggers
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
