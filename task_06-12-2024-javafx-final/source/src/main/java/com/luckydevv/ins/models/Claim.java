package com.luckydevv.ins.models;

import javafx.beans.property.*;

import java.sql.Date;

import static com.luckydevv.ins.models.Policy.getDateObjectProperty;

public class Claim {
    private final IntegerProperty claimId;
    private final IntegerProperty policyId;
    private final SimpleStringProperty dateOfClaim;
    private final DoubleProperty amount;
    private final SimpleStringProperty status;

    public Claim(int claimId, int policyId, Date dateOfClaim, double amount, String status) {
        this.claimId = new SimpleIntegerProperty(claimId);
        this.policyId = new SimpleIntegerProperty(policyId);
        this.dateOfClaim = new SimpleStringProperty(dateOfClaim.toString());
        this.amount = new SimpleDoubleProperty(amount);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty claimIdProperty() {
        return claimId;
    }

    public IntegerProperty policyIdProperty() {
        return policyId;
    }

    public ObjectProperty<Date> dateOfClaimProperty() {
        return getDateObjectProperty(dateOfClaim);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
