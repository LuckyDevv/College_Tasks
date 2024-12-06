package com.luckydevv.ins.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class CoverageArea {
    private final IntegerProperty coverageAreaId;
    private final StringProperty description;

    public CoverageArea(int coverageAreaId, String description) {
        this.coverageAreaId = new SimpleIntegerProperty(coverageAreaId);
        this.description = new SimpleStringProperty(description);
    }

    public IntegerProperty coverageAreaIdProperty() {
        return coverageAreaId;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
