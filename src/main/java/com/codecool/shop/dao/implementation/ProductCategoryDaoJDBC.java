package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE = "jdbc:postgresql://localhost:5432/webshop";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private List<ProductCategory> data = new ArrayList<>();
    private static ProductCategoryDaoJDBC instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoJDBC() {
    }

    public static com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        try (Connection connection = getConnection();
             PreparedStatement addNewCategory = connection.prepareStatement(
                     "INSERT INTO ProductCategory (department, description, name) VALUES (?, ?, ?);");
        ){
            addNewCategory.setString(1, category.getDepartment());
            addNewCategory.setString(2, category.getDescription());
            addNewCategory.setString(3, category.getName());
            addNewCategory.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM ProductCategory WHERE id ='" + id + "';";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getString("description"));
                return productCategory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try (Connection connection = getConnection();
             PreparedStatement removeCategory = connection.prepareStatement(
                     "DELETE * FROM ProductCategory WHERE id = ?);");
        ){
            removeCategory.setInt(1, id);
            removeCategory.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM ProductCategory;";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getString("description"));
                data.add(productCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }
}
