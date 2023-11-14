package org.bootstrap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

        int[] logSizes = {100, 500, 1000, 5000, 10000};
        String[] noiseTypes = {"oa", "ia", "ooo"};

        for (int log = 0; log < 10; log++) {
            for (String noiseType : noiseTypes) {
                for (int logSize : logSizes) {
                    int sampleSize = 5 * logSize;
                    for (int noise = 0; noise < 21; noise = noise + 5) {
                        String logFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/Log" + log + "/" + getFolder(noiseType) + "/Logs/log-" + logSize + "-" + noise + ".xes";
                        String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/Log" + log + "/" + getFolder(noiseType) + "/Models/model-" + logSize + "-" + noise + ".pnml";
                        EventLog eventLog = new EventLog(logFilePath);
                        Double[] result = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath).calculateBootstrapGen();
                        writeToCSV(log, logSize, noiseType, noise, result[0], result[1], result[2], result[3], result[4]);
                    }
                }
            }
        }
    }

    private static void writeToCSV(int log, int logSize, String noiseType, int noiseLevel, double precision, double recall, double staticPrec, double staticRec, double traces) {
        String filePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/output_constg.csv";

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            String[] values = {Integer.toString(log), Integer.toString(logSize), noiseType, Integer.toString(noiseLevel), Double.toString(precision), Double.toString(recall), Double.toString(staticPrec), Double.toString(staticRec), Double.toString(traces)};
            csvPrinter.printRecord(Arrays.asList(values));
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