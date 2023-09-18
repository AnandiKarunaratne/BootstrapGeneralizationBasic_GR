package org.bootstrap;

import org.bootstrap.gen.GeneralizationMeasure;
import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.model.Model;

public class Main {
    final private static int sampleSize = 10; // n
    final private static int numberOfSamples = 10; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;
    final private static GeneralizationMeasure generalizationMeasure = GeneralizationMeasure.ENTROPY_PRECISION;

    public static void main(String[] args) {
        EventLog eventLog = new EventLog();
        Model model = new Model();

        double genValue = new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, model, generalizationMeasure).calculateBootstrapGen();
        System.out.println(genValue);
    }

}