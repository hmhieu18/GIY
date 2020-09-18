package com.example.finalproject2.Fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Model.Plant;
import com.example.finalproject2.Model.User;
import com.example.finalproject2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditScheduleFragment extends Fragment {
    private CheckBox mon, tue, wed, thu, fri, sat, sun;
    private Button finish;
    private String waterCondition;
    private Plant currentPlant;
    private Plant.Schedule schedule;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int timetowater;
    private int countCheck;
    private TimePicker timePicker;
    private TextView repeatTextView;

    public EditScheduleFragment() {
        // Required empty public constructor
    }

    public static EditScheduleFragment newInstance(String plantName) {
        EditScheduleFragment fragment = new EditScheduleFragment();
        Bundle args = new Bundle();
        args.putString("plantName", plantName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String plantName = getArguments().getString("plantName");
            for (Plant i : AppData._plants) {
                if (i.name == plantName) currentPlant = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_schedule, container, false);
        initComponent(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initComponent(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mon = view.findViewById(R.id.mon);
        tue = view.findViewById(R.id.tue);
        wed = view.findViewById(R.id.wed);
        thu = view.findViewById(R.id.thu);
        fri = view.findViewById(R.id.fri);
        sat = view.findViewById(R.id.sat);
        sun = view.findViewById(R.id.sun);
        timePicker = view.findViewById(R.id.edit_alarm_time_picker);
        repeatTextView = view.findViewById(R.id.repeat);
        finish = view.findViewById(R.id.finish);

        if (currentPlant.getWater().equals("Medium")) timetowater = 3;
        else if (currentPlant.getWater().equals("High")) timetowater = 5;
        else timetowater = 1;
        repeatTextView.setText(currentPlant.getName() + " need to be watered " + timetowater + " times per week");
        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int hourFromPicker = timePicker.getHour();
                int minuteFromPicker = timePicker.getMinute();
                countDays();
                if (countCheck == timetowater) {
                    Toast.makeText(getContext(), "Setting Notification...", Toast.LENGTH_SHORT).show();
                    ArrayList<Integer> alarmDays = getDayArrayList();
                    addAlarm(hourFromPicker, minuteFromPicker, alarmDays);
                    currentPlant.wateringSchedule = new Plant.Schedule(alarmDays, hourFromPicker, minuteFromPicker);
                    AppData.user.userPlants.add(currentPlant);
                    saveUserNewPlantToDatabase();
                } else if (countCheck < timetowater) {
                    Toast.makeText(getContext(), "Times to water are less than necessary, you have to check more", Toast.LENGTH_SHORT).show();
                } else if (countCheck > timetowater) {
                    Toast.makeText(getContext(), "Times to water are more than necessary, you have to uncheck some days", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void countDays() {
        countCheck = 0;
        if (mon.isChecked()) {
            countCheck++;
        }
        if (tue.isChecked()) {
            countCheck++;
        }
        if (wed.isChecked()) {
            countCheck++;
        }
        if (thu.isChecked()) {
            countCheck++;
        }
        if (fri.isChecked()) {
            countCheck++;
        }
        if (sat.isChecked()) {
            countCheck++;
        }
        if (sun.isChecked()) {
            countCheck++;
        }
    }

    private ArrayList<Integer> getDayArrayList() {
        ArrayList<Integer> alarmDays = new ArrayList<>();
        if (mon.isChecked()) {
            alarmDays.add(2);
        }
        if (tue.isChecked()) {
            alarmDays.add(3);
        }
        if (wed.isChecked()) {
            alarmDays.add(4);
        }
        if (thu.isChecked()) {
            alarmDays.add(5);
        }
        if (fri.isChecked()) {
            alarmDays.add(6);
        }
        if (sat.isChecked()) {
            alarmDays.add(7);
        }
        if (sun.isChecked()) {
            alarmDays.add(1);
        }
        return alarmDays;
    }

    private void addAlarm(int hourFromPicker, int minuteFromPicker, ArrayList<Integer> alarmDays) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Wake up!")
                .putExtra(AlarmClock.EXTRA_HOUR, hourFromPicker)
                .putExtra(AlarmClock.EXTRA_MINUTES, minuteFromPicker);

        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(intent);
    }

    public void showTimePicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Plan your schedule:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void setalarm(int weekno, int hour, int min) {
//
//        cal.set(Calendar.DAY_OF_WEEK, weekno);
//        cal.set(Calendar.HOUR, hour);
//        cal.set(Calendar.MINUTE, min);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveUserNewPlantToDatabase() {
        System.out.println(mAuth.getUid());
        mDatabase.child("users").child(mAuth.getUid()).child("plants_list").setValue(AppData.user.userPlants);
    }
}
