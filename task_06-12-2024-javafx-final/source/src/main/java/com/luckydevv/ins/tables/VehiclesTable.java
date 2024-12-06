package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.Vehicle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiclesTable extends Application {

    private TableView<Vehicle> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Транспортных Средств");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить ТС");
        addButton.setOnAction(e -> addVehicle());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadVehicles();
    }

    private void setupTable() {
        TableColumn<Vehicle, Integer> vehicleIdCol = new TableColumn<>("ID транспорта");
        vehicleIdCol.setCellValueFactory(cellData -> cellData.getValue().vehicleIdProperty().asObject());

        TableColumn<Vehicle, Integer> policyIdCol = new TableColumn<>("ID полиса");
        policyIdCol.setCellValueFactory(cellData -> cellData.getValue().policyIdProperty().asObject());

        TableColumn<Vehicle, String> makeCol = new TableColumn<>("Марка:");
        makeCol.setCellValueFactory(cellData -> cellData.getValue().makeProperty());

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Модель");
        modelCol.setCellValueFactory(cellData -> cellData.getValue().modelProperty());

        TableColumn<Vehicle, Integer> yearCol = new TableColumn<>("Год выпуска");
        yearCol.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());

        table.getColumns().addAll(vehicleIdCol, policyIdCol, makeCol, modelCol, yearCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicles");
            while (resultSet.next()) {
                vehicles.add(new Vehicle(
                        resultSet.getInt("vehicle_id"),
                        resultSet.getInt("policy_id"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year")
                ));
            }
            table.getItems().setAll(vehicles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addVehicle() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление ТС");
        dialog.setHeaderText("Введите информацию о транспортном средстве:");

        dialog.setContentText("ID полиса:");
        String police_id = dialog.showAndWait().orElse("");
        if (police_id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели ID полиса!");
            alert.showAndWait();
            return;
        }
        int police_id_int;
        try {
            police_id_int = Integer.parseInt(police_id);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID полиса должен быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Марка машины:");
        String make = dialog.showAndWait().orElse("");
        if (make.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели марку машины!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Модель:");
        String model = dialog.showAndWait().orElse("");
        if (model.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели модель!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Год выпуска:");
        String year = dialog.showAndWait().orElse("");
        if (year.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели год выпуска!");
            alert.showAndWait();
            return;
        }
        int year_int;
        try {
            year_int = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Год выпуска должен быть числом!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO vehicles (policy_id, make, model, year) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, police_id_int);
            preparedStatement.setString(2, make);
            preparedStatement.setString(3, model);
            preparedStatement.setInt(4, year_int);
            preparedStatement.executeUpdate();
            loadVehicles();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить ТС в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
