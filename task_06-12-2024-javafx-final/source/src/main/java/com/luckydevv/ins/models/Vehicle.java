package com.luckydevv.ins.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Vehicle {
    private final IntegerProperty vehicleId;
    private final IntegerProperty policyId;
    private final StringProperty make;
    private final StringProperty model;
    private final IntegerProperty year;

    public Vehicle(int vehicleId, int policyId, String make, String model, int year) {
        this.vehicleId = new SimpleIntegerProperty(vehicleId);
        this.policyId = new SimpleIntegerProperty(policyId);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleIntegerProperty(year);
    }

    public IntegerProperty vehicleIdProperty() {
        return vehicleId;
    }

    public IntegerProperty policyIdProperty() {
        return policyId;
    }

    public StringProperty makeProperty() {
        return make;
    }

    public StringProperty modelProperty() {
        return model;
    }

    public IntegerProperty yearProperty() {
        return year;
    }
}
