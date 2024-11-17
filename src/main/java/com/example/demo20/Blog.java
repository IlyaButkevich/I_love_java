package com.example.demo20;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Blog {

    private Long id;
    private String user;
    private String name;
    private String text;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    protected Blog(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public  void setId(Long id){this.id = id;}
    public String getUser(){return user;}
    public  void setUser(String user){this.user = user;}
    public String getName(){return name;}
    public  void setName(String name){this.name = name;}
    public String getText(){return text;}
    public  void setText(String text){this.text = text;}
    public Date getDate(){return date;}
    public  void setDate(Date date){this.date = date;}


}
