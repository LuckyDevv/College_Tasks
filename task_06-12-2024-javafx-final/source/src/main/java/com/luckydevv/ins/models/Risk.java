package com.luckydevv.ins.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Risk {
    private final IntegerProperty riskId;
    private final StringProperty description;
    private final IntegerProperty policyId;

    public Risk(int riskId, String description, int policyId) {
        this.riskId = new SimpleIntegerProperty(riskId);
        this.description = new SimpleStringProperty(description);
        this.policyId = new SimpleIntegerProperty(policyId);
    }

    public IntegerProperty riskIdProperty() {
        return riskId;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public IntegerProperty policyIdProperty() {
        return policyId;
    }
}
