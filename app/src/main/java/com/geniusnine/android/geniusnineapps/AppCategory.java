package com.geniusnine.android.geniusnineapps;

/**
 * Created by AndriodDev8 on 20-02-2017.
 */

public class AppCategory {
    @com.google.gson.annotations.SerializedName("category")
    private String category;
    @com.google.gson.annotations.SerializedName("id")
    private  String id;
    public  AppCategory(){

    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
