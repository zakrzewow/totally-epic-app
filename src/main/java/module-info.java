module pl.zakrzewow.totallyepicapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens pl.zakrzewow.totallyepicapp to javafx.fxml;
    exports pl.zakrzewow.totallyepicapp;
    exports pl.zakrzewow.totallyepicapp.io;
}