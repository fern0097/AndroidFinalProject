package com.example.finalproject.ticketMaster;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 *  Represents an TicketMasterEvent retrieved from TicketMaster API
 *  @author Rodrigo Tavares
 */
public class TicketMasterEvent {

    public TicketMasterEvent(String id, String name, Date startingDate, String currency, double lowestPrice, double highestPrice, URL url, URL imageUrl) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.currency = currency;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    private String  id;
    private String  name;
    private Date    startingDate;
    private String  currency;
    private double  lowestPrice;
    private double  highestPrice;
    private URL     url;
    private URL     imageUrl;

    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public Date getStartingDate() {
        return startingDate;
    }
    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getLowestPrice() {
        return lowestPrice;
    }
    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }
    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getImageUrl() { return imageUrl; }
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

}