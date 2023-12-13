package org.bootstrap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    final private static int numberOfSamples = 100; // m
    final private static int g = 100;
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

        int[] logSizes = {64, 128, 256, 512, 1024};
        String[] noiseTypes = {"oa", "ia", "ooo"};
        double[] noiseLevels = {0, 0.25, 0.5, 0.75, 1, 5, 10, 15, 20};

        for (int log = 0; log < 3; log++) {
            for (String noiseType : noiseTypes) {
                for (int logSize : logSizes) {
                    int sampleSize = 5 * logSize;
                    for (double noise : noiseLevels) {
                        String logFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV4/Log" + log + "/" + getFolder(noiseType) + "/Logs/log-" + logSize + "-" + noise + ".xes";
                        String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV4/Log" + log + "/" + getFolder(noiseType) + "/Models/model-" + logSize + "-" + noise + ".pnml";
                        EventLog eventLog = new EventLog(logFilePath);
                        double[] result = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath, g).calc();
                        writeToCSV(String.valueOf(log), logSize, noiseType, noise, result);
                    }
                }
            }
        }
    }

    private static void writeToCSV(String log, int logSize, String noiseType, double noiseLevel, double[] result) {
        String filePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/bResultsStdDevTest.csv";

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            List<String> values = new ArrayList<>();
            values.add(log);
            values.add(Integer.toString(logSize));
            values.add(noiseType);
            values.add(Double.toString(noiseLevel));
            for (double res : result) values.add(Double.toString(res));
            csvPrinter.printRecord(values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFolder(String word) {
        if (word == "oa") return "OmitActivity";
        else if (word == "ia") return "InjectActivity";
        else if (word == "ooo") return "OutOfOrder";
        else return null;
    }

}