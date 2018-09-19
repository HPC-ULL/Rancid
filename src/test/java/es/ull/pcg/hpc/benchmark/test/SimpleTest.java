package es.ull.pcg.hpc.benchmark.test;

import es.ull.pcg.hpc.benchmark.BenchmarkManager;
import es.ull.pcg.hpc.benchmark.Parameters;
import es.ull.pcg.hpc.benchmark.analyzers.*;
import es.ull.pcg.hpc.benchmark.benchmarks.MultipleBenchmark;
import es.ull.pcg.hpc.benchmark.benchmarks.SimpleBenchmark;
import es.ull.pcg.hpc.benchmark.loggers.HumanReadableResultsLogger;
import es.ull.pcg.hpc.benchmark.loggers.JsonResultsLogger;
import es.ull.pcg.hpc.benchmark.meters.ExecutionTimeMeter;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.printers.PrintWriterOutputPrinter;
import es.ull.pcg.hpc.benchmark.stopconditions.AndStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.ErrorStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.FixedIterationsStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.OrStopCondition;
import org.junit.Test;

import java.io.PrintWriter;

public class SimpleTest {

    private static abstract class MyBenchmark extends SimpleBenchmark {
        protected int w, h;
        protected double n;

        public MyBenchmark (String name) {
            super(name);
        }

        @Override
        public void preBenchmark (Parameters parameters) {
            super.preBenchmark(parameters);
            this.w = parameters.getParameter("Width");
            this.h = parameters.getParameter("Height");
        }

        @Override
        public void preRun () {
            super.preRun();
            this.n = 0.0;
        }

        @Override
        public boolean handleException (Exception exception) {
            return true;
        }
    }

    private SimpleBenchmark fwdBenchmark = new MyBenchmark("Forward Implementation") {
        @Override
        protected void benchmark () {
            if (Math.random() < 0.25)
                throw new RuntimeException("Oh no.");

            for (int i = 0; i < w; ++i)
                for (int j = 0; j < h; ++j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    };

    private SimpleBenchmark revBenchmark = new MyBenchmark("Reverse Implementation") {
        @Override
        protected void benchmark () {
            for (int i = w - 1; i >= 0; --i)
                for (int j = h - 1; j >= 0; --j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    };

    @Test
    public void test () {
        for (int w = 100; w <= 1000; w *= 10) {
            for (int h = 100; h <= 1000; h *= 10) {
                Parameters params = new Parameters(String.valueOf(w) + "x" + String.valueOf(h));
                params.addParameter("Width", w);
                params.addParameter("Height", h);
                fwdBenchmark.addParameters(params);
                revBenchmark.addParameters(params);
            }
        }

        final int warmup = 10;

        AndStopCondition andCondition = new AndStopCondition(
            new FixedIterationsStopCondition(20 + warmup),
            new ErrorStopCondition(ExecutionTimeMeter.NAME, warmup, 0.1));

        OrStopCondition stopCondition = new OrStopCondition(andCondition, new FixedIterationsStopCondition(100));

        MultipleBenchmark myBenchmark = new MultipleBenchmark("Loop Benchmark", stopCondition);
        myBenchmark.addImplementations(fwdBenchmark);
        myBenchmark.addImplementations(revBenchmark);

        BenchmarkManager mgr = new BenchmarkManager();
        mgr.addBenchmark(myBenchmark);

        mgr.addMeter(new SuccessfulRunsMeter());
        mgr.addMeter(new ExecutionTimeMeter());

        mgr.addRunAnalyzer(new WarmupIterationsFilter(warmup));
        mgr.addRunAnalyzer(new SumAnalyzer(SuccessfulRunsMeter.NAME));
        mgr.addRunAnalyzer(new AverageAnalyzer(SuccessfulRunsMeter.NAME));
        mgr.addRunAnalyzer(new InvalidRunsFilter());
        mgr.addRunAnalyzer(new AverageAnalyzer(ExecutionTimeMeter.NAME));
        mgr.addRunAnalyzer(new StdDeviationAnalyzer(ExecutionTimeMeter.NAME));

        PrintWriter printer = new PrintWriter(System.out, true);
        mgr.addOnlineLogger(new HumanReadableResultsLogger(new PrintWriterOutputPrinter(printer)));
        mgr.addGlobalLogger(new JsonResultsLogger(new PrintWriterOutputPrinter(printer)));

        //mgr.addProgressListener(new RelativeProgressListener() {
        //    @Override
        //    public void updateProgress (double globalProgress, double benchmarksProgress, double parametersProgress) {
        //        System.out.println("('" + mBenchmarkName + "' '" + mParametersName + "') " +
        //                           String.valueOf(100 * globalProgress) + "% completed");
        //    }
        //});
        mgr.runBenchmarks();
    }
}
