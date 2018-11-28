package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoWithJDBC implements SupplierDao {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/todolist";
    private static final String DB_USER = "noemi";
    private static final String DB_PASSWORD = "Finally over";


    private List<Supplier> data = new ArrayList<>();
    private static SupplierDaoWithJDBC instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoWithJDBC() {
    }

    public static SupplierDaoWithJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoWithJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {

        String query = "INSERT INTO webshop (id, name, description) " +
                "VALUES ('" + supplier.getId() + "', '" + supplier.getName() + "', '" + supplier.getDescription() + "');";
        executeQuery(query);

    }

    @Override
    public Supplier find(int id) {

        String query = "SELECT * FROM webshop WHERE id ='" + id + "';";

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                Supplier result = new Supplier(resultSet.getString("id"),
                        resultSet.getString("name"));
                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void remove(int id) {

        String query = "DELETE FROM webshop WHERE id = '" + id +"';";
        executeQuery(query);

    }

    @Override
    public List<Supplier> getAll() {

        String query = "SELECT * FROM webshop;";

        List<Supplier> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()){
                Supplier actTodo = new Supplier(resultSet.getString("id"),
                        resultSet.getString("name"));
                resultList.add(actTodo);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


