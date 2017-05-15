package com.practice.android.cobajson;

import java.util.ArrayList;

/**
 * Created by Belal on 11/9/2015.
 */
public class SuperHero {
    //Data Variables
    private String imageUrl;
    private String name;
    //private int rank;
    //private String realName;
    private String createdBy;
    //private String firstAppearance;
    //private ArrayList<String> powers;

    //Getters and Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
