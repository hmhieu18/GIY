package com.example.finalproject2.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Model.Plant;
import com.example.finalproject2.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {
    private Button _finishButton;
    private String _water, _light, _bloom;
    private ArrayList<Plant> resultPlantArrayList = new ArrayList<>();
    private ArrayList<Plant> arrayList = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxArrayWater = new ArrayList<>();
    private int countWaterBox = 0;
    private ArrayList<CheckBox> checkBoxArrayLight = new ArrayList<>();
    private int countLightBox = 0;
    private ArrayList<CheckBox> checkBoxArrayBloom = new ArrayList<>();
    private int countBloomBox = 0;
    private String[] _waterList = new String[] {"Low","Medium","High"};
    private String[] _lightList = new String[] {"Full Sun", "Partial Shade", "Full Shade"};
    private String[] _bloomList = new String[] {"Spring", "Summer", "Fall", "Winter"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
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
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
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
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        initComponent(view);
        loadData();
        return view;
    }

    private void loadData() {
        for (Plant i : AppData._plants) {
            arrayList.add(i);
        }
    }


    private void initComponent(View view) {
        _finishButton = view.findViewById(R.id.buttonFinishQuiz);
        FinishQuizButtonOnClickedListener();
        checkBoxWaterResult(view);
        checkBoxLightResult(view);
        checkBoxBloomResult(view);
    }

    private void FinishQuizButtonOnClickedListener() {
        _finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultPlantArrayList.clear();
                Plant temp;
                for (int i = 0; i < arrayList.size(); i++) {
                    if (_water != null && _light != null && _bloom != null) {
                        if (arrayList.get(i).getWater().equals(_water)
                                && arrayList.get(i).getBloom_time().contains(_bloom)
                                && arrayList.get(i).getLight().contains(_light)) {
                            temp = arrayList.get(i);
                            resultPlantArrayList.add(temp);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please check the checkbox",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                Random rand = new Random(); //instance of random class
                if (resultPlantArrayList.size() > 0) {
                    int upperbound = resultPlantArrayList.size();
                    int int_random = rand.nextInt(upperbound);
                    openFragment(PlantDetailsFragment.newInstance(resultPlantArrayList.get(int_random), int_random, true));
                }
                else
                {
                    int upperbound = AppData._plants.size();
                    int int_random = rand.nextInt(upperbound);
                    openFragment(PlantDetailsFragment.newInstance(AppData._plants.get(int_random), int_random, true));
                }
            }
        });
    }

    private void checkBoxWaterResult(View view) {
        CheckBox _wLow, _wMedium, _wHigh;
        _wLow = view.findViewById(R.id.checkLow);
        _wMedium = view.findViewById(R.id.checkMedium);
        _wHigh = view.findViewById(R.id.checkHigh);
        checkBoxArrayWater.add(_wLow);
        checkBoxArrayWater.add(_wMedium);
        checkBoxArrayWater.add(_wHigh);
        for (CheckBox cb : checkBoxArrayWater) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        countWaterBox++;
                    } else {
                        countWaterBox--;
                    }
                    if (countWaterBox >= 1)
                        for (CheckBox cb1 : checkBoxArrayWater) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArrayWater) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }

        if (_wLow.isChecked() == false && _wHigh.isChecked() == false && _wMedium.isChecked() == false) {
            Random rand = new Random(); //instance of random class
            int Rand = rand.nextInt(3);
            _water = _waterList[Rand];
        }

        _wLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    _water = "Low";
                else _water = null;
            }
        });

        _wMedium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    _water = "Medium";
                else _water = null;
            }
        });

        _wHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    _water = "High";
                else _water = null;
            }
        });

    }

    private void checkBoxBloomResult(View view) {
        CheckBox _bSpring, _bSummer, _bFall, _bWinter;
        _bSpring = view.findViewById(R.id.checkSpring);
        _bSummer = view.findViewById(R.id.checkSummer);
        _bFall = view.findViewById(R.id.checkFall);
        _bWinter = view.findViewById(R.id.checkWinter);

        checkBoxArrayBloom.add(_bSpring);
        checkBoxArrayBloom.add(_bSummer);
        checkBoxArrayBloom.add(_bFall);
        checkBoxArrayBloom.add(_bWinter);
        for (CheckBox cb : checkBoxArrayBloom) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        countBloomBox++;
                    } else {
                        countBloomBox--;
                    }
                    if (countBloomBox >= 1)
                        for (CheckBox cb1 : checkBoxArrayBloom) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArrayBloom) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }

        if(_bSpring.isChecked() == false && _bSummer.isChecked() == false
                && _bFall.isChecked() == false && _bWinter.isChecked() == false)
        {
            Random rand = new Random(); //instance of random class
            int Rand = rand.nextInt(4);
            _bloom = _bloomList[Rand];
        }

        _bSpring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _bloom = "Spring";
                else _bloom = null;
            }
        });

        _bSummer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _bloom = "Summer";
                else _bloom = null;
            }
        });

        _bFall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _bloom = "Fall";
                else _bloom = null;
            }
        });

        _bWinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _bloom = "Winter";
                else _bloom = null;
            }
        });
    }

    private void checkBoxLightResult(View view) {
        CheckBox _light1, _light2, _light3;
        _light1 = view.findViewById(R.id.checkFullSun);
        _light2 = view.findViewById(R.id.checkHalfShade);
        _light3 = view.findViewById(R.id.checkFullShade);

        checkBoxArrayLight.add(_light1);
        checkBoxArrayLight.add(_light2);
        checkBoxArrayLight.add(_light3);
        for (CheckBox cb : checkBoxArrayLight) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        countLightBox++;
                    } else {
                        countLightBox--;
                    }
                    if (countLightBox >= 1)
                        for (CheckBox cb1 : checkBoxArrayLight) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArrayLight) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }

        if (_light1.isChecked() == false && _light2.isChecked() == false && _light3.isChecked() == false) {
            Random rand = new Random(); //instance of random class
            int Rand = rand.nextInt(3);
            _light = _lightList[Rand];
        }

        _light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _light = "Full Sun";
                else _light = null;
            }
        });

        _light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _light = "Partial Shade";
                else _light = null;
            }
        });

        _light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _light = "Full Shade";
                else _light = null;
            }
        });

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
