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
    public Pair<Double, Double> calculateEntropyRecallPrecision(String modelFilePath, String logFilePath) throws Exception {
        Object relevantTraces = QualityMeasuresCLI.parseModel(logFilePath);
        Object retrievedTraces = QualityMeasuresCLI.parseModel(modelFilePath);
        EntropyPrecisionRecallMeasure epr = new EntropyPrecisionRecallMeasure(relevantTraces, retrievedTraces, 0, 0, true, true, false);
        Pair<Double, Double> result = epr.computeMeasure();
        return result;
    }

}
