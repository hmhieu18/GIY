package com.example.finalproject2.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class Plant {
    public String name;
    public String scientific_name;
    public String water;
    public String light;
    public String bloom_time;
    public int t_min;
    public int t_max;
    public Schedule wateringSchedule;
    public Calendar calendar;
//    public int _plantImage;

    public Plant(String _name, String _scientificName, String _water, String _light, String _bloomTime, int _tMin, int _tMax) {
        this.name = _name;
        this.scientific_name = _scientificName;
        this.water = _water;
        this.light = _light;
        this.bloom_time = _bloomTime;
        this.t_min = _tMin;
        this.t_max = _tMax;
//        this._plantImage = _plantImage;
    }

    public Plant() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getBloom_time() {
        return bloom_time;
    }

    public void setBloom_time(String bloom_time) {
        this.bloom_time = bloom_time;
    }

    public int getT_min() {
        return t_min;
    }

    public void setT_min(int t_min) {
        this.t_min = t_min;
    }

    public int getT_max() {
        return t_max;
    }

    public void setT_max(int t_max) {
        this.t_max = t_max;
    }

    //
//    public int get_plantImage() {
//        return _plantImage;
//    }
//
//    public void set_plantImage(int _plantImage) {
//        this._plantImage = _plantImage;
//    }
    public int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public static class Schedule {
        public ArrayList<Integer> dayOfWeek;
        public int hour, min;

        public Schedule(ArrayList<Integer> dayOfWeek, int hour, int min) {
            this.dayOfWeek = dayOfWeek;
            this.hour = hour;
            this.min = min;
        }

        public Schedule() {
        }
    }
}
