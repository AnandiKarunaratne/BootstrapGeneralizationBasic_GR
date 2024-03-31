package org.bootstrap.bootstrap;

public class BootstrapTerminationCriterion {

    private BootstrapTerminationCriterionEnum bootstrapTerminationCriterion;
    private double criterionValue;

    public BootstrapTerminationCriterion(BootstrapTerminationCriterionEnum bootstrapTerminationCriterionEnum, double criterionValue) {
        this.bootstrapTerminationCriterion = bootstrapTerminationCriterionEnum;
        this.criterionValue = criterionValue;
    }

    public BootstrapTerminationCriterionEnum getBootstrapTerminationCriterion() {
        return bootstrapTerminationCriterion;
    }

    public double getCriterionValue() {
        return criterionValue;
    }

}
