package org.bootstrap.lsm;

public class LogSamplingMethod {

    private final LogSamplingMethodEnum logSamplingMethodEnum;

    LogSamplingMethod(LogSamplingMethodEnum logSamplingMethodEnum) {
        this.logSamplingMethodEnum = logSamplingMethodEnum;
    }

    public LogSamplingMethodEnum getLogSamplingMethodEnum() {
        return logSamplingMethodEnum;
    }
}
