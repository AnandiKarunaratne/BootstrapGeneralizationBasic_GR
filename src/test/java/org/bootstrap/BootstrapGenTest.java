package org.bootstrap;

import org.bootstrap.lsm.LogSamplingMethod;
import org.junit.Test;
import org.bootstrap.log.EventLog;
import org.utils.log.Trace;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BootstrapGenTest {

    int sampleSize = 5;
    int numberOfSamples = 2;
    EventLog eventLog = new EventLog(new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "b", "c", "f")), new Trace(Arrays.asList("a", "c", "f", "a", "d", "e", "f")))));
    LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;
    String modelFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/model.pnml";
    int g = 2;

    @Test
    public void testCalc() throws Exception {
        BootstrapGen bootstrapGen = new BootstrapGen(100000, 100, new EventLog(), logSamplingMethod, modelFilePath, 10000);
        double[] result = bootstrapGen.calculateGenUsingVariableSamples();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String pre = decimalFormat.format(result[0]);
        String rec = decimalFormat.format(result[2]);

        assertEquals("0.89", pre);
        assertEquals("0.91", rec);
    }

    @Test
    public void testCalculateBootstrapGen() throws Exception {
        BootstrapGen bootstrapGen = new BootstrapGen(100000, 100, new EventLog(), logSamplingMethod, modelFilePath, 10000);
        double[] result = bootstrapGen.calculateGenUsingFixedSamples();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String pre = decimalFormat.format(result[0]);
        String rec = decimalFormat.format(result[1]);

        assertEquals("0.89", pre);
        assertEquals("0.91", rec);
    }

    @Test
    public void testEstimatePopulation() {
        BootstrapGen bootstrapGenNP = new BootstrapGen(sampleSize, numberOfSamples, eventLog, LogSamplingMethod.NON_PARAMETRIC, modelFilePath, g);
        List<Trace> resultNP = bootstrapGenNP.estimatePopulation();
        assertEquals(eventLog.getTraceList(), resultNP);

        BootstrapGen bootstrapGenSP = new BootstrapGen(sampleSize, numberOfSamples, eventLog, LogSamplingMethod.SEMI_PARAMETRIC, modelFilePath, g);
        List<Trace> resultSP = bootstrapGenSP.estimatePopulation();
        assertEquals(eventLog.size() * (g + 1), resultSP.size());
    }

    @Test
    public void testGenerateSample() {
        BootstrapGen bootstrapGen = new BootstrapGen(sampleSize, numberOfSamples, new EventLog(), logSamplingMethod, modelFilePath, g);
        List<Trace> input = new EventLog().getTraceList();
        List<Trace> result = bootstrapGen.generateSample(input);
        assertEquals(sampleSize, result.size());
        assertTrue(input.containsAll(result));
    }

}
