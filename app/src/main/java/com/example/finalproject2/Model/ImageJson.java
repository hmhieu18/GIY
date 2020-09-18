package com.example.finalproject2.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageJson {
    public ImageJson(String images) {
        this.images = new ArrayList<>();
        this.images.add(images);
        modifiers = new ArrayList<>();
        modifiers.add("similar_images");
        plant_details = new ArrayList<>();
        plant_details.addAll(Arrays.asList("common_names", "url", "wiki_description", "taxonomy"));
    }

    private ArrayList<String> modifiers;
    private ArrayList<String> plant_details;
    private ArrayList<String> images;
}
