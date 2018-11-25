package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductDao;
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = {"/payment"})
public class Payment extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String paymentChoice = req.getParameter("payment-choice");
        System.out.println(paymentChoice);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("paymentChoice", paymentChoice);
        //context.setVariable("total", ShoppingCart.total);
        engine.process("product/payment.html", context, resp.getWriter());

    }


   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Enumeration personalInfo = req.getParameterNames();
        //TODO: retrieve data if credit card,and if paypal
       while(personalInfo.hasMoreElements())
       {
           Object obj = personalInfo.nextElement();
           String fieldName = (String) obj;
           String fieldValue = req.getParameter(fieldName);
           System.out.println(fieldName + " : " + fieldValue + "<br>");
       }



       // TODO: redirecting where?

       TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
       WebContext context = new WebContext(req, resp, req.getServletContext());
       engine.process("product/payment.html", context, resp.getWriter());


    }


}
