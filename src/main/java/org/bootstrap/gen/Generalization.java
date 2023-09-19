package org.bootstrap.gen;

import org.apache.commons.math3.linear.*;
import org.bootstrap.model.Model;

import java.util.*;

public class Generalization {

    /**
     * This method will calculate the entropy-based model-log recall of a given model and a log
     *
     * @param model
     * @param log
     * @return recall value
     */
    public double calculateEntropyRecall(Map<String, List<String>> model, List<String> log) {
        return calculateEntropyOfLog(findModelLogIntersection(model, log))/calculateEntropyOfLog(log);
    }

    /**
     * This method will calculate the entropy-based model-log precision of a given model and a log
     *
     * @param model
     * @param log
     * @return precision value
     */
    public double calculateEntropyPrecision(Model model, List<String> log) {
        return calculateEntropyOfLog(findModelLogIntersection(model.getModel(), log))/calculateEntropyOfModel(getSampleAutomaton());
    }

    /**
     * This method will calculate the entropy of a given model
     * This calculates the Perron-Frobenius eigenvalue of the short-circuited automaton
     *
     * @param model
     * @return entropy of model
     */
    double calculateEntropyOfModel(double[][] model) {
        double perronFrobeniusEigenvalue = 0;

        double[][] shortCircuitedModel = shortCircuitModel(model);
        try {
            perronFrobeniusEigenvalue = getPerronFrobeniusEigenvalue(shortCircuitedModel);
        } catch (NonSquareMatrixException e) {
            e.printStackTrace();
        }
        return Math.log(perronFrobeniusEigenvalue);
    }

    /**
     * This method will calculate the Perron Frobenius eigen value of a given matrix
     * @param model
     * @return the Perron Frobenius eigen value
     */
    private static double getPerronFrobeniusEigenvalue(double[][] model) {
        RealMatrix matrix = new Array2DRowRealMatrix(model);

        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);

        double[] realEigenvalues = eigenDecomposition.getRealEigenvalues();
        double largestEigenValue = realEigenvalues[0];
        for (double eigenValue : realEigenvalues) {
            if (eigenValue > largestEigenValue) {
                largestEigenValue = eigenValue;
            }
        }
        return largestEigenValue;
    }

    /**
     * This method will produce the short-circuited automaton of a given model
     * This is not yet implemented
     * The short-circuiting is done manually
     *
     * @param model
     * @return short-circuited automaton
     */
    private double[][] shortCircuitModel(double[][] model) {
        // done manually
        model[0][0] = model[0][0] + 1;
        return model;
    }

    /**
     * This method will calculate the entropy value of a given log
     *
     * @param log
     * @return entropy of log
     */
    double calculateEntropyOfLog(List<String> log) {
        int maxTraceLength = 0;
        double limitSup = 0.0;
        Set<String> uniqueTraces = new HashSet<>();

        for (String trace : log) {
            uniqueTraces.add(trace);
            if (trace.length() > maxTraceLength) {
                maxTraceLength = trace.length();
            }
        }
        for (int n = 1; n <= maxTraceLength; n++) {
            int count = 0;
            for (String trace : uniqueTraces) {
                if (trace.length() == n) {
                    count++;
                }
            }
            if (count > 0) {
                double logRatio = Math.log(count) / n;

                if (logRatio > limitSup) {
                    limitSup = logRatio;
                }
            }
        }
        return limitSup;
    }

    /**
     * This method calculates the model-log recall of a given model and a log
     *
     * @param model
     * @param log
     * @return recall value
     */
    public double calculateRecall(Map<String, List<String>> model, List<String> log) {
        int matchingEvents = findModelLogIntersection(model, log).size();
        int totalEventsInLog = log.size();

        return (double) matchingEvents/totalEventsInLog;
    }

    /**
     * This method finds which of the traces in the log are allowed by the model
     *
     * @param model
     * @param log
     * @return the list of traces allowed by the model
     */
    private List<String> findModelLogIntersection(Map<String, List<String>> model, List<String> log) {
        List<String> intersection = new ArrayList<>();

        for (String trace : log) {
            for (int i = 0; i < trace.length() - 1; i++) {
                String currentEvent = String.valueOf(trace.charAt(i));
                String nextEvent = String.valueOf(trace.charAt(i + 1));

                if (model.containsKey(currentEvent) && model.get(currentEvent).contains(nextEvent)) {
                    if (i == trace.length() - 2) {
                        intersection.add(trace);
                    }
                }
                else {
                    break;
                }
            }
        }
        return intersection;
    }

    private double[][] getSampleAutomaton() {
        return new double[][]{{0, 1, 0, 0, 0, 0},
                              {0, 0, 1, 0, 1, 0},
                              {0, 0, 0, 1, 0, 0},
                              {1, 0, 0, 0, 0, 0},
                              {0, 0, 0, 0, 0, 1},
                              {1, 0, 0, 0, 0, 1}};
    }

}
