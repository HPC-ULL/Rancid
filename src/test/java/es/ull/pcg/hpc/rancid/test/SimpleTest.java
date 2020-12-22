package es.ull.pcg.hpc.rancid.test;

import es.ull.pcg.hpc.rancid.OutputPrinter;
import es.ull.pcg.hpc.rancid.Parameters;
import es.ull.pcg.hpc.rancid.StopCondition;
import es.ull.pcg.hpc.rancid.analyzers.*;
import es.ull.pcg.hpc.rancid.benchmark.Benchmark;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkConfiguration;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkManager;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkRunner;
import es.ull.pcg.hpc.rancid.loggers.HumanReadableResultsLogger;
import es.ull.pcg.hpc.rancid.meters.ExecutionTimeMeter;
import es.ull.pcg.hpc.rancid.meters.SuccessfulRunsMeter;
import es.ull.pcg.hpc.rancid.printers.PrintStreamOutputPrinter;
import es.ull.pcg.hpc.rancid.removers.InvalidRunsRemover;
import es.ull.pcg.hpc.rancid.removers.ResultsRemover;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.stopconditions.FixedIterationsStopCondition;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void test() {
        OutputPrinter stdoutPrinter = new PrintStreamOutputPrinter(System.out);

        StopCondition stopCondition = new FixedIterationsStopCondition(10);
        BenchmarkRunner runner = new BenchmarkRunner(stopCondition);
        BenchmarkConfiguration config = new BenchmarkConfiguration(runner,
                new TestImplementations.ForwardImplementation());

        Benchmark benchmark = new Benchmark("Simple Loop Benchmark");
        benchmark.addConfiguration(config);

        for (int w = 100; w <= 1000; w *= 10) {
            for (int h = 100; h <= 1000; h *= 10) {
                Parameters params = new Parameters(w + "x" + h);
                params.addParameter("Width", w);
                params.addParameter("Height", h);
                benchmark.addParameterSet(params);
            }
        }

        BenchmarkManager mgr = new BenchmarkManager();
        mgr.addBenchmark(benchmark);

        mgr.addMeter(new SuccessfulRunsMeter());
        mgr.addMeter(new ExecutionTimeMeter());

        mgr.addRunProcessor(new InvalidRunsRemover());
        mgr.addRunProcessor(new ResultsRemover(ResultTypes.Metric, SuccessfulRunsMeter.TITLE));

        mgr.addRunProcessor(new HistogramAnalyzer(ExecutionTimeMeter.TITLE, 10));
        mgr.addRunProcessor(new MinAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new MaxAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new ArithmeticAverageAnalyzer(ExecutionTimeMeter.TITLE));
        mgr.addRunProcessor(new StdDeviationAnalyzer(ExecutionTimeMeter.TITLE));

        mgr.addOnlineLogger(new HumanReadableResultsLogger(stdoutPrinter));
        mgr.runBenchmarks();
    }
}
