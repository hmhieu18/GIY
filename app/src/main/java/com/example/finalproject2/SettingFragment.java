package com.example.finalproject2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private Uri filePath;
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 71;
    private Bitmap bitmap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
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
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_prediction, container, false);
        initComponent(view);
        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initComponent(View view) {
//        _conceptArrayList = new ArrayList<>();
//        _predictionListView = findViewById(R.id.predictionresults);
//        _predictionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent myIntent = new Intent(PredictionFragment.this, searchResult.class);
//                myIntent.putExtra("query", String.valueOf(_conceptArrayList.get(position).getName()));
//                PredictionFragment.this.startActivity(myIntent);
//            }
//        });
//        _predictionArrayAdapter = new PredictionArrayAdapter(PredictionFragment.this, R.layout.place_item, _conceptArrayList);
//        _predictionListView.setAdapter(_predictionArrayAdapter);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnChoose = (Button)

                view.findViewById(R.id.choose);

        btnUpload = (Button)

                view.findViewById(R.id.upload);

        imageView = (ImageView)

                view.findViewById(R.id.imageView);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new makePrediction().execute();
            }
        });
    }

    class makePrediction extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(Void... params) {
//            try {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                byte[] byteArray = byteArrayOutputStream.toByteArray();
//                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//                String myURL = "https://api.plant.id/v2/identify";
//                URL obj = null;
//                obj = new URL(myURL);
//
//                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//                con.setRequestMethod("POST");
//                con.setRequestProperty("Content-Type", "application/json");
//                con.setRequestProperty("Api-key", "r4wDGy7ABKJK1ALr1gPdLe0PKADvwnB2VVPvPuwnl4gap4FYLT");
//                con.setDoOutput(true);
//
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
////                String json = "{\"images\": " +
////                        encoded +
////                        ", \"modifiers\": [\"similar_images\"], \"plant_details\": [\"common_names\", \"url\", \"wiki_description\", \"taxonomy\"]}";
//                JSONObject json = new JSONObject();
//                json.put("images", encoded);
//                wr.writeBytes(json.toString());
//                wr.flush();
//                wr.close();
//
//                int responseCode = con.getResponseCode();
//                Log.d("Response Code : ", Integer.valueOf(responseCode).toString());
//                BufferedReader iny = new BufferedReader(
//                        new InputStreamReader(con.getInputStream()));
//                String output;
//                StringBuffer response = new StringBuffer();
//
//                while ((output = iny.readLine()) != null) {
//                    response.append(output);
//                }
//                iny.close();
//                //printing result from response
//                Log.d("response", response.toString());
////                MainActivity.mProfile = new Gson().fromJson(response.toString(), mProfile.class);
////                Log.d("token", MainActivity.mProfile.getSessionToken());
////                if (MainActivity.mProfile.get_id() != null)
////                    isvalid = true;
////                else isvalid = false;
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
            try {
                String encoded = getEncoded64ImageStringFromBitmap(bitmap);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("https://api.plant.id/v2/identify");
                String data = "{'images':"+encoded+"}";
                System.out.println(data.substring(data.length()-20));
                StringEntity se = new StringEntity(data);
                se.setContentType("application/json");
                post.setEntity(se);
                post.setHeader("Api-key", "r4wDGy7ABKJK1ALr1gPdLe0PKADvwnB2VVPvPuwnl4gap4FYLT");
                HttpResponse response = client.execute(post);
                Log.d("response", response.toString());

                String result = EntityUtils.toString(response.getEntity());
                Log.d("res", result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();

        // Get the Base64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
//        return imgString;
        return "['" + imgString + "']";
    }
}
