package com.example.finalproject2.Model;

import com.example.finalproject2.R;

import java.util.ArrayList;
import java.util.Arrays;

public class WeekScheduleItem {
    public ArrayList<DayScheduleItem> getDayScheduleItemsArrayList() {
        return dayScheduleItemsArrayList;
    }

    public void setDayScheduleItemsArrayList(ArrayList<DayScheduleItem> dayScheduleItemsArrayList) {
        this.dayScheduleItemsArrayList = dayScheduleItemsArrayList;
    }

    private ArrayList<DayScheduleItem> dayScheduleItemsArrayList = new ArrayList<>();


    public WeekScheduleItem() {
        for (int i = 0; i < 8; i++) {
            dayScheduleItemsArrayList.add(new DayScheduleItem());
        }
        convertPlantListToSchedule();
    }

    public void convertPlantListToSchedule() {
        for (int i = 1; i < 8; i++) {
            for (Plant p : AppData.user.userPlants) {
                if (p.wateringSchedule.dayOfWeek != null)
                    for (int day : p.wateringSchedule.dayOfWeek) {
                        if (i == day) {
                            dayScheduleItemsArrayList.get(i)
                                    .scheduleItemArrayList.add(new ScheduleItem(p.name, p.getId(p.getName().replaceAll(" ", "").toLowerCase(), R.drawable.class),
                                    p.wateringSchedule.hour, p.wateringSchedule.min));
                        }
                    }
            }
        }

    }

    public class DayScheduleItem {
        public ArrayList<ScheduleItem> getScheduleItemArrayList() {
            return scheduleItemArrayList;
        }

        public void setScheduleItemArrayList(ArrayList<ScheduleItem> scheduleItemArrayList) {
            this.scheduleItemArrayList = scheduleItemArrayList;
        }

        public DayScheduleItem() {
            scheduleItemArrayList = new ArrayList<>();
        }

        private ArrayList<ScheduleItem> scheduleItemArrayList;
    }

    public class ScheduleItem {
        private String name;
        private int imageID, hour, min;

        public ScheduleItem(String name, int imageID, int hour, int min) {
            this.name = name;
            this.imageID = imageID;
            this.hour = hour;
            this.min = min;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImageID() {
            return imageID;
        }

        public void setImageID(int imageID) {
            this.imageID = imageID;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }
}
