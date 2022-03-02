/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.satyam.inventorymanagementsystem;

import java.util.Base64;
import java.util.Date;
import javax.persistence.*;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Satyam
 */
@Entity
@Table(name = "Users")
public class User {

    @Transient
    public static User user = new User();

    public User() {
    }

    public User(String username, String accessType, String password, Date lastLogin) {
        this.username = username;
        this.accessType = accessType;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String encode(String password) {
        String base64encodedString = "";

        try {
            base64encodedString = Base64.getEncoder().encodeToString(
                    password.getBytes("utf-8"));

        } catch (Exception e) {
            base64encodedString = password;
        }

        return base64encodedString;

    }

    public String decode(String password) {

        try {
            byte[] base64decodedBytes = Base64.getDecoder().decode(password);

            return new String(base64decodedBytes, "utf-8");

        } catch (Exception e) {
            return null;
        }

    }

    public String getPassword() {
        return this.password;

    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Id
    @Column(name = "username", length = 100)
    private String username;
    @Column(name = "access_type", nullable = false)
    private String accessType;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "last_login", nullable = true)
    private Date lastLogin;
}
