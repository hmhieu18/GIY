package com.example.finalproject2.Fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Model.Plant;
import com.example.finalproject2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPlantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPlantFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button _quizButton;
    private Spinner spinner;
    private ImageView imageView;
    private Button addPlantBtn, shopBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Plant pickedPlant;

    public NewPlantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPlantFragment newInstance(String param1, String param2) {
        NewPlantFragment fragment = new NewPlantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_plant, container, false);
        initComponent(view);
        loadData();
        return view;
    }

    private void initComponent(View view) {
        _quizButton=view.findViewById(R.id.buttonQuiz);
        quizButtonOnclickedListener();
        spinner = view.findViewById(R.id.namelist);
        imageView = view.findViewById(R.id.plantphoto_in_add);
        addPlantBtn = view.findViewById(R.id.add);
        addPlantBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFragment(EditScheduleFragment.newInstance(pickedPlant.getName(), -1));
            }
        });
        shopBtn=view.findViewById(R.id.shop);
        shopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchStore();
            }
        });
    }

    private void loadData() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Plant i : AppData._plants) {
            arrayList.add(i.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String tutorialsName = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
                pickedPlant = AppData._plants.get(position);
                Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(getContext()).getResources(),
                        pickedPlant.getId(pickedPlant.getName()
                                        .replaceAll(" ", "")
                                        .toLowerCase(), R.drawable.class));
                imageView.setImageBitmap(bmp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void quizButtonOnclickedListener()
    {
        _quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(SubQuizFragment.newInstance("",""));
            }
        });
    }
    private void searchStore() {
        Intent myIntent = new Intent(getActivity(), SearchFragment.class);
        Objects.requireNonNull(getActivity()).startActivity(myIntent);
    }
}
