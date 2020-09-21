package com.example.finalproject2.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.finalproject2.Helper.ReminderHelper;
import com.example.finalproject2.Model.AppData;
import com.example.finalproject2.Adapter.GridViewAdapter;
import com.example.finalproject2.Model.Plant;
import com.example.finalproject2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGardenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGardenFragment extends Fragment {
    private GridView _gridView;
    public GridViewAdapter _adapter;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("users");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MyGardenFragment() {
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
    public static MyGardenFragment newInstance(String param1, String param2) {
        MyGardenFragment fragment = new MyGardenFragment();
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
//        loadPlants();
    }

    private void initComponent(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        _gridView = view.findViewById(R.id.gridview_plants);

        loadUserPlants();
    }

    private void refreshGridView() {
        _adapter = new GridViewAdapter(mContext, R.layout.gridview_plant_item, AppData.user.userPlants);
        _gridView.setAdapter(_adapter);
        _gridView.setOnItemClickListener(_gridViewItemOnClick);
        _gridView.setOnItemLongClickListener(_gridViewItemOnLongClick);
    }

    private void loadUserPlants() {
        mDatabase.child("users").child(mAuth.getUid()).child("plants_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppData.user.userPlants = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.v("@@@", "" + dataSnapshot1.getKey()); //displays the key for the node
                    Plant temp = dataSnapshot1.getValue(Plant.class);   //gives the value for given keyname
                    System.out.println(temp.getName());
                    AppData.user.userPlants.add(temp);
                    refreshGridView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garden, container, false);
        initComponent(view);
        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private GridView.OnItemClickListener _gridViewItemOnClick = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            openFragment(PlantDetailsFragment.newInstance(AppData.user.userPlants.get(position), position, false));
        }
    };
    private GridView.OnItemLongClickListener _gridViewItemOnLongClick = new GridView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            optionPlace(mContext, i);
            return true;
        }
    };

    public String loadJSONFromAsset() {
        InputStream is = getResources().openRawResource(R.raw.list);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    private void optionPlace(final Context context, final int i) {
        final CharSequence[] options = {"Remove this plant", "Where to buy", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Option");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Remove this plant")) {
                    getWarningDialog(i).show();
                } else if (options[item].equals("Where to buy")) {
                    searchStore();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void searchStore() {
        Intent myIntent = new Intent(getActivity(), SearchFragment.class);
        Objects.requireNonNull(getActivity()).startActivity(myIntent);
    }

    private void removePlantByID(int item) {
        ReminderHelper.deleteEvent(Objects.requireNonNull(getActivity()), AppData.user.userPlants.get(item).wateringSchedule.eventID);
        AppData.user.userPlants.remove(item);
        mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).child("plants_list").setValue(AppData.user.userPlants);
        _gridView.invalidateViews();
    }

    private AlertDialog.Builder getWarningDialog(final int item) {
        return new AlertDialog.Builder(mContext)
                .setTitle("Delete plant")
                .setMessage("Are you sure to delete this plant?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removePlantByID(item);
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }
}
