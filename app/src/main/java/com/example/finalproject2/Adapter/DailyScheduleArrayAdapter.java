package com.example.finalproject2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject2.Model.WeekScheduleItem;
import com.example.finalproject2.R;

import java.util.List;

public class DailyScheduleArrayAdapter extends ArrayAdapter<WeekScheduleItem.ScheduleItem> {
    private Context _context;
    private int _layoutID;
    private List<WeekScheduleItem.ScheduleItem> _items;


    public DailyScheduleArrayAdapter(@NonNull Context context, int resource, @NonNull List<WeekScheduleItem.ScheduleItem> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _items = objects;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    public WeekScheduleItem.ScheduleItem getItem(int position) {
        return _items.get(position);
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null, false);
        }

        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView textView = convertView.findViewById(R.id.plantname);
        TextView textViewSub = convertView.findViewById(R.id.description);
        TextView textViewRating = convertView.findViewById(R.id.percentage);
        final WeekScheduleItem.ScheduleItem scheduleItem = _items.get(position);
        if (scheduleItem != null) {
            imageView.setImageResource(R.drawable.ic_wateringicon);
            textView.setText(Integer.valueOf(scheduleItem.getHour()).toString() + ':' + Integer.valueOf(scheduleItem.getMin()).toString());
            textViewSub.setText("Watering " + scheduleItem.getName());
            textViewRating.setText("");
        }
        return convertView;
    }
}