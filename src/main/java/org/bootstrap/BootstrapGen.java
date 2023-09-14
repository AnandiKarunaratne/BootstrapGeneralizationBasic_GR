package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.model.Model;

import java.util.*;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<String> log;
    private final String logSamplingMethod;
    private final Model model;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, String logSamplingMethod, Model model) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getSampleTraceList();
        this.logSamplingMethod = logSamplingMethod;
        this.model = model;
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     *
     * @return bootstrap generalization value
     */
    public double[] calculateBootstrapGen() {
        double genValueOfSample1;
        double genValueOfSample2;
        double genValuesSum1 = 0;
        double genValuesSum2 = 0;
        double[] genValues = new double[2];
        Generalization generalization = new Generalization();

        List<String> estimatedPopulation = estimatePopulation();
        for (int i = 0; i < numberOfSamples; i++) {
            genValueOfSample1 = generalization.calculateEntropyRecall(model.getModel(), generateSample(estimatedPopulation));
            genValueOfSample2 = generalization.calculateEntropyPrecision(model, generateSample(estimatedPopulation));
            genValuesSum1 = genValuesSum1 + genValueOfSample1;
            genValuesSum2 = genValuesSum2 + genValueOfSample2;
        }
        genValues[0] = genValuesSum1 / numberOfSamples;
        genValues[1] = genValuesSum2 / numberOfSamples;
        return genValues;
    }

    /**
     * This method will estimate the population based on the log sampling method
     *
     * @return estimated trace list
     */
    List<String> estimatePopulation() {
        if (logSamplingMethod.equals(LogSamplingMethod.NON_PARAMETRIC)) {
            return log;
        } else if (logSamplingMethod.equals(LogSamplingMethod.SEMI_PARAMETRIC)) {
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
