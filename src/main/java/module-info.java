 module org.example {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires java.sql;
    requires sqlite.jdbc;

    opens org.example to javafx.fxml,javafx.graphics;
    exports org.example;
}
