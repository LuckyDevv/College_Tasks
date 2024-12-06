package com.luckydevv.ins.models;

import javafx.beans.property.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Policy {
    private final IntegerProperty policyId;
    private final IntegerProperty clientId;
    private final IntegerProperty agentId;
    private final IntegerProperty companyId;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty endDate;
    private final DoubleProperty premium;

    public Policy(int policyId, int clientId, int agentId, int companyId, Date startDate, Date endDate, double premium) {
        this.policyId = new SimpleIntegerProperty(policyId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.agentId = new SimpleIntegerProperty(agentId);
        this.companyId = new SimpleIntegerProperty(companyId);
        this.startDate = new SimpleStringProperty(startDate.toString());
        this.endDate = new SimpleStringProperty(endDate.toString());
        this.premium = new SimpleDoubleProperty(premium);
    }

    public IntegerProperty policyIdProperty() {
        return policyId;
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public IntegerProperty agentIdProperty() {
        return agentId;
    }

    public IntegerProperty companyIdProperty() {
        return companyId;
    }

    public ObjectProperty<Date> startDateProperty() {
        return getDateObjectProperty(startDate);
    }

    public ObjectProperty<Date> endDateProperty() {
        return getDateObjectProperty(endDate);
    }

    public static ObjectProperty<Date> getDateObjectProperty(SimpleStringProperty startDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = format.parse(startDate.get());
            Date sqlDate = new Date(parsedDate.getTime());
            return new SimpleObjectProperty<>(sqlDate);
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DoubleProperty premiumProperty() {
        return premium;
    }
}
