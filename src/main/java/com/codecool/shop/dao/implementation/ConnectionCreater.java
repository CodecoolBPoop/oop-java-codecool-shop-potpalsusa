package com.codecool.shop.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionCreater {
    protected static final String DATABASE = "jdbc:postgresql://localhost:5432/webshop";
    protected static final String DB_USER = "gabor";
    protected static final String DB_PASSWORD = "";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    protected void executeQuery(String template, Consumer<PreparedStatement> statementBuilder) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(template);
        ){
            statementBuilder.accept(statement);
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
