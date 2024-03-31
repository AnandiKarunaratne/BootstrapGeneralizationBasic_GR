package org.bootstrap.lsm;

public class SemiParametricLSM extends LogSamplingMethod {

    private final int g;
    private final int k;
    private final double p;

    public SemiParametricLSM(LogSamplingMethodEnum logSamplingMethodEnum, int g, int k, double p) {
        super(logSamplingMethodEnum);
        this.g = g;
        this.k = k;
        this.p = p;
    }

    public int getG() {
        return g;
    }

    public int getK() {
        return k;
    }

    public double getP() {
        return p;
    }

}
