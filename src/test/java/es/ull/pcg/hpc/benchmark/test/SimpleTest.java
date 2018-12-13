package es.ull.pcg.hpc.benchmark.test;

import es.ull.pcg.hpc.benchmark.BenchmarkManager;
import es.ull.pcg.hpc.benchmark.Parameters;
import es.ull.pcg.hpc.benchmark.removers.ResultsRemover;
import es.ull.pcg.hpc.benchmark.StopCondition;
import es.ull.pcg.hpc.benchmark.analyzers.*;
import es.ull.pcg.hpc.benchmark.benchmarks.MultipleBenchmark;
import es.ull.pcg.hpc.benchmark.benchmarks.SimpleBenchmark;
import es.ull.pcg.hpc.benchmark.removers.InvalidRunsRemover;
import es.ull.pcg.hpc.benchmark.filters.WindowIterationsFilter;
import es.ull.pcg.hpc.benchmark.loggers.HumanReadableResultsLogger;
import es.ull.pcg.hpc.benchmark.loggers.JsonResultsLogger;
import es.ull.pcg.hpc.benchmark.meters.ExecutionTimeMeter;
import es.ull.pcg.hpc.benchmark.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.benchmark.printers.PrintWriterOutputPrinter;
import es.ull.pcg.hpc.benchmark.results.ResultTypes;
import es.ull.pcg.hpc.benchmark.stopconditions.ElapsedTimeStopCondition;
import es.ull.pcg.hpc.benchmark.stopconditions.ErrorWindowStopCondition;
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
        // Parameters
        for (int w = 100; w <= 1000; w *= 10) {
            for (int h = 100; h <= 1000; h *= 10) {
                Parameters params = new Parameters(w + "x" + h);
                params.addParameter("Width", w);
                params.addParameter("Height", h);
                fwdBenchmark.addParameters(params);
                revBenchmark.addParameters(params);
            }
        }

        final int window = 20;

        // Stop condition
        StopCondition stopCondition = new OrStopCondition(
                new ErrorWindowStopCondition(ExecutionTimeMeter.NAME, window, 0.0001),
                new ElapsedTimeStopCondition(100));

        // Benchmarks
        MultipleBenchmark myBenchmark = new MultipleBenchmark("Loop Benchmark", stopCondition);
        myBenchmark.addImplementations(fwdBenchmark);
        myBenchmark.addImplementations(revBenchmark);

        // Manager
        BenchmarkManager mgr = new BenchmarkManager();
        mgr.addBenchmark(myBenchmark);

        // Meters
        mgr.addMeter(new SuccessfulRunsMeter());
        mgr.addMeter(new ExecutionTimeMeter());

        // Processors
        // - Add total number and proportion of successful runs analysis
        mgr.addRunProcessor(new SumAnalyzer(SuccessfulRunsMeter.NAME));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(SuccessfulRunsMeter.NAME));

        // - Remove invalid runs data, and the successful runs metric raw data
        mgr.addRunProcessor(new InvalidRunsRemover());
        mgr.addRunProcessor(new ResultsRemover(ResultTypes.Metric, SuccessfulRunsMeter.NAME));

        // - Calculate histogram, min, max, average and stddev of all successful runs
        mgr.addRunProcessor(new HistogramAnalyzer(ExecutionTimeMeter.NAME, 10));
        mgr.addRunProcessor(new MinAnalyzer(ExecutionTimeMeter.NAME));
        mgr.addRunProcessor(new MaxAnalyzer(ExecutionTimeMeter.NAME));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(ExecutionTimeMeter.NAME));
        mgr.addRunProcessor(new StdDeviationAnalyzer(ExecutionTimeMeter.NAME));

        // - Filter run data within the window used for the stop condition, and remove the rest of results
        mgr.addRunProcessor(new WindowIterationsFilter(window));
        mgr.addRunProcessor(new ResultsRemover(ResultTypes.Metric, ExecutionTimeMeter.NAME));

        // - Calculate histogram, min, max, average and stddev of runs inside the window
        mgr.addRunProcessor(new HistogramAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.NAME)));
        mgr.addRunProcessor(new MinAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.NAME)));
        mgr.addRunProcessor(new MaxAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.NAME)));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.NAME)));
        mgr.addRunProcessor(new StdDeviationAnalyzer(WindowIterationsFilter.processedMetricTitle(ExecutionTimeMeter.NAME)));

        // Loggers
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
