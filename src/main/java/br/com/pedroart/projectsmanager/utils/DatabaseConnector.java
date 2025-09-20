package br.com.pedroart.projectsmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/seu_banco_de_dados?useSSL=false&serverTimezone=UTC";
    private static final String USER = "seu_usuario_mysql";
    private static final String PASSWORD = "sua_senha_mysql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
