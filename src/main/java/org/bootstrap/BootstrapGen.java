package org.bootstrap;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.util.Pair;
import org.bootstrap.gen.Generalization;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogBreeding;
import org.bootstrap.lsm.LogSamplingMethod;
import org.utils.log.Trace;

import java.util.*;
import java.util.stream.Collectors;

public class BootstrapGen {
    private final int sampleSize;
    private final int numberOfSamples;
    private final List<Trace> log;
    private final LogSamplingMethod logSamplingMethod;
    private final String modelFilePath;
    private static final String sampleFilePath = "./src/main/java/org/bootstrap/resources/samplelogfile.xes";
    private static int g;
    private static final double epsilon = 0.01;

    public BootstrapGen(int sampleSize, int numberOfSamples, EventLog eventLog, LogSamplingMethod logSamplingMethod, String modelFilePath, int g) {
        this.sampleSize = sampleSize;
        this.numberOfSamples = numberOfSamples;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
        this.modelFilePath = modelFilePath;
        BootstrapGen.g = g;
    }

    public double[] calculateGenUsingVariableSamples() throws Exception {
        Pair<Double, Double> genValueOfSample;
        List<Pair<Double, Double>> genValues = new ArrayList<>();

        Generalization generalization = new Generalization();
        List<Trace> estimatedPopulation = estimatePopulation();

        int uniqueTraceCount = 0;
        int numOfVariableSamples = 0;

        double confidenceIntervalHighest = 10;

        while (confidenceIntervalHighest > epsilon) {
            List<Trace> sample = generateSample(estimatedPopulation);
            numOfVariableSamples++;

            Set<Trace> distinctTraces = new HashSet<>(sample);
            uniqueTraceCount += distinctTraces.size();

            genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
            genValues.add(genValueOfSample);

            confidenceIntervalHighest = (genValues.size() >= 2) ? calculateHighestConfidenceInterval(genValues) : confidenceIntervalHighest;
        }
        List<Double> genValuesPre = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        List<Double> genValuesRec = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        return new double[]{ calculateMean(genValuesPre), calculateConfidenceInterval(genValuesPre), calculateMean(genValuesRec), calculateConfidenceInterval(genValuesRec),
                (double) numOfVariableSamples, (double) uniqueTraceCount/ numOfVariableSamples, epsilon };
    }

    private double calculateHighestConfidenceInterval(List<Pair<Double, Double>> genValues) {
        // recall
        List<Double> recValues = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        double confidenceIntervalRec = calculateConfidenceInterval(recValues);

        // precision
        List<Double> preValues = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        double confidenceIntervalPre = calculateConfidenceInterval(preValues);

        return Math.max(confidenceIntervalRec, confidenceIntervalPre);
    }

    private double calcHighestStdDev(List<Pair<Double, Double>> genValues) {
        // recall
        List<Double> recValues = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        double stdDevRec = calculateStdDev(recValues);

        // precision
        List<Double> preValues = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        double stdDevPre = calculateStdDev(preValues);

        return Math.max(stdDevRec, stdDevPre);
    }

    private double calculateStdDev(List<Double> values) {
        double sum = 0.0;
        for (double num : values) sum += Math.pow(num - calculateMean(values), 2);
        return Math.sqrt(sum / values.size());
    }

    private double calculateMean(List<Double> values) {
        double sum = 0.0;
        for (double num : values) sum += num;
        return sum / values.size();
    }

    private double calculateConfidenceInterval(List<Double> values) {
        double confidenceLevel = 0.95;
        TDistribution tDistribution = new TDistribution(values.size() - 1);
        double marginOfError = tDistribution.inverseCumulativeProbability(1 - (1 - confidenceLevel) / 2);
        double standardError = calculateStdDev(values) / Math.sqrt(values.size());
        return marginOfError * standardError;
    }

    /**
     * This method will calculate the bootstrap generalization value by aggregating the generalization value of samples
     */
    public double[] calculateGenUsingFixedSamples() throws Exception {
        Pair<Double, Double> genValueOfSample;
        double genValuesRecallSum = 0;
        double genValuesPrecisionSum = 0;
        Generalization generalization = new Generalization();
        List<Trace> estimatedPopulation = estimatePopulation();

        int uniqueTraceCount = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            List<Trace> sample = generateSample(estimatedPopulation);

            Set<Trace> distinctTraces = new HashSet<>(sample);
            uniqueTraceCount += distinctTraces.size();

            genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
            genValuesRecallSum += genValueOfSample.getFirst();
            genValuesPrecisionSum += genValueOfSample.getSecond();
        }
        return new double[]{genValuesPrecisionSum/numberOfSamples, genValuesRecallSum/numberOfSamples, (double) uniqueTraceCount/numberOfSamples};
    }

    /**
     * This method will estimate the population based on the log sampling method
     *
     * @return estimated trace list
     */
    protected List<Trace> estimatePopulation() {
        if (logSamplingMethod == LogSamplingMethod.NON_PARAMETRIC) {
            return log;
        } else if (logSamplingMethod == LogSamplingMethod.SEMI_PARAMETRIC) {
            return new LogBreeding().estimatePopulationWithLogBreeding(new EventLog(log), g, 2, 1.0);
        } else return null;
    }

    /**
     * This method will generate a sample of the given sample size using the trace list
     *
     * @return sample of traces
     */
    protected List<Trace> generateSample(List<Trace> estimatedPopulation) {
        List<Trace> sample = new ArrayList<>();
        Random random = new Random();
        int maxLimit = estimatedPopulation.size();

        while (sample.size() < sampleSize) {
            int randomIndex = random.nextInt(maxLimit);
            Trace trace = estimatedPopulation.get(randomIndex);
            sample.add(trace);
        }
        new EventLog(sample).serializeEventLog(sampleFilePath);
//        LogUtils.generateXES(sample, sampleFilePath);
        return sample;
    }

}


