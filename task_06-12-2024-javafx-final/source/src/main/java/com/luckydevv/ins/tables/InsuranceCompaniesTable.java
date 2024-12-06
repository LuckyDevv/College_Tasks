package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.InsuranceCompany;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsuranceCompaniesTable extends Application {

    private TableView<InsuranceCompany> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Страховых компаний");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить компанию");
        addButton.setOnAction(e -> addCompany());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadCompanies();
    }

    private void setupTable() {
        TableColumn<InsuranceCompany, String> idCol = new TableColumn<>("ID компании");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<InsuranceCompany, String> nameCol = new TableColumn<>("Название компании");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<InsuranceCompany, String> addressCol = new TableColumn<>("Адрес");
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        TableColumn<InsuranceCompany, String> phoneCol = new TableColumn<>("Номер телефона");
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        table.getColumns().addAll(idCol, nameCol, addressCol, phoneCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCompanies() {
        List<InsuranceCompany> companies = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM insurance_companies");
            while (resultSet.next()) {
                companies.add(new InsuranceCompany(resultSet.getInt("company_id"), resultSet.getString("name"), resultSet.getString("address"), resultSet.getString("phone")));
            }
            table.getItems().setAll(companies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCompany() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление компании");
        dialog.setHeaderText("Введите информацию о компании:");

        dialog.setContentText("Название компании:");
        String name = dialog.showAndWait().orElse("");
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели название компании!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Адрес:");
        String address = dialog.showAndWait().orElse("");
        if (address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели адрес!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Номер телефона:");
        String phone = dialog.showAndWait().orElse("");
        if (phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели номер телефона!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO insurance_companies (name, address, phone) VALUES (?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, phone);
            preparedStatement.executeUpdate();
            loadCompanies();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить страховую компанию в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


