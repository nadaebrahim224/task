package com.example.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class event_adapter extends ArrayAdapter<event_data> {


    public event_adapter(@androidx.annotation.NonNull Context context, int resource, @androidx.annotation.NonNull List<event_data> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(getContext()).inflate(R.layout.event_row_design,parent,false);
        TextView event_name=convertView.findViewById(R.id.event_name);
//        Button accept_event=convertView.findViewById(R.id.btn_accept);
//        Button decline_event=convertView.findViewById(R.id.btn_decline);
//        TextView show_event_details=convertView.findViewById(R.id.show_event_details);


        event_name.setText(getItem(position).getEvent_name());

        return convertView;
    }
}
