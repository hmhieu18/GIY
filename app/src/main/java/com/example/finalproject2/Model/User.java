package com.example.finalproject2.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public ArrayList<Plant> userPlants=new ArrayList<>();
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
// [END blog_user_class]
