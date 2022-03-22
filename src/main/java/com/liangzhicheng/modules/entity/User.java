package com.liangzhicheng.modules.entity;

public class User {

    private String username;
    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public User() {
        super();
    }

    public User(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

}
