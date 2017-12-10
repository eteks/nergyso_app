package com.example.user.energysoft;

/**
 * Created by root on 9/12/17.
 */

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;


public class NewsListAdapter extends ArrayAdapter<news>{
    private Context mContext;
    public static  String[] vehicle_id = new String[100];
    /**
     * Adapter View layout
     */
    private int mLayoutResourceId;
    int index = 0;


    public NewsListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final news currentItem = getItem(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }
//        System.out.println("Current item :"+currentItem);
        row.setTag(currentItem);
        System.out.println(currentItem);
//        final TextView text = (TextView)  row.findViewById(R.id.textView);
////        System.out.println(currentItem.getText());
//        String [] title = currentItem.getText().split(",");
////        System.out.println(title[0]);
//        text.setText(title[0]);
//        final TextView vehicleStatus = (TextView)  row.findViewById(R.id.vehicleStatus);
//        vehicle_id[position] = title[1];
        return row;
    }
}
