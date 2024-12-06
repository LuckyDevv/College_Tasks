package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.Claim;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClaimsTable extends Application {

    private TableView<Claim> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Страховых случаев");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить страховой случай");
        addButton.setOnAction(e -> addClaim());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadClaims();
    }

    private void setupTable() {
        TableColumn<Claim, Integer> claimIdCol = new TableColumn<>("ID страхового случая");
        claimIdCol.setCellValueFactory(cellData -> cellData.getValue().claimIdProperty().asObject());

        TableColumn<Claim, Integer> policyIdCol = new TableColumn<>("ID полиса");
        policyIdCol.setCellValueFactory(cellData -> cellData.getValue().policyIdProperty().asObject());

        TableColumn<Claim, Date> dateOfClaimCol = new TableColumn<>("Дата");
        dateOfClaimCol.setCellValueFactory(cellData -> cellData.getValue().dateOfClaimProperty());

        TableColumn<Claim, Double> amountCol = new TableColumn<>("Сумма");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        TableColumn<Claim, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(claimIdCol, policyIdCol, dateOfClaimCol, amountCol, statusCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClaims() {
        List<Claim> claims = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM claims");
            while (resultSet.next()) {
                claims.add(new Claim(
                        resultSet.getInt("claim_id"),
                        resultSet.getInt("policy_id"),
                        resultSet.getDate("date_of_claim"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("status")
                ));
            }
            table.getItems().setAll(claims);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addClaim() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление Страхового случая");
        dialog.setHeaderText("Введите информацию о страховом случае:");

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
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID полиса должен быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Дата:");
        String date_str = dialog.showAndWait().orElse("");
        if (date_str.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели дату!");
            alert.showAndWait();
            return;
        }
        Date sqlDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsedDate = format.parse(String.valueOf(date_str));
            sqlDate = new Date(parsedDate.getTime());
        }catch(ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Дата должна вводиться в формате: дд.мм.гггг");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Сумма:");
        String amount = dialog.showAndWait().orElse("");
        if (amount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели сумму!");
            alert.showAndWait();
            return;
        }
        int amount_int;
        try {
            amount_int = Integer.parseInt(amount);
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Сумма должна быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Статус:");
        String status = dialog.showAndWait().orElse("");
        if (status.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели статус!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO claims (policy_id, date_of_claim, amount, status) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, police_id_int);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setInt(3, amount_int);
            preparedStatement.setString(4, status);
            preparedStatement.executeUpdate();
            loadClaims();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить страховой случай в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
