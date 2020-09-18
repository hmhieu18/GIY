package com.example.finalproject2;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
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
    public EditScheduleFragment() {
        // Required empty public constructor
    }

    public static EditScheduleFragment newInstance(String water) {
        EditScheduleFragment fragment = new EditScheduleFragment();
        Bundle args = new Bundle();
        args.putString("Water", water);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            waterCondition = getArguments().getString("Water");
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
        mon = view.findViewById(R.id.mon);
        tue = view.findViewById(R.id.tue);
        wed = view.findViewById(R.id.wed);
        thu = view.findViewById(R.id.thu);
        fri = view.findViewById(R.id.fri);
        sat = view.findViewById(R.id.sat);
        sun = view.findViewById(R.id.sun);
        finish = view.findViewById(R.id.finish);
//        if (mon.isChecked()) {
//            setalarm(2);
//        } else if (tue.isChecked()) {
//            setalarm(3);
//        } else if (Wednesday.isChecked()) {
//            setalarm(4);
//        } else if (Thursday.isChecked()) {
//            setalarm(5);
//        } else if (Friday.isChecked()) {
//            setalarm(6);
//        } else if (Saturday.isChecked()) {
//            setalarm(7);
//        } else if (Sunday.isChecked()) {
//            setalarm(1);
//        }
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
}
