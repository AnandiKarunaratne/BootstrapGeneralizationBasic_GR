package org.bootstrap;

import org.bootstrap.log.EventLog;

import java.util.*;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private List<String> log;
    private String logSamplingMethod;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, String logSamplingMethod) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     *
     * @return bootstrap generalization value
     */
    public double calculateBootstrapGen() {
        double genValueOfSample;
        double genValuesSum = 0;

        List<String> estimatedPopulation = estimatePopulation();
        for (int i = 0; i < numberOfSamples; i++) {
            genValueOfSample = calculateGeneralization(generateSample(estimatedPopulation));
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
            int g = 10;
            int k = 2;
            double p = 0.5;

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
