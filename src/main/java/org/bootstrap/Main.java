package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.model.Model;

public class Main {
    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 100; // m
    final private static String eventLogFile = "/Users/anandi/Downloads/sampleData.csv";
    final private static String logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) {
        EventLog eventLog = new EventLog();
        Model model = new Model();
        double[] genValue = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, model).calculateBootstrapGen();

        for (double i : genValue) System.out.println(i);
    }

}