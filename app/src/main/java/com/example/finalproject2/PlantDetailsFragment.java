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
    private TextView _nickname;
    private TextView _tempe;
    private TextView _lightCondition;

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
        args.putString("Description", plant.getName());
        args.putInt("LogoID", plant.getLogoID());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plant = new Plant(getArguments().getString("Name"), getArguments().getString("Description"), getArguments().getInt("LogoID"));
        }
    }

    private void showInfo() {
        _name.setText(plant.getName());
        _nickname.setText(plant.getDescription());
        Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(getContext()).getResources(), plant.getLogoID());
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
        _lightCondition = view.findViewById(R.id.lighttextview);
        _nickname = view.findViewById(R.id.nicknametxtview);
        _tempe = view.findViewById(R.id.tempetextview);
        _plantImg=view.findViewById(R.id.plantphoto);
    }
}
