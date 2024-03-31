package org.bootstrap;

import org.bootstrap.bootstrap.BootstrapTerminationCriterion;
import org.bootstrap.bootstrap.BootstrapTerminationCriterionEnum;
import org.bootstrap.lsm.LogSamplingMethod;
import org.bootstrap.lsm.LogSamplingMethodEnum;
import org.bootstrap.lsm.NonParametricLSM;
import org.bootstrap.lsm.SemiParametricLSM;
import org.junit.Test;
import org.bootstrap.log.EventLog;
import org.utils.log.Trace;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BootstrapGenTest {

    int sampleSize = 5;
    EventLog eventLog = new EventLog(new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "b", "c", "f")), new Trace(Arrays.asList("a", "c", "f", "a", "d", "e", "f")))));
    String modelFilePath = "/Users/anandik/Library/CloudStorage/OneDrive-TheUniversityofMelbourne/GR/RQ1/Experiments/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/infinite-model.pnml";
    int g = 2;
    BootstrapTerminationCriterion bootstrapTerminationCriterion = new BootstrapTerminationCriterion(BootstrapTerminationCriterionEnum.CONFIDENCE_INTERVAL, 0.01);
    LogSamplingMethod logSamplingMethod = new SemiParametricLSM(LogSamplingMethodEnum.SEMI_PARAMETRIC, 2, 2, 1.0);

    @Test
    public void testCalculateGen() {
        BootstrapTerminationCriterion bootstrapTerminationCriterion = new BootstrapTerminationCriterion(BootstrapTerminationCriterionEnum.FIXED_SAMPLES, 10);
        LogSamplingMethod logSamplingMethod = new SemiParametricLSM(LogSamplingMethodEnum.SEMI_PARAMETRIC, 10000, 2, 1.0);
        BootstrapGen bootstrapGen = new BootstrapGen(100000, bootstrapTerminationCriterion, new EventLog(), logSamplingMethod, false, modelFilePath);
        double[] result = bootstrapGen.calculateGen();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String pre = decimalFormat.format(result[0]);
        String rec = decimalFormat.format(result[1]);

        assertEquals("0.89", pre);
        assertEquals("0.91", rec);

        bootstrapGen = new BootstrapGen(100000, this.bootstrapTerminationCriterion, new EventLog(), logSamplingMethod, false, modelFilePath);
        result = bootstrapGen.calculateGen();

        pre = decimalFormat.format(result[0]);
        rec = decimalFormat.format(result[2]);

        assertEquals("0.89", pre);
        assertEquals("0.91", rec);
    }

    @Test
    public void testEstimatePopulation() {
        LogSamplingMethod logSamplingMethod = new NonParametricLSM(LogSamplingMethodEnum.NON_PARAMETRIC);
        BootstrapGen bootstrapGenNP = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, eventLog, logSamplingMethod, false, modelFilePath);
        List<Trace> resultNP = bootstrapGenNP.estimatePopulation(eventLog);
        assertEquals(eventLog.getTraceList(), resultNP);

        BootstrapGen bootstrapGenSP = new BootstrapGen(sampleSize, bootstrapTerminationCriterion, eventLog, this.logSamplingMethod, false, modelFilePath);
        List<Trace> resultSP = bootstrapGenSP.estimatePopulation(eventLog);
        assertEquals(eventLog.size() * (g + 1), resultSP.size());
    }

}
