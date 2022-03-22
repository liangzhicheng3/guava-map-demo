package com.liangzhicheng.modules.entity;

public class Dept {

    private String name;
    private Integer num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Dept() {
        super();
    }

    public Dept(String name, Integer num) {
        this.name = name;
        this.num = num;
    }

}
