package com.finalproject.youcef.bobo;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Youcef on 10/04/2017.
 */

import com.bumptech.glide.Glide;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryData> {

    public HistoryAdapter(Context context, int resource, List<HistoryData> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_history, parent, false);
        }

//        ImageView photoImageView = (TextView) convertView.findViewById(R.id.photoImageView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTV);
        TextView fNameTextView = (TextView) convertView.findViewById(R.id.driverNameTV);

        HistoryData history = getItem(position);

        dateTextView.setText(history.getlNumber());
        fNameTextView.setText(history.getFname());
        Log.d("myTag", "dateTV: " +dateTextView);

        return convertView;
    }

}
