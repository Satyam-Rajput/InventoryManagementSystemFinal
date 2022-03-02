/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.satyam.inventorymanagementsystem;

//import javax.persistence.Column;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import javax.persistence.Transient;


/**
 *
 * @author Satyam
 */
@Embeddable
public class Medicine {

    public Medicine() {
    }

    public Medicine(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Medicine other = (Medicine) obj;
        return Objects.equals(this.name, other.name);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double calculateTotal() {
        return this.price * this.quantity;
    }

    public double getTotal() {

        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "price", nullable = false)
    private double price;
    @Transient
    private double total;

}
