package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Product, Integer> shoppingCart = new HashMap<>();

        ProductDao productDataStore = ProductDaoMem.getInstance();
        Cookie clientCookies[] = req.getCookies();

        double price = 0;

        for (Cookie cookie: clientCookies) {
            if (cookie.getName().length() <= 3) {
                shoppingCart.put(productDataStore.find(Integer.parseInt(cookie.getName())), Integer.parseInt(cookie.getValue()));
                price += productDataStore.find(Integer.parseInt(cookie.getName())).getDefaultPrice() * Integer.parseInt(cookie.getValue());
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("cookies", clientCookies);
        context.setVariable("recipient", "World");
        context.setVariable("cart", shoppingCart);
        context.setVariable("total", (double)Math.round(price * 100d) / 100d);
        engine.process("product/shopping-cart.html", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<Product, Integer> shoppingCart = new HashMap<>();

        ProductDao productDataStore = ProductDaoMem.getInstance();
        Cookie clientCookies[] = req.getCookies();

        String itemId = req.getParameter("itemId");

        if(itemId != null){
            String itemQuantity = req.getParameter("quantity");
            for (Cookie cookie: clientCookies) {
                if (cookie.getName().length() <= 3) {
                    if(cookie.getName().equals(itemId)){
                        cookie.setValue(itemQuantity);
                        cookie.setMaxAge(60 * 60 * 3);
                        resp.addCookie(cookie);
                        break;
                    }
                }
            }
        }

        double price = 0;

        for (Cookie cookie: clientCookies) {
            if (cookie.getName().length() <= 3) {
                shoppingCart.put(productDataStore.find(Integer.parseInt(cookie.getName())), Integer.parseInt(cookie.getValue()));
                price += productDataStore.find(Integer.parseInt(cookie.getName())).getDefaultPrice() * Integer.parseInt(cookie.getValue());
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("cookies", clientCookies);
        context.setVariable("recipient", "World");
        context.setVariable("cart", shoppingCart);
        context.setVariable("total", (double)Math.round(price * 100d) / 100d);
        engine.process("product/shopping-cart.html", context, resp.getWriter());

    }

}
