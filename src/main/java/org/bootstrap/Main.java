package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

public class Main {
//    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

        int[] logSizes = {1000, 5000, 10000};

        for (int logSize : logSizes) {
            int sampleSize = 5 * logSize;
            for (int noise = 0; noise < 21; noise = noise + 5) {
                String logFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/Log0/OmitActivity/Logs/log-" + logSize + "-" + noise + ".xes";
                String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/Log0/OmitActivity/Models/model-" + logSize + "-" + noise + ".pnml";
//                String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/model.pnml";

//                String logFilePath = "/Users/anandik/Documents/LogQualitySamples/log-" + logSize + "/log-" + logSize + "-" + String.format("%02d", noise) + ".xes";
//                String modelFilePath = "/Users/anandik/Documents/LogQualityModels/log-" + logSize + "/model-" + logSize + "-" + String.format("%02d", noise) + ".pnml";
                EventLog eventLog = new EventLog(logFilePath);

                for (int i = 0; i < 3; i++) {
                    new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath, noise).calculateBootstrapGen();
                }
            }
        }
    }

}