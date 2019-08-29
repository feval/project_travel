package com.lin.client.entity;

import java.util.Set;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 16:30
 */
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String brief;
    private Set<String> userNames;
    public User() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Set<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(Set<String> userNames) {
        this.userNames = userNames;
    }
}
