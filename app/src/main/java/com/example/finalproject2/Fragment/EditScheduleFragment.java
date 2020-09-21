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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject2.Helper.ReminderHelper;
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
    private int countBoxChecked = 0;

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
        setClickListenerForButtonsArrayList();
        timePicker = view.findViewById(R.id.edit_alarm_time_picker);
        if (index != -1) {
            loadOldSchedule();
        }
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

    private void setClickListenerForButtonsArrayList() {
        for (CheckBox cb : checkBoxArrayList) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        countBoxChecked++;
                    } else {
                        countBoxChecked--;
                    }
                    if (countBoxChecked >= timetowater)
                        for (CheckBox cb1 : checkBoxArrayList) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArrayList) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }
    }

    private void loadOldSchedule() {
        timePicker.setHour(AppData.user.userPlants.get(index).wateringSchedule.hour);
        timePicker.setMinute(AppData.user.userPlants.get(index).wateringSchedule.min);
        for (int i = 0; i < checkBoxArrayList.size(); i++) {
            if (AppData.user.userPlants.get(index).wateringSchedule.dayOfWeek != null)
                if (AppData.user.userPlants.get(index).wateringSchedule.dayOfWeek.contains(i + 1)) {
                    checkBoxArrayList.get(i).setChecked(true);
                    countBoxChecked++;
                }
        }
        for (CheckBox cb1 : checkBoxArrayList) {
            if (!cb1.isChecked()) cb1.setEnabled(false);
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
                finishClicked();
            }
        });
    }

    private void finishClicked() {
        int hourFromPicker = timePicker.getHour();
        int minuteFromPicker = timePicker.getMinute();
        if (countBoxChecked == timetowater) {
            Toast.makeText(getContext(), "Setting Notification...", Toast.LENGTH_SHORT).show();
            ArrayList<Integer> alarmDays = getDayArrayList();
            if (index != -1)
                ReminderHelper.deleteEvent(getActivity(), AppData.user.userPlants.get(index).wateringSchedule.eventID);
//            addAlarm(hourFromPicker, minuteFromPicker, alarmDays);
            currentPlant.wateringSchedule = new Plant.Schedule(alarmDays, hourFromPicker, minuteFromPicker);
            currentPlant.wateringSchedule.eventID = ReminderHelper.setReminder(getActivity(), currentPlant);
            if (index == -1)
                AppData.user.userPlants.add(currentPlant);
            else
                AppData.user.userPlants.get(index).wateringSchedule = currentPlant.wateringSchedule;
            saveUserNewPlantToDatabase();
            openFragment(MyGardenFragment.newInstance("", ""));
        } else if (countBoxChecked < timetowater) {
            Toast.makeText(getContext(), "Times to water are less than necessary, you have to check at least " + Integer.valueOf(timetowater).toString() + " time(s)", Toast.LENGTH_SHORT).show();
        }
    }


    private ArrayList<Integer> getDayArrayList() {
        ArrayList<Integer> alarmDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (checkBoxArrayList.get(i).isChecked()) alarmDays.add(i + 1);
        }
        return alarmDays;
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
