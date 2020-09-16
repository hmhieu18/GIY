package com.example.finalproject2;

public class Plant {
    private String _name;
    private String _description;
    private int _plantImage;

    public Plant(String name, String description, int logoID) {
        this._name = name;
        this._description = description;
        this._plantImage = logoID;
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }
    public String getDescription() {
        return _description;
    }
    public int getLogoID() {
        return _plantImage;
    }
}
