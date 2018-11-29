package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC extends ConnectionCreater implements ProductDao {

    private List<Product> data = new ArrayList<>();
    private static ProductDaoJDBC instance = null;
    private SupplierDao supplierDaoDataStore = SupplierDaoJDBC.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();

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
                     "INSERT INTO product (name , default_price , product_category_id, supplier_id, default_currency, description)" +
                             " VALUES (?, ?, ?, ?, ?, ?);");
        ) {
            addNewProduct.setString(1, product.getName());
            addNewProduct.setFloat(2, product.getDefaultPrice());
            addNewProduct.setInt(3, product.getProductCategory().getId());
            addNewProduct.setInt(4, product.getSupplier().getId());
            addNewProduct.setString(5, String.valueOf(product.getDefaultCurrency()));
            addNewProduct.setString(6, product.getDescription());
            addNewProduct.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Product find(int id) {
        try (Connection connection = getConnection();
             PreparedStatement findProduct = connection.prepareStatement("SELECT * FROM product WHERE id= ?;")
        ) {
            findProduct.setInt(1, id);
            ResultSet resultSet = findProduct.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        id,
                        resultSet.getString("name"),
                        resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDataStore.find(Integer.parseInt(resultSet.getString("product_category_id"))),
                        supplierDaoDataStore.find(Integer.parseInt(resultSet.getString("supplier_id"))));

                connection.close();
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE * FROM product WHERE id = ?;";
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
        String query = "SELECT * FROM product;";
        try (Connection connection = getConnection();
             Statement getAll = connection.createStatement();
             ResultSet resultSet = getAll.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDataStore.find(Integer.parseInt(resultSet.getString("product_category_id"))),
                        supplierDaoDataStore.find(Integer.parseInt(resultSet.getString("supplier_id"))));
                products.add(product);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(products);
        return products;
    }

    private int getSupplierId(Supplier supplier){
        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM supplier WHERE supplier_name = ? ;");
            stmt.setString(1, supplier.getName());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            return id;
        }catch (SQLException e){
            System.out.println(e);
        }
        return 0;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        int suppId = getSupplierId(supplier);
        List<Product> productsById= new ArrayList<>();
        String query = "SELECT * FROM product WHERE supplier_id = " + suppId + ";";

        try (Connection connection = getConnection();
             Statement getProductsBySupplier = connection.createStatement();
             ResultSet resultSet = getProductsBySupplier.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDataStore.find(Integer.parseInt(resultSet.getString("product_category_id"))),
                        supplierDaoDataStore.find(Integer.parseInt(resultSet.getString("supplier_id"))));
                productsById.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsById;
    }


    private int getCategoryId(ProductCategory productCategory){
        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product_category WHERE name = ? ;");
            stmt.setString(1, productCategory.getName());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            return id;
        }catch (SQLException e){
            System.out.println(e);
        }
        System.out.println("Nem jó");
        return 10;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        int productCategoryId = getCategoryId(productCategory);
        List<Product> productsById= new ArrayList<>();
        String query = "SELECT * FROM product WHERE product_category_id = " + productCategoryId + ";";
        try (Connection connection = getConnection();
             Statement getProductsBySupplier = connection.createStatement();
             ResultSet resultSet = getProductsBySupplier.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"),
                        resultSet.getString("description"),
                        productCategoryDataStore.find(Integer.parseInt(resultSet.getString("product_category_id"))),
                        supplierDaoDataStore.find(Integer.parseInt(resultSet.getString("supplier_id"))));
                productsById.add(product);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsById;
    }

}
