package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
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

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

//        Map params = new HashMap<>();
//        params.put("category", productCategoryDataStore.find(1));
//        params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));

        String add = req.getParameter("add");


        if(add != null){
            Cookie clientCookies[] = req.getCookies();
            String itemId = req.getParameter("item");
            if(itemInCart(itemId , clientCookies)){
                for (Cookie cookie: clientCookies) {
                    if(itemId.equals(cookie.getName())){
                        cookie.setValue(String.valueOf(Integer.parseInt(cookie.getValue())+1));
                        resp.addCookie(cookie);
                        System.out.println(cookie.getValue());
                        break;
                    }
                }
            } else {
                Cookie item = new Cookie(req.getParameter("item"), "1");
                resp.addCookie(item);
            }
            resp.sendRedirect("/");
        }

        Cookie clientCookies[] = req.getCookies();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
//        context.setVariables(params);
        context.setVariable("cookies", clientCookies);
        context.setVariable("recipient", "World");
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

}
