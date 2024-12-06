package com.luckydevv.ins.tables;
import com.luckydevv.ins.models.Agent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentsTable extends Application {

    private TableView<Agent> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Агентов");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить агента");
        addButton.setOnAction(e -> addAgent());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadAgents();
    }

    private void setupTable() {
        TableColumn<Agent, String> idCol = new TableColumn<>("ID агента");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Agent, String> firstNameCol = new TableColumn<>("Имя");
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<Agent, String> lastNameCol = new TableColumn<>("Отчество");
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Agent, String> emailCol = new TableColumn<>("Электронная почта");
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAgents() {
        List<Agent> agents = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM agents");
            while (resultSet.next()) {
                agents.add(new Agent(resultSet.getInt("agent_id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("email")));
            }
            table.getItems().setAll(agents);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAgent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление агента");
        dialog.setHeaderText("Введите информацию об агенте:");
        dialog.setContentText("Имя:");

        String firstName = dialog.showAndWait().orElse("");
        if (firstName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели имя!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Отчество:");
        String lastName = dialog.showAndWait().orElse("");
        if (lastName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели отчество!");
            alert.showAndWait();
            return;
        }

        dialog.setContentText("Электронная почта:");
        String email = dialog.showAndWait().orElse("");
        if (email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели электронную почту!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO agents (first_name, last_name, email) VALUES (?, ?, ?)");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            loadAgents();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить агента в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
