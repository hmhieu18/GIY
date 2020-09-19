package com.example.finalproject2.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.finalproject2.Adapter.ResultArrayAdapter;
import com.example.finalproject2.Helper.SearchHandling;
import com.example.finalproject2.Model.Results;
import com.example.finalproject2.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String query;
    private Location lastKnownLocation = new Location("");
    private ListView _listViewResult;
    private ArrayList<Results> _resultsList;
    public ResultArrayAdapter _resultArrayAdapter;
    private ArrayList<Marker> markerArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                query = null;
            } else query = extras.getString("query");
        } else {
            query = (String) savedInstanceState.getSerializable("query");
        }
        assert query != null;
        Log.d("query", query);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLastKnownLocation();
        initComponent();
        query = "cây kiểng";
    }

    private void initComponent() {
        _resultsList = new ArrayList<>();
        _listViewResult = findViewById(R.id.listViewResults);
        _listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveCameraToMarker(position);
            }
        });
        _listViewResult.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                optionPlace(SearchFragment.this, i);
                return true;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    class retrieveData extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String request = parseURL();
                SearchHandling task = new SearchHandling(request);
                _resultsList.addAll(task.search());
                Log.d("res", Integer.valueOf(_resultsList.size()).toString());
                return null;
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (_resultsList.size() > 0) {
                _resultArrayAdapter = new ResultArrayAdapter(SearchFragment.this, R.layout.prediction_item, _resultsList);
                _listViewResult.setAdapter(_resultArrayAdapter);
                Log.d("res", Integer.valueOf(_resultsList.size()).toString());
                Log.d("res", Integer.valueOf(_resultArrayAdapter.getCount()).toString());
                displayMarkers();
            }
        }
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void displayMarkers() {

        Bitmap bmp = getBitmap(R.drawable.ic_flowermarker);
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 10, bmp.getHeight() / 10, false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp);
        for (Results i : _resultsList) {
            Marker temp = mMap.addMarker(new MarkerOptions()
                    .position(i.getGeometry().getLocation().getLatLng())
                    .title(i.getName())
                    .icon(bitmapDescriptor)
            );
            markerArrayList.add(temp);
        }
        moveCameraToMarker(0);
    }

    private void moveCameraToMarker(int i) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(_resultsList.get(i).getGeometry().getLocation().getLatLng())     // Sets the center of the map to Mountain View
                .zoom(15)                           // Sets the zoom
                .bearing(90)                        // Sets the orientation of the camera to east
                .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lastKnownLocation = location;
                            Log.d("location", Double.valueOf(lastKnownLocation.getLatitude()).toString());
                            if (!query.equals("")) new retrieveData().execute();
                        }
                    }
                });
    }

    private String parseURL() throws MalformedURLException, UnsupportedEncodingException {
        StringBuilder _query = new StringBuilder();
        _query.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query=");
        _query.append(URLEncoder.encode(query, "UTF-8"));
        _query.append("&location=");
        _query.append(Double.valueOf(lastKnownLocation.getLatitude()).toString() + ',' + Double.valueOf(lastKnownLocation.getLongitude()).toString() +
                "&radius=2000" +
                "&language=vi" +
                "&key=" +
                "AIzaSyDk2Z5H8EiJNIsHWrb7-fXXDnhbPqjIfbI");
        return String.valueOf(_query);
    }

    private void optionPlace(final Context context, final int i) {
        final CharSequence[] options = {"Add to favorite", "Direction", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Option");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Add to favorite")) {
                } else if (options[item].equals("Direction")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" +
                                    Double.valueOf(lastKnownLocation.getLatitude()).toString() + ',' + Double.valueOf(lastKnownLocation.getLongitude()).toString() +
                                    "&daddr=" +
                                    _resultsList.get(i).getGeometry().getLocation().getLat() + ',' + _resultsList.get(i).getGeometry().getLocation().getLng()));
                    startActivity(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}