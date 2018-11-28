package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoJDBC implements ProductDao {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "Jani";
    private static final String DB_PASSWORD = "qwert12345";

    private List<Product> data = new ArrayList<>();
    private static ProductDaoJDBC instance = null;
    private SupplierDao supplierDaoDataStore = SupplierDaoWithJDBC.getInstance();

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoJDBC() {
    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement addNewProduct = connection.prepareStatement(
                     "INSERT INTO Product (name , defaultPrice , productCategoryId, supplierId, defaultCurrency)" +
                             " VALUES (?, ?, ?, ?, ?);");
        ) {
            addNewProduct.setString(1, product.getName());
            addNewProduct.setString(2, String.valueOf(product.getDefaultPrice()));
            addNewProduct.setString(3, String.valueOf(product.getProductCategory()));
            addNewProduct.setString(4, String.valueOf(product.getSupplier()));
            addNewProduct.setString(5, String.valueOf(product.getDefaultCurrency()));
            addNewProduct.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Product find(int id) {
        String query = "SELECT * FROM Product " + "WHERE id=?;";
        try (Connection connection = getConnection();
             Statement findProduct = connection.createStatement();
             ResultSet resultSet = findProduct.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(id,
                        resultSet.getString("name"),
                        resultSet.getFloat("defaultPrice"),
                        resultSet.getString("defaultCurrency"),
                        resultSet.getString("productCategory"),
                        supplierDaoDataStore.find(resultSet.getString("supplierId")));

                connection.close();
                return product;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void remove(int id) {
        String query = "DELETE * FROM Product WHERE id = ?;";
        try (Connection connection = getConnection();
             PreparedStatement deleteFromProduct = connection.prepareStatement(query);
        ) {
            deleteFromProduct.setInt(1, id);
            deleteFromProduct.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product;";
        try (Connection connection = getConnection();
             Statement getAll = connection.createStatement();
             ResultSet resultSet = getAll.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"),
                        resultSet.getFloat("defaultPrice"),
                        resultSet.getString("defaultCurrency"),
                        resultSet.getString("productCategory"),
                        supplierDaoDataStore.find(resultSet.getString("supplierId")));
                products.add(product);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        int suppId = supplier.getId();
        List<Product> productsById= new ArrayList<>();
        String query = "SELECT * FROM Product WHERE Supplier.id = " + suppId + ";";
        try (Connection connection = getConnection();
             Statement getProductsBySupplier = connection.createStatement();
             ResultSet resultSet = getProductsBySupplier.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"),
                        resultSet.getFloat("defaultPrice"),
                        resultSet.getString("defaultCurrency"),
                        resultSet.getString("productCategory"),
                        supplierDaoDataStore.find(resultSet.getString("supplierId")));
                productsById.add(product);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsById;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        int productCategoryId = productCategory.getId();
        List<Product> productsById= new ArrayList<>();
        String query = "SELECT * FROM Product WHERE Supplier.id = " + productCategoryId + ";";
        try (Connection connection = getConnection();
             Statement getProductsBySupplier = connection.createStatement();
             ResultSet resultSet = getProductsBySupplier.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"),
                        resultSet.getFloat("defaultPrice"),
                        resultSet.getString("defaultCurrency"),
                        resultSet.getString("productCategory"),
                        supplierDaoDataStore.find(resultSet.getString("supplierId")));
                productsById.add(product);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsById;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
