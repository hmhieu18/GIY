package com.example.finalproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.finalproject2.Fragment.MyGardenFragment;
import com.example.finalproject2.Fragment.NewPlantFragment;
import com.example.finalproject2.Fragment.PredictionFragment;
import com.example.finalproject2.Fragment.ScheduleManagerFragment;
import com.example.finalproject2.Fragment.SettingFragment;
import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Model.Plant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AppData appData;
    BottomNavigationView bottomNavigation;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("app_plant_list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //json: AppPlants.json
        //loadPlants();
        //json: UserPlants.json
        //loadPlantsUser();
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppData._plants = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.v("TAG", "" + dataSnapshot1.getKey()); //displays the key for the node
                    Plant temp = dataSnapshot1.getValue(Plant.class);   //gives the value for given keyname
                    System.out.println(temp.getName());
                    AppData._plants.add(temp);
                }
                openFragment(MyGardenFragment.newInstance("", ""));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_garden:
                            openFragment(MyGardenFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_add_plant:
                            openFragment(NewPlantFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_camera:
                            openFragment(PredictionFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_setting:
                            openFragment(SettingFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_schedule:
                            openFragment(ScheduleManagerFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };

}