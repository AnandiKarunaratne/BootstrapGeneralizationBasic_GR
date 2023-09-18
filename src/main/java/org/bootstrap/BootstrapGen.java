package org.bootstrap;

import org.bootstrap.gen.Generalization;
import org.bootstrap.gen.GeneralizationMeasure;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogBreeding;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.model.Model;

import java.util.*;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<String> log;
    private final LogSamplingMethod logSamplingMethod;
    private final Model model;
    private final GeneralizationMeasure generalizationMeasure;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, LogSamplingMethod logSamplingMethod, Model model, GeneralizationMeasure generalizationMeasure) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
        this.model = model;
        this.generalizationMeasure = generalizationMeasure;
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     *
     * @return bootstrap generalization value
     */
    public double calculateBootstrapGen() {
        double genValueOfSample = 0;
        double genValuesSum = 0;
        Generalization generalization = new Generalization();
        List<String> estimatedPopulation = estimatePopulation();

        for (int i = 0; i < numberOfSamples; i++) {
            List<String> sample = generateSample(estimatedPopulation);

            if (generalizationMeasure == GeneralizationMeasure.RECALL) {
                genValueOfSample = generalization.calculateRecall(model.getModel(), sample);
            } else if (generalizationMeasure == GeneralizationMeasure.ENTROPY_RECALL) {
                genValueOfSample = generalization.calculateEntropyRecall(model.getModel(), sample);
            } else if (generalizationMeasure == GeneralizationMeasure.ENTROPY_PRECISION) {
                genValueOfSample = generalization.calculateEntropyPrecision(model, sample);
            }
            genValuesSum = genValuesSum + genValueOfSample;
        }
        return genValuesSum / numberOfSamples;
    }

    /**
     * This method will estimate the population based on the log sampling method
     *
     * @return estimated trace list
     */
    List<String> estimatePopulation() {
        if (logSamplingMethod == LogSamplingMethod.NON_PARAMETRIC) {
            return log;
        } else if (logSamplingMethod == LogSamplingMethod.SEMI_PARAMETRIC) {
            int g = 10000;
            int k = 2;
            double p = 1.0;

            List<String>[] G = new ArrayList[g + 1];
            G[0] = new ArrayList<>(log);

            // Generate log generations
            for (int i = 1; i <= g; i++) {
                G[i] = new LogBreeding().breedLogs(log, G[i - 1], k, p);
            }

            List<String> aggregatedLog = new ArrayList<>();
            for (List<String> bredLog : G) {
                aggregatedLog.addAll(bredLog);
            }
            return aggregatedLog;
        }
        else return null;
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
        return sample;

    }

    /**
     * This method will calculate the generalization of given logs
     * Used algorithm : Generalization Value = Number of distinct traces in sample / Number of distinct traces in log
     * Assumptions : All the traces in sample are included in the log (recall = 1)
     *
     * @param logSample bootstrapped log sample
     * @return generalization value of the log sample
     */
    private double calculateGeneralization(List<String> logSample) {
        Set<String> distinctTracesInLog = new HashSet<>(log);
        Set<String> distinctTracesInSample = new HashSet<>(logSample);
        return (double) distinctTracesInSample.size() /distinctTracesInLog.size();
    }

}
