package org.bootstrap;

import org.apache.commons.math3.util.Pair;
import org.bootstrap.gen.Generalization;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogBreeding;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.utils.LogUtils;

import java.io.*;
import java.util.*;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<String> log;
    private final LogSamplingMethod logSamplingMethod;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, LogSamplingMethod logSamplingMethod) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     */
    public void calculateBootstrapGen() throws Exception {
        Pair<Double, Double> genValueOfSample;
        double genValuesRecallSum = 0;
        double genValuesPrecisionSum = 0;
        Generalization generalization = new Generalization();
        List<String> estimatedPopulation = estimatePopulation();

        int uniqueTraceCount = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            List<String> sample = generateSample(estimatedPopulation);

            Set<String> distinctTraces = new HashSet<>();
            for (String trace : sample) {
                distinctTraces.add(trace);
            }
            uniqueTraceCount += distinctTraces.size();

            genValueOfSample = generalization.calculateEntropyRecallPrecision();
            genValuesRecallSum += genValueOfSample.getFirst();
            genValuesPrecisionSum += genValueOfSample.getSecond();
        }
        System.out.println("Unique Traces: " + (double) uniqueTraceCount/numberOfSamples);
        System.out.println("Recall: " + genValuesRecallSum/numberOfSamples);
        System.out.println("Precision: " + genValuesPrecisionSum/numberOfSamples);
    }

    /**
     * This method will estimate the population based on the log sampling method
     *
     * @return estimated trace list
     */
    private List<String> estimatePopulation() {
        if (logSamplingMethod == LogSamplingMethod.NON_PARAMETRIC) {
            return log;
        } else if (logSamplingMethod == LogSamplingMethod.SEMI_PARAMETRIC) {
            return new LogBreeding().estimatePopulationWithLogBreeding(log, 10000, 2, 1.0);
        } else return null;
    }

    /**
     * This method will generate a sample of the given sample size using the trace list
     *
     * @return sample of traces
     */
    private List<String> generateSample(List<String> estimatedPopulation) {
        List<String> sample = new ArrayList<>();
        Random random = new Random();
        int maxLimit = estimatedPopulation.size();

        while (sample.size() < sampleSize) {
            int randomIndex = random.nextInt(maxLimit);
            String trace = estimatedPopulation.get(randomIndex);
            sample.add(trace);
        }
        LogUtils.generateXES(sample);
        return sample;
    }

}


