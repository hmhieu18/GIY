package com.example.finalproject2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GridViewArrayAdapter extends ArrayAdapter<Plant> {
    private Context _context;
    private int _layoutID;
    private ArrayList<Plant> _plants;

    public GridViewArrayAdapter(@NonNull Context context, int resource,
                                @NonNull List<Plant> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _plants = (ArrayList<Plant>) objects;
    }

    @Override
    public int getCount() {
        return _plants.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img_logo);
        TextView textView = convertView.findViewById(R.id.txt_name);

        Plant plant = _plants.get(position);
        Bitmap bmp = BitmapFactory.decodeResource(_context.getResources(), plant.getLogoID());
        imageView.setImageBitmap(circleFit(bmp));
        textView.setText(plant.getName());
        return convertView;
    }
    public Bitmap circleFit(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle((float) (bitmap.getWidth() /2), (float) (bitmap.getHeight() / 2),
                (float) (bitmap.getWidth() / 2.5), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
