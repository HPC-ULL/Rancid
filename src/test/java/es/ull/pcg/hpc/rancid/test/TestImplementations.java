package es.ull.pcg.hpc.rancid.test;

import es.ull.pcg.hpc.rancid.Parameters;
import es.ull.pcg.hpc.rancid.benchmark.BenchmarkImplementation;

public class TestImplementations {
    private static abstract class BaseImplementation extends BenchmarkImplementation {
        protected int w, h;
        protected double n;

        public BaseImplementation (String title) {
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

    public static class ForwardImplementation extends BaseImplementation {
        public ForwardImplementation () {
            super("Forward Implementation");
        }

        @Override
        public void instrumentedRun () {
            if (Math.random() < 0.25)
                throw new RuntimeException("Oh no.");

            for (int i = 0; i < w; ++i)
                for (int j = 0; j < h; ++j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    }

    public static class ReverseImplementation extends BaseImplementation {
        public ReverseImplementation () {
            super("Reverse Implementation");
        }

        @Override
        public void instrumentedRun () {
            for (int i = w - 1; i >= 0; --i)
                for (int j = h - 1; j >= 0; --j)
                    n += ((i + 1) * (j + 1)) / (double) (w * h);
        }
    }
}
