package com.hyunwoong.pestsaver.core.model;

import java.net.URL;

/**
 * @author : Hyunwoong
 * @when : 2019-11-27 오후 4:00
 * @homepage : https://github.com/gusdnd852
 */
public class User {
    private String id;
    private String pw;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
