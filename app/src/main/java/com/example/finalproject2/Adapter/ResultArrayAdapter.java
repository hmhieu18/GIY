package com.example.finalproject2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject2.Model.Results;
import com.example.finalproject2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultArrayAdapter extends ArrayAdapter<Results> {
    private Context _context;
    private int _layoutID;
    private List<Results> _items;


    public ResultArrayAdapter(@NonNull Context context, int resource, @NonNull List<Results> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _items = objects;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    public Results getItem(int position) {
        return _items.get(position);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null, false);
        }

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView place_name = convertView.findViewById(R.id.plantname);
        TextView address = convertView.findViewById(R.id.description);
        TextView rating = convertView.findViewById(R.id.percentage);
        final Results result = _items.get(position);
        icon.setImageResource(R.drawable.ic_garden);
        icon.setImageTintList(_context.getResources().getColorStateList(R.color.colorPrimary));
        place_name.setText(result.getName());
        address.setText(result.getFormatted_address());
        rating.setText(result.getRating());
        return convertView;
    }
}
