package com.luckydevv.ins.tables;

import com.luckydevv.ins.models.CoverageArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoverageAreasTable extends Application {

    private TableView<CoverageArea> table;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Таблица Зоны Покрытия");

        table = new TableView<>();
        setupTable();

        Button addButton = new Button("Добавить зону покрытия");
        addButton.setOnAction(e -> addCoverageArea());

        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(addButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase();
        loadCoverageAreas();
    }

    private void setupTable() {
        TableColumn<CoverageArea, Integer> coverageAreaIdCol = new TableColumn<>("ID зоны");
        coverageAreaIdCol.setCellValueFactory(cellData -> cellData.getValue().coverageAreaIdProperty().asObject());

        TableColumn<CoverageArea, String> descriptionCol = new TableColumn<>("Описание");
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        table.getColumns().addAll(coverageAreaIdCol, descriptionCol);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ins_db?allowPublicKeyRetrieval=True&useSSL=false&serverTimezone=Europe/Moscow", "root", "eather1192@How91");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCoverageAreas() {
        List<CoverageArea> coverageAreas = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM coverage_Areas");
            while (resultSet.next()) {
                coverageAreas.add(new CoverageArea(
                        resultSet.getInt("coverage_area_id"),
                        resultSet.getString("description")
                ));
            }
            table.getItems().setAll(coverageAreas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCoverageArea() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавление Зоны покрытия");
        dialog.setHeaderText("Введите информацию о зоне покрытия:");

        dialog.setContentText("Описание зоны покрытия:");
        String description = dialog.showAndWait().orElse("");
        if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Вы не ввели описание!");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO coverage_Areas (description) VALUES (?)");
            preparedStatement.setString(1, description);
            preparedStatement.executeUpdate();
            loadCoverageAreas();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось добавить зону покрытия в базу данных!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
