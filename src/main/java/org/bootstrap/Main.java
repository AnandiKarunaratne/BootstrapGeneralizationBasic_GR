package org.bootstrap;

import org.bootstrap.log.EventLog;

public class Main {
    final private static int sampleSize = 10; // n
    final private static int numberOfSamples = 10; // m
    final private static String eventLogFile = "/Users/anandi/Downloads/sampleData.csv";
    final private static String logSamplingMethod = LogSamplingMethod.NON_PARAMETRIC;
    public static void main(String[] args) {
        EventLog eventLog = new EventLog(eventLogFile);
        double genValue = new BootstrapGen(sampleSize, numberOfSamples, eventLog, LogSamplingMethod.SEMI_PARAMETRIC).calculateBootstrapGen();
        System.out.println(genValue);
    }

}