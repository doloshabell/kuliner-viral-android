package com.example.newsapplication.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends ArrayAdapter {

    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String, String>> data) {
        super(context, resource, data);
    }
}
