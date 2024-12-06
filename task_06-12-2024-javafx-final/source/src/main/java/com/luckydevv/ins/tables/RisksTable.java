package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.Risk;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RisksTable extends Application {

    private TableView<Risk> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Рисков");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить риск");
        addButton.setOnAction(e -> addRisk());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadRisks();
    }

    private void setupTable() {
        TableColumn<Risk, Integer> riskIdCol = new TableColumn<>("ID риска");
        riskIdCol.setCellValueFactory(cellData -> cellData.getValue().riskIdProperty().asObject());

        TableColumn<Risk, String> descriptionCol = new TableColumn<>("Описание");
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Risk, Integer> policyIdCol = new TableColumn<>("ID полиса");
        policyIdCol.setCellValueFactory(cellData -> cellData.getValue().policyIdProperty().asObject());

        table.getColumns().addAll(riskIdCol, descriptionCol, policyIdCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRisks() {
        List<Risk> risks = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM risks");
            while (resultSet.next()) {
                risks.add(new Risk(
                        resultSet.getInt("risk_id"),
                        resultSet.getString("description"),
                        resultSet.getInt("policy_id")
                ));
            }
            table.getItems().setAll(risks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRisk() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление риска");
        dialog.setHeaderText("Введите информацию о риске:");
        dialog.setContentText("Описание риска:");

        String description = dialog.showAndWait().orElse("");
        if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели описание риска!");
            alert.showAndWait();
            return;
        }

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

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO risks (description, policy_id) VALUES (?, ?)");
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, police_id_int);
            preparedStatement.executeUpdate();
            loadRisks();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить риск в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

