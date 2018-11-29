package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.util.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map params = initDefaultData(productDataStore, productCategoryDataStore);

        Cookie clientCookies[] = req.getCookies();

        changeFilter(req, productDataStore, productCategoryDataStore, supplierDataStore, params);

        int itemNum = numberOfItemsInCart(clientCookies);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariables(params);
        context.setVariable("numOfItems", itemNum);
        context.setVariable("recipient", "World");
        context.setVariable("supplier", supplierDataStore.getAll());
        engine.process("product/index.html", context, resp.getWriter());
    }

    private void changeFilter(HttpServletRequest req, ProductDao productDataStore, ProductCategoryDao productCategoryDataStore, SupplierDao supplierDataStore, Map params) {
        String itemCategory = req.getParameter("productCategory");
        if (itemCategory != null) {
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(Integer.parseInt(itemCategory))));
        }

        String supplierId = req.getParameter("supplier");
        if (supplierId != null) {
            params.put("products", productDataStore.getBy(supplierDataStore.find(Integer.parseInt(supplierId))));
        }
    }

    private Map initDefaultData(ProductDao productDataStore, ProductCategoryDao productCategoryDataStore) {
        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.find(10));
        params.put("products", productDataStore.getBy(productCategoryDataStore.find(10)));
        return params;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie clientCookies[] = req.getCookies();

        String item = req.getParameter("itemId");

        if (item != null) {
            String itemId = req.getParameter("itemId");
            if (itemInCart(itemId, clientCookies)) {
                for (Cookie cookie : clientCookies) {
                    if (itemId.equals(cookie.getName())) {
                        cookie.setValue(String.valueOf(Integer.parseInt(cookie.getValue()) + 1));
                        cookie.setMaxAge(60 * 60 * 3);
                        resp.addCookie(cookie);
                        break;
                    }
                }
            } else {
                Cookie product = new Cookie(req.getParameter("itemId"), "1");
                product.setMaxAge(60 * 60 * 3);
                resp.addCookie(product);
            }
        }

        int itemNum = numberOfItemsInCart(clientCookies);

        String supplier = req.getParameter("myForm");

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
//        context.setVariables(params);
        context.setVariable("recipient", "World");
        context.setVariable("numOfItems", itemNum);

        context.setVariable("category", productCategoryDataStore.getAll());

        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(10)));
        context.setVariable("categories", productCategoryDataStore.getAll());
        engine.process("product/index.html", context, resp.getWriter());

    }

    boolean itemInCart(String id, Cookie clientCookies[]) {
        boolean itemInCart = false;
        if (clientCookies == null) return itemInCart;
        for (Cookie cookie : clientCookies) {
            if (id.equals(cookie.getName())) {
                itemInCart = true;
                break;
            }
        }
        return itemInCart;
    }

    private int numberOfItemsInCart(Cookie clientCookies[]) {
        int itemNum = 0;
        if (clientCookies == null) return itemNum;
        for (Cookie cookie : clientCookies) {
            if (cookie.getName().length() <= 3) {
                itemNum += Integer.parseInt(cookie.getValue());
            }
        }
        return itemNum;
    }
}