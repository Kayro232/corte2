package edu.uam.ni.evaluacion.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/papeleria_campus";

    private static final String USUARIO = "postgres";

    private static final String CLAVE = "admin123";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
