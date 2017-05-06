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

        TextView licenseTextView = (TextView) convertView.findViewById(R.id.licenseTV);
        TextView fNameTextView = (TextView) convertView.findViewById(R.id.driverNameTV);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.hisTimeTV);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.hisDateTV);
        TextView lNameTextView = (TextView) convertView.findViewById(R.id.driverLnameTV);
        TextView regTextView = (TextView) convertView.findViewById(R.id.hisRegTV);
//        TextView

        HistoryData history = getItem(position);

        licenseTextView.setText(history.getLicense_number());
        timeTextView.setText(history.getTime());
        fNameTextView.setText(history.getDriver_fname());
        dateTextView.setText(history.getDate());
        lNameTextView.setText(history.getDriver_lname());
        regTextView.setText(history.getReg_number());
        Log.d("myTag", "dateTV: " +licenseTextView);

        return convertView;
    }

}
