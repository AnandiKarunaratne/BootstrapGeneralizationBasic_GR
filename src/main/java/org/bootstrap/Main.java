package org.bootstrap;

import org.bootstrap.bootstrap.BootstrapTerminationCriterion;
import org.bootstrap.bootstrap.BootstrapTerminationCriterionEnum;
import org.bootstrap.log.EventLog;
import org.bootstrap.log.EventLogOps;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.lsm.LogSamplingMethodEnum;
import org.bootstrap.lsm.SemiParametricLSM;
import org.bootstrap.utils.CsvUtils;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;

import java.util.ArrayList;
import java.util.List;

public class Main {
//    final private static int numberOfSamples = 100; // m
    final private static LogSamplingMethod logSamplingMethod = new SemiParametricLSM(LogSamplingMethodEnum.SEMI_PARAMETRIC, 512, 2, 1.0);
    static BootstrapTerminationCriterion bootstrapTerminationCriterion = new BootstrapTerminationCriterion(BootstrapTerminationCriterionEnum.CONFIDENCE_INTERVAL, 0.01);


    public static void main(String[] args) throws Exception {

        String basePath = "/Users/anandik/Library/CloudStorage/OneDrive-TheUniversityofMelbourne/GR/RQ1/Experiments/BootstrapGenEfficacy/ModelBootstrap/";
        String[] inputSystemsPnml = CsvUtils.readCSV(basePath + "inputs.csv");
        int[] logSizes = {64, 128, 256, 512, 1024};

        for (String inputSystemPnml : inputSystemsPnml) {
            String inputSystem = inputSystemPnml.substring(0, inputSystemPnml.indexOf("pnml"));
            String systemFilePath = basePath + "systems/" + inputSystemPnml;
            String modelFilePath = basePath + "models/" + inputSystem + "128.0.pnml";
            String systemLogBaseFilePath = basePath + "system-logs/" + inputSystem;
            String modelLogBaseFilePath = basePath + "model-logs/" + inputSystem;

            createInputLogs(systemFilePath, logSizes, systemLogBaseFilePath);
            createInputLogs(modelFilePath, logSizes, modelLogBaseFilePath);

            for (int logSize : logSizes) {
                for (int sampleSize = 256; sampleSize <= 4096; sampleSize *= 2) {
                    EventLog systemLog = new EventLog(systemLogBaseFilePath + logSize + ".xes");
                    String modelLogFilePath = modelLogBaseFilePath + logSize + ".xes";

                    long startTime = System.currentTimeMillis();
                    double[] result = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, systemLog, logSamplingMethod, false, modelFilePath).calculateGen();
                    long endTime = System.currentTimeMillis();
                    CsvUtils.writeToCsv(prepareCsvRow(inputSystem, false, logSize, "N/A", 0.0, sampleSize, 512, result, endTime - startTime), "./output-model-bootstrap-60.csv");

                    startTime = System.currentTimeMillis();
                    result = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, systemLog, logSamplingMethod, true, modelLogFilePath).calculateGen();
                    endTime = System.currentTimeMillis();
                    CsvUtils.writeToCsv(prepareCsvRow(inputSystem, true, logSize, "N/A", 0.0, sampleSize, 512, result, endTime - startTime), "./output-model-bootstrap-60.csv");
                }
            }
        }

    }

    private static void createInputLogs(String netSystemFilePath, int[] logSizes, String logBaseFilePath) {
        EventLog prevLog = new EventLog();
        int prevLogSize = 0;
        for (int logSize : logSizes) {
            System.out.println(prevLog.size());
            int numOfTracesToGenerate = logSize - prevLogSize;
            EventLog generatedLog = createInputLog(netSystemFilePath, numOfTracesToGenerate);
            prevLog.addAll(generatedLog);
            prevLog.serializeEventLog(logBaseFilePath + logSize + ".xes");
            prevLogSize = logSize;
        }
    }

    private static EventLog createInputLog(String netSystemFilePath, int logSize) {
        PNMLSerializer pnmlSerializer = new PNMLSerializer();
        NetSystem netSystem = pnmlSerializer.parse(netSystemFilePath);
        return new EventLogOps().sampleRandomLog(netSystem, logSize);
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