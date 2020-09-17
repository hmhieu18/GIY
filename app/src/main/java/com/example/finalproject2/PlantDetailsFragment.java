package com.example.finalproject2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlantDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantDetailsFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private ImageView _plantImg;
    private Plant plant;
    private TextView _name;
    private TextView _scientificName;
    private TextView _water;
    private TextView _light;
    private TextView _bloomTime;
    private TextView _tMin;
    private TextView _tMax;

    public PlantDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlantDetailsFragment newInstance(Plant plant) {
        PlantDetailsFragment fragment = new PlantDetailsFragment();
        Bundle args = new Bundle();
        args.putString("Name", plant.getName());
        args.putString("ScientificName", plant.getScientific_name());
        args.putString("Water", plant.getWater());
        args.putString("Light", plant.getLight());
        args.putString("BloomTime", plant.getBloom_time());
        args.putString("TMin", plant.getT_min());
        args.putString("TMax", plant.getT_max());
        args.putInt("LogoID", plant.getId(plant.getName().replaceAll(" ", "").toLowerCase(), R.drawable.class));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plant = new Plant(getArguments().getString("Name"), getArguments().getString("ScientificName"), getArguments().getString("Water"),
                    getArguments().getString("Light"), getArguments().getString("BloomTime"),getArguments().getString("TMin"),getArguments().getString("TMax"));
        }
    }

    private void showInfo() {
        _name.setText(plant.getName());
        _scientificName.setText(plant.getScientific_name());
        _water.setText(plant.getWater());
        _light.setText(plant.getLight());
        _bloomTime.setText(plant.getBloom_time());
        _tMin.setText(plant.getT_min());
        _tMax.setText(plant.getT_max());
        Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(getContext()).getResources(), plant.getId(plant.getName().replaceAll(" ", "").toLowerCase(), R.drawable.class));
        _plantImg.setImageBitmap(bmp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_details, container, false);
        initComponent(view);
        showInfo();
        return view;
    }

    private void initComponent(View view) {
        _name = view.findViewById(R.id.nametxtview);
        _scientificName = view.findViewById(R.id.scientificnametxtview);
        _water = view.findViewById(R.id.watertextview);
        _light = view.findViewById(R.id.lighttextview);
        _bloomTime = view.findViewById(R.id.bloomtimetextview);
        _tMin = view.findViewById(R.id.tmintextview);
        _tMax = view.findViewById(R.id.tmaxtextview);
        _plantImg=view.findViewById(R.id.plantphoto);
    }
}
