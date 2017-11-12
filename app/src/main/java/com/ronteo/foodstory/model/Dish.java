package com.ronteo.foodstory.model;

/**
 * Created by ronteo on 12/11/17.
 */

public class Dish {

    private long id;
    private String name;
    private String price;
    private String description;
    private String image;
    private boolean signature;

    private Hawker hawker;

    public Dish() {

    }

    public Dish(String name, String price, String description, String image, boolean signature) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.signature = signature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    public Hawker getHawker() {
        return hawker;
    }

    public void setHawker(Hawker hawker) {
        this.hawker = hawker;
    }

}
