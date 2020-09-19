package com.example.finalproject2.Helper;

import android.util.Log;

import com.example.finalproject2.Model.Results;
import com.example.finalproject2.Model.SearchResultRoot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchHandling {
    private String _request;


    public SearchHandling(String _mRequest) {
        this._request = _mRequest;
    }

    public ArrayList<Results> search() {
        ArrayList<Results> ret = new ArrayList<>();
        Type listType = new TypeToken<ArrayList<Results>>() {
        }.getType();
        try {
            URL url;
            url = new URL(_request);
            Log.d("res", _request);
            InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
            SearchResultRoot temp = new SearchResultRoot();
            temp = new Gson().fromJson(reader, SearchResultRoot.class);
            ret.addAll(temp.getResults());
            Log.d("res", Integer.valueOf(ret.size()).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
