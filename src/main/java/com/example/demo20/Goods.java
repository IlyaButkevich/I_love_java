package com.example.demo20;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Goods {
    private Long id;
    private String goodname;
    private String contents;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateofshipment;
    private String shipmentcity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateofarrival;

    private String arrivalcity;

    protected Goods(){}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public  void setId(Long id){this.id = id;}

    public String getGoodname(){return goodname;}
    public  void setGoodname(String goodname){this.goodname = goodname;}
    public String getContents(){return contents;}
    public  void setContents(String contents){this.contents = contents;}
    public String getShipmentcity(){return shipmentcity;}
    public  void setShipmentcity(String shipmentcity){this.shipmentcity = shipmentcity;}
    public String getArrivalcity(){return arrivalcity;}
    public  void setArrivalcity(String arrivalcity){this.arrivalcity = arrivalcity;}
    public Date getDateofshipment(){return dateofshipment;}
    public  void setDateofshipment(Date dateofshipment){this.dateofshipment = dateofshipment;}
    public Date getDateofarrival(){return dateofarrival;}
    public  void setDateofarrival(Date dateofarrival){this.dateofarrival = dateofarrival;}
}
