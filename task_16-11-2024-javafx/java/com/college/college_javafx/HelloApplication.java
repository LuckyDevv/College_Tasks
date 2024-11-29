package com.college.college_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("EATHER College Task");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        MY_SQL sql = new MY_SQL();
        if (sql.openConnection()) {
            if (sql.createTables()) {
                launch();
            }else System.exit(2);
        }else System.exit(1);
    }
}