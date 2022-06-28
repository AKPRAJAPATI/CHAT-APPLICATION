package com.example.chatapplication.Model;

public class Model {
    private String image_url;
    private String name;
    private String  email;
    private String password;
    private String userId;

    private String  status;

    public Model() {
    }

    public Model(String image_url, String name, String email, String password, String userId) {
        this.image_url = image_url;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public Model(String image_url, String name, String email, String password, String userId, String status) {
        this.image_url = image_url;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
