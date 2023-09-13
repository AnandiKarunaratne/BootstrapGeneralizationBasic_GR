package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.model.Model;

import java.util.List;

public class Main {
    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 100; // m
    final private static String eventLogFile = "/Users/anandi/Downloads/sampleData.csv";
    final private static String logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) {
        List<String> traces = new EventLog().getSampleTraceList();
        Model model = new Model();
        double genValue = new BootstrapGen(sampleSize, numberOfSamples, traces, logSamplingMethod, model).calculateBootstrapGen();
        System.out.println(genValue);
    }

}