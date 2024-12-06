package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.Policy;
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

public class PoliciesTable extends Application {

    private TableView<Policy> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Полисов:");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить полис");
        addButton.setOnAction(e -> addPolicy());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadPolicies();
    }

    private void setupTable() {
        TableColumn<Policy, Integer> policyIdCol = new TableColumn<>("ID полиса");
        policyIdCol.setCellValueFactory(cellData -> cellData.getValue().policyIdProperty().asObject());

        TableColumn<Policy, String> clientIdCol = new TableColumn<>("ID клиента");
        clientIdCol.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asString());

        TableColumn<Policy, String> agentIdCol = new TableColumn<>("ID агента");
        agentIdCol.setCellValueFactory(cellData -> cellData.getValue().agentIdProperty().asString());

        TableColumn<Policy, String> companyIdCol = new TableColumn<>("ID компании");
        companyIdCol.setCellValueFactory(cellData -> cellData.getValue().companyIdProperty().asString());

        TableColumn<Policy, Date> startDateCol = new TableColumn<>("Дата регистрации");
        startDateCol.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());

        TableColumn<Policy, Date> endDateCol = new TableColumn<>("Дата окончания");
        endDateCol.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        TableColumn<Policy, Double> premiumCol = new TableColumn<>("Премиум");
        premiumCol.setCellValueFactory(cellData -> cellData.getValue().premiumProperty().asObject());

        table.getColumns().addAll(policyIdCol, clientIdCol, agentIdCol, companyIdCol, startDateCol, endDateCol, premiumCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPolicies() {
        List<Policy> policies = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM policies");
            while (resultSet.next()) {
                policies.add(new Policy(
                        resultSet.getInt("policy_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("agent_id"),
                        resultSet.getInt("company_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getDouble("premium")
                ));
            }
            table.getItems().setAll(policies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPolicy() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление полиса");
        dialog.setHeaderText("Введите информацию о полисе:");

        dialog.setContentText("ID клиента:");
        String client_id = dialog.showAndWait().orElse("");
        if (client_id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели ID клиента!");
            alert.showAndWait();
            return;
        }
        int client_id_int;
        try {
            client_id_int = Integer.parseInt(client_id);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID клиента должен быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("ID агента:");
        String agent_id = dialog.showAndWait().orElse("");
        if (agent_id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели ID агента!");
            alert.showAndWait();
            return;
        }
        int agent_id_int;
        try {
            agent_id_int = Integer.parseInt(agent_id);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID агента должен быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("ID страховой компании:");
        String company_id = dialog.showAndWait().orElse("");
        if (company_id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели ID компании!");
            alert.showAndWait();
            return;
        }
        int company_id_int;
        try {
            company_id_int = Integer.parseInt(company_id);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID компании должен быть числом!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Дата регистрации:");
        String start_date_str = dialog.showAndWait().orElse("");
        if (start_date_str.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели дату!");
            alert.showAndWait();
            return;
        }
        Date start_sqlDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsedDate = format.parse(start_date_str);
            start_sqlDate = new Date(parsedDate.getTime());
        }catch(ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Дата регистрации должна вводиться в формате: дд.мм.гггг");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Дата окончания:");
        String end_date_str = dialog.showAndWait().orElse("");
        if (end_date_str.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели дату!");
            alert.showAndWait();
            return;
        }
        Date end_sqlDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsedDate = format.parse(end_date_str);
            end_sqlDate = new Date(parsedDate.getTime());
        }catch(ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Дата окончания должна вводиться в формате: дд.мм.гггг");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Стоимость оформления:");
        String amount = dialog.showAndWait().orElse("");
        if (amount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели стоимость оформления");
            alert.showAndWait();
            return;
        }
        double amount_int;
        try {
            amount_int = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Стоимость оформления должна быть числом!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO policies (client_id, agent_id, company_id, start_date, end_date, premium) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, client_id_int);
            preparedStatement.setInt(2, agent_id_int);
            preparedStatement.setInt(3, company_id_int);
            preparedStatement.setDate(4, start_sqlDate);
            preparedStatement.setDate(5, end_sqlDate);
            preparedStatement.setDouble(6, amount_int);
            preparedStatement.executeUpdate();
            loadPolicies();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить полис в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

