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
public class SubQuizFragment extends Fragment {
    private Button _nextButton;

    private ArrayList<CheckBox> checkBoxArray1 = new ArrayList<>();
    private int count1 = 0;
    private ArrayList<CheckBox> checkBoxArray2 = new ArrayList<>();
    private int count2 = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubQuizFragment() {
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
    public static SubQuizFragment newInstance(String param1, String param2) {
        SubQuizFragment fragment = new SubQuizFragment();
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
        View view = inflater.inflate(R.layout.fragment_subquiz, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        _nextButton = view.findViewById(R.id.buttonNextQuiz);
        NextQuizButtonOnClickedListener();
        checkBoxResult1(view);
        checkBoxResult2(view);
    }

    private void NextQuizButtonOnClickedListener() {
        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(QuizFragment.newInstance("",""));
            }
        });
    }

    private void checkBoxResult1(View view) {
        CheckBox temp;
        temp = view.findViewById(R.id.checkAgree);
        checkBoxArray1.add(temp);
        temp = view.findViewById(R.id.checkDisagree);
        checkBoxArray1.add(temp);
        temp = view.findViewById(R.id.checkDunno);
        checkBoxArray1.add(temp);

        for (CheckBox cb : checkBoxArray1) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        count1++;
                    } else {
                        count1--;
                    }
                    if (count1 >= 1)
                        for (CheckBox cb1 : checkBoxArray1) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArray1) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }

    }

    private void checkBoxResult2(View view) {
        CheckBox temp;
        temp = view.findViewById(R.id.checkplace1);
        checkBoxArray2.add(temp);
        temp = view.findViewById(R.id.checkplace2);
        checkBoxArray2.add(temp);
        temp = view.findViewById(R.id.checkplace3);
        checkBoxArray2.add(temp);
        temp = view.findViewById(R.id.checkplace4);
        checkBoxArray2.add(temp);

        for (CheckBox cb : checkBoxArray2) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CompoundButton) view).isChecked()) {
                        count2++;
                    } else {
                        count2--;
                    }
                    if (count2 >= 1)
                        for (CheckBox cb1 : checkBoxArray2) {
                            if (!cb1.isChecked()) cb1.setEnabled(false);
                        }
                    else
                        for (CheckBox cb1 : checkBoxArray2) {
                            if (!cb1.isChecked()) cb1.setEnabled(true);
                        }
                }
            });
        }

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
