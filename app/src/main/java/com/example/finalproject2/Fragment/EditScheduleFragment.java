package com.example.finalproject2.Fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
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
import com.example.finalproject2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditScheduleFragment extends Fragment {
    private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    private Button finish;
    private Plant currentPlant = new Plant();
    private int index = -1;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int timetowater;
    private TimePicker timePicker;
    private TextView repeatTextView;

    public EditScheduleFragment() {
        // Required empty public constructor
    }

    public static EditScheduleFragment newInstance(String plantName, int _index) {
        EditScheduleFragment fragment = new EditScheduleFragment();
        Bundle args = new Bundle();
        args.putString("plantName", plantName);
        args.putInt("index", _index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPlant.name = getArguments().getString("plantName");
            index = getArguments().getInt("index");
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

    private void initComponent(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        CheckBox temp;
        temp = view.findViewById(R.id.sun);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.mon);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.tue);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.wed);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.thu);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.fri);
        checkBoxArrayList.add(temp);
        temp = view.findViewById(R.id.sat);
        checkBoxArrayList.add(temp);

        timePicker = view.findViewById(R.id.edit_alarm_time_picker);
        repeatTextView = view.findViewById(R.id.repeat);
        finish = view.findViewById(R.id.finish);
        for (Plant i : AppData._plants) {
            if (i.name.equals(currentPlant.getName())) {
                currentPlant = i;
                waterConditionHandling();
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void waterConditionHandling() {
        if (currentPlant.getWater().equals("Medium")) timetowater = 3;
        else if (currentPlant.getWater().equals("High")) timetowater = 5;
        else timetowater = 1;
        Log.d("repeat", currentPlant.getName() + " need to be watered " + timetowater + " times per week");
        repeatTextView.setText(currentPlant.getName() + " need to be watered " + timetowater + " times per week");
        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int hourFromPicker = timePicker.getHour();
                int minuteFromPicker = timePicker.getMinute();
                int countCheck = countDays();
                if (countCheck == timetowater) {
                    Toast.makeText(getContext(), "Setting Notification...", Toast.LENGTH_SHORT).show();
                    ArrayList<Integer> alarmDays = getDayArrayList();
                    addAlarm(hourFromPicker, minuteFromPicker, alarmDays);
                    currentPlant.wateringSchedule = new Plant.Schedule(alarmDays, hourFromPicker, minuteFromPicker);
                    if (index == -1)
                        AppData.user.userPlants.add(currentPlant);
                    else
                        AppData.user.userPlants.get(index).wateringSchedule = currentPlant.wateringSchedule;
                    saveUserNewPlantToDatabase();
                } else if (countCheck < timetowater) {
                    Toast.makeText(getContext(), "Times to water are less than necessary, you have to check more", Toast.LENGTH_SHORT).show();
                } else if (countCheck > timetowater) {
                    Toast.makeText(getContext(), "Times to water are more than necessary, you have to uncheck some days", Toast.LENGTH_SHORT).show();
                }
                openFragment(MyGardenFragment.newInstance("", ""));
            }
        });
    }

    private int countDays() {
        int countCheck = 0;
        for (CheckBox cb : checkBoxArrayList) {
            if (cb.isChecked()) countCheck++;
        }
        return countCheck;
    }

    private ArrayList<Integer> getDayArrayList() {
        ArrayList<Integer> alarmDays = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (checkBoxArrayList.get(i).isChecked()) alarmDays.add(i + 1);
        }
        return alarmDays;
    }

    private void addAlarm(int hourFromPicker, int minuteFromPicker, ArrayList<Integer> alarmDays) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Your " + currentPlant.getName() + " gonna be drained. It's time to water it")
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
