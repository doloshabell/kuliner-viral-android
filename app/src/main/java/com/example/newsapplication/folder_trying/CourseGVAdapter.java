package com.example.newsapplication.folder_trying;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapplication.R;

import java.util.ArrayList;

public class CourseGVAdapter extends ArrayAdapter<CourseModel> {

    public CourseGVAdapter(@NonNull Context context, ArrayList<CourseModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_card, parent, false);
        }

        CourseModel courseModel = getItem(position);
        TextView textView = listItemView.findViewById(R.id.itemCardTextViewName);
        ImageView imageView = listItemView.findViewById(R.id.itemCardImageView);

        textView.setText(courseModel.getCourse_name());
        imageView.setImageResource(courseModel.getImgid());
        return listItemView;
    }
}
