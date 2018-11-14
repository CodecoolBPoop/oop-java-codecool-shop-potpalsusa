package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.find(1));
        params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));

        Cookie clientCookies[] = req.getCookies();

        int itemNum = numberOfItemsInCart(clientCookies);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariables(params);
        context.setVariable("numOfItems", itemNum);
        context.setVariable("recipient", "World");
        //context.setVariable("category", productCategoryDataStore.find(1));
        //context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        Cookie clientCookies[] = req.getCookies();

        String item = req.getParameter("itemId");

        if(item != null){
            String itemId = req.getParameter("itemId");
            if(itemInCart(itemId , clientCookies)){
                for (Cookie cookie: clientCookies) {
                    if(itemId.equals(cookie.getName())){
                        cookie.setValue(String.valueOf(Integer.parseInt(cookie.getValue())+1));
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

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
//        context.setVariables(params);
        context.setVariable("recipient", "World");
        context.setVariable("numOfItems", itemNum);
        context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        engine.process("product/index.html", context, resp.getWriter());

    }

    private boolean itemInCart(String id, Cookie clientCookies[]){
        boolean itemInCart = false;
        for (Cookie cookie: clientCookies) {
            if(id.equals(cookie.getName())){
                itemInCart = true;
                break;
            }
        }
        return  itemInCart;
    }

    private int numberOfItemsInCart(Cookie clientCookies[]){
        int itemNum = 0;
        for(Cookie cookie : clientCookies){
            if(cookie.getName().length() <= 3){
                itemNum += Integer.parseInt(cookie.getValue());
            }
        }
        return itemNum;
    }

}
