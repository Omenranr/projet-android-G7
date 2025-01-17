package com.rdmn.plutus.helperapp.Model;

public class User{
    private String email, name, password, phone, avatarUrl, rates, helpType;

    public User(){

    }

    public User(String email, String name, String password, String phone, String avatarUrl, String rates, String helpType) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rates = rates;
        this.helpType = helpType;
    }

    public String getHelpType() {
        return helpType;
    }

    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
