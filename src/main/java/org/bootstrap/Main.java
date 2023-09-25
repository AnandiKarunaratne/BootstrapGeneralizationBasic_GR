package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

public class Main {
    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {
        EventLog eventLog = new EventLog();

        new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod).calculateBootstrapGen();

    }

}