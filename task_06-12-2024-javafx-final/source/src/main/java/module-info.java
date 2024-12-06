module com.luckydevv.ins {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens com.luckydevv.ins to javafx.fxml;
    exports com.luckydevv.ins;
    exports com.luckydevv.ins.models;
    opens com.luckydevv.ins.models to javafx.fxml;
    exports com.luckydevv.ins.tables;
    opens com.luckydevv.ins.tables to javafx.fxml;
}