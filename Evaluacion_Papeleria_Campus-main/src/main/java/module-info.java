module edu.uam.ni.evaluacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.postgresql.jdbc;

    exports edu.uam.ni.evaluacion;
    exports edu.uam.ni.evaluacion.controller;

    opens edu.uam.ni.evaluacion.controller to javafx.fxml;
    opens edu.uam.ni.evaluacion.model to javafx.base;
}