package org.bootstrap.gen;

import org.apache.commons.math3.util.Pair;
import org.jbpt.pm.quality.EntropyPrecisionRecallMeasure;
import org.jbpt.pm.tools.QualityMeasuresCLI;

public class Generalization {

    /**
     * This method will calculate the entropy-based model-log recall and precision of a given model and a log
     *
     * @return recall and precision values
     */
    public Pair<Double, Double> calculateEntropyRecallPrecision() throws Exception {
        Object relevantTraces = QualityMeasuresCLI.parseModel("/Users/anandik/Documents/GR/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/samplelogfile.xes");
        Object retrievedTraces = QualityMeasuresCLI.parseModel("/Users/anandik/Documents/GR/Code/BootstrapGeneralizationBasic_GR/src/main/java/org/bootstrap/resources/model.pnml");
        EntropyPrecisionRecallMeasure epr = new EntropyPrecisionRecallMeasure(relevantTraces, retrievedTraces, 0, 0, true, true, false);
        Pair<Double, Double> result = epr.computeMeasure();
        return result;
    }

}
