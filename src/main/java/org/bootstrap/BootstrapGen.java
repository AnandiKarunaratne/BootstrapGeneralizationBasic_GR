package org.bootstrap;

import org.apache.commons.math3.util.Pair;
import org.bootstrap.gen.Generalization;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogBreeding;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.utils.LogUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<String> log;
    private final LogSamplingMethod logSamplingMethod;
    private final String modelFilePath;
    private static String sampleFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/samplelogfile.xes";
    private static int g;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, LogSamplingMethod logSamplingMethod, String modelFilePath, int g) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
        this.modelFilePath = modelFilePath;
        this.g = g;
    }

    public double[] calc() throws Exception {
        Pair<Double, Double> genValueOfSample;
        List<Pair<Double, Double>> genValues = new ArrayList<>();

        Generalization generalization = new Generalization();
        List<String> estimatedPopulation = estimatePopulation();

        int uniqueTraceCount = 0;
        int numOfSamples = 0;

        double stdDevHighest = 10;

        while (stdDevHighest > 0.1) {
            List<String> sample = generateSample(estimatedPopulation);
            numOfSamples++;

            Set<String> distinctTraces = new HashSet<>(sample);
            uniqueTraceCount += distinctTraces.size();

            genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
            genValues.add(genValueOfSample);

            stdDevHighest = (genValues.size() >=2) ? calcHighestStdDev(genValues) : stdDevHighest;
        }
        List<Double> genValuesPre = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        List<Double> genValuesRec = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        double[] result = {calcMean(genValuesPre), calcStdDev(genValuesPre), calcMean(genValuesRec), calcStdDev(genValuesRec),
                (double) numOfSamples, (double) uniqueTraceCount/numOfSamples};
        return result;
    }

    private double calcHighestStdDev(List<Pair<Double, Double>> genValues) {
        // recall
        List<Double> recValues = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        Double stdDevRec = calcStdDev(recValues);

        // precision
        List<Double> preValues = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        Double stdDevPre = calcStdDev(preValues);

        return (stdDevRec < stdDevPre) ? stdDevPre : stdDevRec;
    }

    private double calcStdDev(List<Double> values) {
        double sum = 0.0;
        for (double num : values) sum += Math.pow(num - calcMean(values), 2);
        return Math.sqrt(sum / values.size());
    }

    private double calcMean(List<Double> values) {
        double sum = 0.0;
        for (double num : values) sum += num;
        return sum / values.size();
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     */
    public double[] calculateBootstrapGen() throws Exception {
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

            genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
            genValuesRecallSum += genValueOfSample.getFirst();
            genValuesPrecisionSum += genValueOfSample.getSecond();
        }
        double[] result = {genValuesPrecisionSum/numberOfSamples, genValuesRecallSum/numberOfSamples, (double) uniqueTraceCount/numberOfSamples};
        return result;
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
            return new LogBreeding().estimatePopulationWithLogBreeding(log, g, 2, 1.0);
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
        LogUtils.generateXES(sample, sampleFilePath);
        return sample;
    }

}


