package org.bootstrap;

import org.bootstrap.bootstrap.BootstrapTerminationCriterion;
import org.bootstrap.bootstrap.BootstrapTerminationCriterionEnum;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.lsm.LogSamplingMethodEnum;
import org.bootstrap.lsm.SemiParametricLSM;
import org.bootstrap.utils.CsvUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
//    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = new SemiParametricLSM(LogSamplingMethodEnum.SEMI_PARAMETRIC, 512, 2, 1.0);
    static BootstrapTerminationCriterion bootstrapTerminationCriterion = new BootstrapTerminationCriterion(BootstrapTerminationCriterionEnum.CONFIDENCE_INTERVAL, 0.01);


    public static void main(String[] args) throws Exception {

        for (int k = 0; k <= 2; k++) {
            for (int logSize = 64; logSize <= 1024; logSize *= 2) {
//                for (int sampleSize = 8; sampleSize <= 4096; sampleSize *= 2) {
                    String type = "infinite";
                int sampleSize = 4096;
                    String logWriteFilePath = "/Users/anandik/Library/CloudStorage/OneDrive-TheUniversityofMelbourne/GR/RQ1/Experiments/BootstrapGenEfficacy/ModelBootstrap/Data/" + type + "-" + logSize + ".xes";
                    String modelLangFilePath = "/Users/anandik/Library/CloudStorage/OneDrive-TheUniversityofMelbourne/GR/RQ1/Experiments/BootstrapGenEfficacy/ModelBootstrap/Data/" + type + "-model" + logSize + ".xes";
                    String modelFilePath = "/Users/anandik/Library/CloudStorage/OneDrive-TheUniversityofMelbourne/GR/RQ1/Experiments/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/" + type + "-model.pnml";

                    long startTime = System.currentTimeMillis();
                    double[] result = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, new EventLog(logWriteFilePath), logSamplingMethod, false, modelFilePath).calculateGen();
                    long endTime = System.currentTimeMillis();
                    CsvUtils.writeToCsv(prepareCsvRow(type, false, logSize, "N/A", 0.0, sampleSize, 512, result, endTime - startTime), "./output-model-bootstrap-test1.csv");

                    startTime = System.currentTimeMillis();
                    result = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, new EventLog(logWriteFilePath), logSamplingMethod, true, modelLangFilePath).calculateGen();
                    endTime = System.currentTimeMillis();
                    CsvUtils.writeToCsv(prepareCsvRow(type, true, logSize, "N/A", 0.0, sampleSize, 512, result, endTime - startTime), "./output-model-bootstrap-test1.csv");
//                }
            }
        }

    }

    private static List<String> prepareCsvRow(String log, boolean bootstrapped, int logSize, String noiseType, double noiseLevel, int sampleSize, int g, double[] result, double duration) {
        List<String> csvRow = new ArrayList<>();
        csvRow.add(log);
        csvRow.add(String.valueOf(bootstrapped));
        csvRow.add(String.valueOf(logSize));
        csvRow.add(noiseType);
        csvRow.add(String.valueOf(noiseLevel));
        csvRow.add(String.valueOf(sampleSize));
        csvRow.add(String.valueOf(g));
        for (double value : result) csvRow.add(String.valueOf(value));
        csvRow.add(String.valueOf(duration));
        return csvRow;
    }

}