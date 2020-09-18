package com.example.finalproject2.Adapter;

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

import com.example.finalproject2.Model.PredictingResult;
import com.example.finalproject2.R;

import java.util.List;

public class PredictionArrayAdapter extends ArrayAdapter<PredictingResult.Suggestion> {
    private Context _context;
    private int _layoutID;
    private List<PredictingResult.Suggestion> _items;


    public PredictionArrayAdapter(@NonNull Context context, int resource, @NonNull List<PredictingResult.Suggestion> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _items = objects;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    public PredictingResult.Suggestion getItem(int position) {
        return _items.get(position);
    }

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
        final PredictingResult.Suggestion suggestion = _items.get(position);
        if (suggestion != null) {
            Bitmap bmp = BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_leaf);
            imageView.setImageBitmap(bmp);
            if (suggestion.getPlant_details().common_names != null)
                textView.setText(suggestion.getPlant_details().common_names.get(0));
            else textView.setText(suggestion.getPlant_name());
            if (suggestion.getPlant_details().getWiki_description().value.length() < 500)
                textViewSub.setText(suggestion.getPlant_details().getWiki_description().value);
            else textViewSub.setText(suggestion.getPlant_details().getWiki_description().value.substring(0, 499));
            String result = String.format("%.2f", suggestion.getProbability() * 100);
            textViewRating.setText(result + '%');
        }
        return convertView;
    }
}