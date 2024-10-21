package com.example.demo20;

import jakarta.persistence.*;



//create table myappuser(
//        id int primary key,
//        email varchar(255),
//        password varchar(255),
//        username varchar(255),
//        role VARCHAR(255) DEFAULT 'USER'
//        )


@Entity
public class Myappuser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;

    @PrePersist
    public void prePersist() {
        if(role == null)
            role = "USER";
    }
    private String role;

    protected Myappuser(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}