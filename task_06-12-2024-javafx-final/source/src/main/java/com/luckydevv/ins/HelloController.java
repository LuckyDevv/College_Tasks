package com.luckydevv.ins;

import com.luckydevv.ins.tables.*;
import javafx.stage.Stage;

public class HelloController {
    public void openClientsTable() {
        ClientsTable table = new ClientsTable();
        table.start(new Stage());
    }

    public void openAgentsTable() {
        AgentsTable table = new AgentsTable();
        table.start(new Stage());
    }

    public void openClaimsTable() {
        ClaimsTable table = new ClaimsTable();
        table.start(new Stage());
    }

    public void openCoverageAreasTable() {
        CoverageAreasTable table = new CoverageAreasTable();
        table.start(new Stage());
    }

    public void openInsuranceCompaniesTable() {
        InsuranceCompaniesTable table = new InsuranceCompaniesTable();
        table.start(new Stage());
    }

    public void openPayoutsTable() {
        PayoutsTable table = new PayoutsTable();
        table.start(new Stage());
    }

    public void openPoliciesTable() {
        PoliciesTable table = new PoliciesTable();
        table.start(new Stage());
    }

    public void openRisksTable() {
        RisksTable table = new RisksTable();
        table.start(new Stage());
    }

    public void openVehiclesTable() {
        VehiclesTable table = new VehiclesTable();
        table.start(new Stage());
    }
}
