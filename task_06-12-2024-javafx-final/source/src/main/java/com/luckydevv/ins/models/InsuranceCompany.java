package com.luckydevv.ins.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InsuranceCompany {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty phone;

    public InsuranceCompany(int id, String name, String address, String phone) {
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
    }

    public StringProperty idProperty() {
        return this.id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty phoneProperty() {
        return phone;
    }
}

