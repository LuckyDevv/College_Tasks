package com.luckydevv.ins.models;
import javafx.beans.property.*;

import java.sql.Date;
import static com.luckydevv.ins.models.Policy.getDateObjectProperty;

public class Payout {
    private final IntegerProperty payoutId;
    private final IntegerProperty claimId;
    private final DoubleProperty amount;
    private final SimpleStringProperty dateOfPayout;

    public Payout(int payoutId, int claimId, double amount, Date dateOfPayout) {
        this.payoutId = new SimpleIntegerProperty(payoutId);
        this.claimId = new SimpleIntegerProperty(claimId);
        this.amount = new SimpleDoubleProperty(amount);
        this.dateOfPayout = new SimpleStringProperty(dateOfPayout.toString());
    }

    public IntegerProperty payoutIdProperty() {
        return payoutId;
    }

    public IntegerProperty claimIdProperty() {
        return claimId;
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public ObjectProperty<Date> dateOfPayoutProperty() {
        return getDateObjectProperty(dateOfPayout);
    }
}
