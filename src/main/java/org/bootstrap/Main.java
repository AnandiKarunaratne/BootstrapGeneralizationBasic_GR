package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

public class Main {
    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

        for (int logSize = 100; logSize <= 1000000; logSize = logSize * 10) {
            for (int noise = 0; noise < 11; noise++) {
                String logFilePath = "/Users/anandik/Documents/LogQualitySamples/log-" + logSize + "/log-" + logSize + "-" + String.format("%02d", noise) + ".xes";
                String modelFilePath = "/Users/anandik/Documents/LogQualityModels/log-" + logSize + "/model-" + logSize + "-" + String.format("%02d", noise) + ".pnml";
                EventLog eventLog = new EventLog(logFilePath);

                for (int i = 0; i < 10; i++) {
                    new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath).calculateBootstrapGen();
                }
            }
        }
    }

}