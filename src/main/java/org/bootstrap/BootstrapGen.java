package org.bootstrap;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.util.Pair;
import org.bootstrap.bootstrap.BootstrapTerminationCriterion;
import org.bootstrap.bootstrap.BootstrapTerminationCriterionEnum;
import org.bootstrap.gen.Generalization;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogBreeding;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.lsm.LogSamplingMethodEnum;
import org.bootstrap.lsm.SemiParametricLSM;
import org.utils.log.Trace;

import java.util.*;
import java.util.stream.Collectors;

public class BootstrapGen {
    private final int sampleSize;
    private final List<Trace> log;
    private final LogSamplingMethod logSamplingMethod;
    private final String modelFilePath;
    private final String sampleFilePath = "./src/main/java/org/bootstrap/resources/samplelogfile.xes";
    private final BootstrapTerminationCriterion bootstrapTerminationCriterion;

    public BootstrapGen(int sampleSize, BootstrapTerminationCriterion bootstrapTerminationCriterion, EventLog eventLog, LogSamplingMethod logSamplingMethod, String modelFilePath) {
        this.sampleSize = sampleSize;
        this.bootstrapTerminationCriterion = bootstrapTerminationCriterion;
        this.log = eventLog.getTraceList();
        this.logSamplingMethod = logSamplingMethod;
        this.modelFilePath = modelFilePath;
    }

    public double[] calculateGen() {
        if (bootstrapTerminationCriterion.getBootstrapTerminationCriterion().equals(BootstrapTerminationCriterionEnum.CONFIDENCE_INTERVAL)) {
            return calculateGenUsingConfidenceInterval(bootstrapTerminationCriterion.getCriterionValue());
        } else if (bootstrapTerminationCriterion.getBootstrapTerminationCriterion().equals(BootstrapTerminationCriterionEnum.FIXED_SAMPLES)) {
            return calculateGenFixedSamples((int) bootstrapTerminationCriterion.getCriterionValue());
        } else return null;
    }

    private double[] calculateGenUsingConfidenceInterval(double epsilon) {
        Pair<Double, Double> genValueOfSample;
        List<Pair<Double, Double>> genValues = new ArrayList<>();

        Generalization generalization = new Generalization();

        int uniqueTraceCount = 0;
        int numOfVariableSamples = 0;

        double confidenceIntervalHighest = 10;

        while (confidenceIntervalHighest > epsilon) {
            List<Trace> sample = generateSample(log, sampleFilePath);
            numOfVariableSamples++;

            Set<Trace> distinctTraces = new HashSet<>(sample);
            uniqueTraceCount += distinctTraces.size();

            try {
                genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
                genValues.add(genValueOfSample);
            } catch (Exception e) {
                e.printStackTrace();
            }

            confidenceIntervalHighest = (genValues.size() >= 2) ? calculateHighestConfidenceInterval(genValues) : confidenceIntervalHighest;
        }
        List<Double> genValuesPre = genValues.stream().map(Pair::getValue).collect(Collectors.toList());
        List<Double> genValuesRec = genValues.stream().map(Pair::getKey).collect(Collectors.toList());
        return new double[]{ calculateMean(genValuesPre), calculateConfidenceInterval(genValuesPre), calculateMean(genValuesRec), calculateConfidenceInterval(genValuesRec),
                (double) numOfVariableSamples, (double) uniqueTraceCount/ numOfVariableSamples, epsilon };
    }

    private double[] calculateGenFixedSamples(int numberOfSamples) {
        Pair<Double, Double> genValueOfSample;
        double genValuesRecallSum = 0;
        double genValuesPrecisionSum = 0;
        Generalization generalization = new Generalization();

        int uniqueTraceCount = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            List<Trace> sample = generateSample(log, sampleFilePath);

            Set<Trace> distinctTraces = new HashSet<>(sample);
            uniqueTraceCount += distinctTraces.size();

            try {
                genValueOfSample = generalization.calculateEntropyRecallPrecision(modelFilePath, sampleFilePath);
                genValuesRecallSum += genValueOfSample.getFirst();
                genValuesPrecisionSum += genValueOfSample.getSecond();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new double[]{ genValuesPrecisionSum/numberOfSamples, genValuesRecallSum/numberOfSamples, (double) uniqueTraceCount/numberOfSamples};
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

    private List<Trace> generateSample(List<Trace> log, String sampleFilePath) {
        List<Trace> sample = new ArrayList<>();
        Random random = new Random();
        List<Trace> estimatedPopulation = estimatePopulation(new EventLog(log));
        int maxLimit = estimatedPopulation.size();

        while (sample.size() < sampleSize) {
            int randomIndex = random.nextInt(maxLimit);
            Trace trace = estimatedPopulation.get(randomIndex);
            sample.add(trace);
        }
        new EventLog(sample).serializeEventLog(sampleFilePath);
        return sample;
    }

    /**
     * This method will estimate the population based on the log sampling method
     *
     * @return estimated trace list
     */
    protected List<Trace> estimatePopulation(List<Trace> log) {
        if (logSamplingMethod.getLogSamplingMethodEnum() == LogSamplingMethodEnum.NON_PARAMETRIC) {
            return log;
        } else if (logSamplingMethod.getLogSamplingMethodEnum() == LogSamplingMethodEnum.SEMI_PARAMETRIC) {
            int g = ((SemiParametricLSM) logSamplingMethod).getG();
            int k = ((SemiParametricLSM) logSamplingMethod).getK();
            double p = ((SemiParametricLSM) logSamplingMethod).getP();
            return new LogBreeding().estimatePopulationWithLogBreeding(new EventLog(log), g, k, p);
        } else return null;
    }

}


