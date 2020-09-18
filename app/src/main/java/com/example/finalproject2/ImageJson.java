package com.example.finalproject2;

import android.widget.ImageView;

import java.util.ArrayList;

public class ImageJson {
    public ImageJson(String images) {
        Images = new ArrayList<>();
        Images.add(images);
    }

    private ArrayList<String> Images;
}
