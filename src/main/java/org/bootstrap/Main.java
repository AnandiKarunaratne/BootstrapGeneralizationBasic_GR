package org.bootstrap;

import com.opencsv.exceptions.CsvBadConverterException;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.utils.CsvUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main {
    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

        int[] logs = {0, 1, 2};
        int[] logSizes = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        double[] noiseLevels = {0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2};
        int[] sampleSizeCos = {1, 2, 4, 8, 16};
        int[] G = {25, 50, 100, 200};
        String[] noiseTypes = {"oa", "ia", "ooo", "all"};

        for (int log : logs) {
            for (int logSize : logSizes) {
                for (int sampleSizeCo : sampleSizeCos) {
                    int sampleSize = sampleSizeCo * logSize;
                    String logFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV5/Logs/" + log + "-" + logSize + ".xes";
                    String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV5/Models/" + log + "-" + logSize + ".pnml";
                    EventLog eventLog = new EventLog(logFilePath);
                    for (int g : G) {
                        long startTime = System.currentTimeMillis();
                        double[] result = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath, g).calculateGenUsingVariableSamples();
                        long endTime = System.currentTimeMillis();
                        CsvUtils.writeToCsv(CsvUtils.prepareCsvRow(log, logSize, "N/A", 0, sampleSizeCo, g, result, endTime - startTime), "./output.csv");
                    }
                }
            }
        }


        for (int log : logs) {
            for (int logSize : logSizes) {
                for (String noiseType : noiseTypes) {
                    for (double noiseLevel : noiseLevels) {
                        for (int sampleSizeCo : sampleSizeCos) {
                            int sampleSize = sampleSizeCo * logSize;
                            String logFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV5/Logs/" + log + "-" + logSize + "-" + noiseType + "-" + noiseLevel + ".xes";
                            String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV5/Models/" + log + "-" + logSize + "-" + noiseType + "-" + noiseLevel + ".pnml";
                            EventLog eventLog = new EventLog(logFilePath);
                            for (int g : G) {
                                long startTime = System.currentTimeMillis();
                                double[] result = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath, g).calculateGenUsingVariableSamples();
                                long endTime = System.currentTimeMillis();
                                CsvUtils.writeToCsv(CsvUtils.prepareCsvRow(log, logSize, noiseType, noiseLevel, sampleSizeCo, g, result, endTime - startTime), "./output.csv");
                            }
                        }
                    }
                }
            }
        }
    }

}