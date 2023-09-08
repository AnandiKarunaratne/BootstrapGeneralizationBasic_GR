package org.bootstrap;

import java.util.*;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<String> log;

    public BootstrapGen(int sampleSize, int numberOfSamples, String eventLogFile) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = new EventLog(eventLogFile).getTraceList();
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     *
     * @return bootstrap generalization value
     */
    public double calculateBootstrapGen() {
        double genValueOfSample;
        double genValuesSum = 0;
        for (int i = 0; i < numberOfSamples; i++) {
            genValueOfSample = calculateGeneralization(generateSample());
            genValuesSum = genValuesSum + genValueOfSample;
        }
        return genValuesSum / numberOfSamples;
    }

    /**
     * This method will generate a sample of the given sample size using the trace list
     *
     * @return sample of traces
     */
    private List<String> generateSample() {
        List<String> sample = new ArrayList<>();
        Random random = new Random();
        int maxLimit = log.size();

        while (sample.size() < sampleSize) {
            int randomIndex = random.nextInt(maxLimit);
            String trace = log.get(randomIndex);
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
