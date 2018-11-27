package com.codecool.shop.controller;

public class Order {
    protected static String firstName;
    protected static String lastName;
    protected static String email;
    protected static int phone;
    protected static String billingCountry;
    protected static String billingCity;
    protected static int billingZipCode;
    protected static String billingAddress;
    protected static String shippingCountry;
    protected static String shippingCity;
    protected static int shippingZipCode;
    protected static String shippingAddress;

    protected Order() {}

    protected String getFirstName() {
        return firstName;
    }

    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected int getPhone() {
        return phone;
    }

    protected void setPhone(int phone) {
        this.phone = phone;
    }

    protected String getBillingCountry() {
        return billingCountry;
    }

    protected void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    protected String getBillingCity() {
        return billingCity;
    }

    protected void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    protected int getBillingZipCode() {
        return billingZipCode;
    }

    protected void setBillingZipCode(int billingZipCode) {
        this.billingZipCode = billingZipCode;
    }

    protected String getBillingAddress() {
        return billingAddress;
    }

    protected void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    protected String getShippingCountry() {
        return shippingCountry;
    }

    protected void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    protected String getShippingCity() {
        return shippingCity;
    }

    protected void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    protected int getShippingZipCode() {
        return shippingZipCode;
    }

    protected void setShippingZipCode(int shippingZipCode) {
        this.shippingZipCode = shippingZipCode;
    }

    protected String getShippingAddress() {
        return shippingAddress;
    }

    protected void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
