package com.luckydevv.ins.tables;
import com.luckydevv.ins.models.Payout;
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

public class PayoutsTable extends Application {

    private TableView<Payout> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Выплат");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить Выплату");
        addButton.setOnAction(e -> addPayout());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadPayouts();
    }

    private void setupTable() {
        TableColumn<Payout, Integer> payoutIdCol = new TableColumn<>("ID выплаты");
        payoutIdCol.setCellValueFactory(cellData -> cellData.getValue().payoutIdProperty().asObject());

        TableColumn<Payout, Integer> claimIdCol = new TableColumn<>("ID случая");
        claimIdCol.setCellValueFactory(cellData -> cellData.getValue().claimIdProperty().asObject());

        TableColumn<Payout, Double> amountCol = new TableColumn<>("Сумма");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        TableColumn<Payout, Date> dateOfPayoutCol = new TableColumn<>("Дата выплаты");
        dateOfPayoutCol.setCellValueFactory(cellData -> cellData.getValue().dateOfPayoutProperty());

        table.getColumns().addAll(payoutIdCol, claimIdCol, amountCol, dateOfPayoutCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPayouts() {
        List<Payout> payouts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM payouts");
            while (resultSet.next()) {
                payouts.add(new Payout(
                        resultSet.getInt("payout_id"),
                        resultSet.getInt("claim_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("date_of_payout")
                ));
            }
            table.getItems().setAll(payouts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPayout() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление выплаты");
        dialog.setHeaderText("Введите информацию о выплате:");

        dialog.setContentText("ID страхового случая:");
        String claim_id = dialog.showAndWait().orElse("");
        if (claim_id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели ID полиса!");
            alert.showAndWait();
            return;
        }
        int claim_id_int;
        try {
            claim_id_int = Integer.parseInt(claim_id);
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID полиса должен быть числом!");
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

        dialog.setContentText("Дата выплаты:");
        String date_str = dialog.showAndWait().orElse("");
        if (date_str.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели дату!");
            alert.showAndWait();
            return;
        }
        Date sqlDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsedDate = format.parse(date_str);
            sqlDate = new Date(parsedDate.getTime());
        }catch(ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Дата должна вводиться в формате: дд.мм.гггг");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO payouts (claim_id, amount, date_of_payout) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, claim_id_int);
            preparedStatement.setInt(2, amount_int);
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.executeUpdate();
            loadPayouts();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить выплату в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

