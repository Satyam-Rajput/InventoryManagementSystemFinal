/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.satyam.inventorymanagementsystem;

import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

/**
 *
 * @author Satyam
 */
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "total", nullable = false)
    private double total;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date", nullable = false)
    private Date date;
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "medicines", joinColumns = @JoinColumn(name = "patient_id"))
    private List<Medicine> list = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient(String name, Date date, List<Medicine> list) {
        this.name = name;
        this.date = date;
        this.list = list;
    }

    public Patient() {
    }

    public double calculateTotal() {

        double tempTotal = 0;
        for (Medicine m : list) {
            tempTotal += m.calculateTotal();
        }
        return tempTotal;
    }

    public double getTotal() {

        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Medicine> getList() {
        return list;
    }

    public void setList(List<Medicine> list) {
        this.list = list;
    }

}
