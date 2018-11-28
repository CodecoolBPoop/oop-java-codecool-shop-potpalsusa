package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC extends ConnectionCreater implements SupplierDao {

    private List<Supplier> data = new ArrayList<>();
    private static SupplierDaoJDBC instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {

        try {
            Connection connection = getConnection();
            PreparedStatement addNewSupplier = connection.prepareStatement(
                     "INSERT INTO supplier (supplier_name, description) VALUES ( ?, ?);");

            addNewSupplier.setString(1, supplier.getName());
            addNewSupplier.setString(2, supplier.getDescription());
            addNewSupplier.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Supplier find(int id) {

        String query = "SELECT * FROM supplier WHERE id ='" + id + "';";

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if (resultSet.next()){
                Supplier result = new Supplier(resultSet.getString("id"),
                        resultSet.getString("supplier_name"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void remove(int id) {

        String query = "DELETE FROM Supplier WHERE id = '" + id +"';";
        executeQuery(query);

    }

    @Override
    public List<Supplier> getAll() {

        String query = "SELECT * FROM supplier;";

        List<Supplier> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()){
                Supplier actTodo = new Supplier(
                        resultSet.getInt("id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("description"));
                resultList.add(actTodo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

}


