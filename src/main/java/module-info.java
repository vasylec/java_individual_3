module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens com.example to javafx.fxml;
    opens com.example.Controllers to javafx.fxml;
    opens com.example.Model to javafx.fxml;

    exports com.example;
    exports com.example.Controllers;
    exports com.example.Model;
}
