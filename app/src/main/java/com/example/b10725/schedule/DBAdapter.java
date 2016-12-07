package com.example.b10725.schedule;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kangeunjin on 2016-11-29.
 */

public class DBAdapter extends CursorAdapter{
    public DBAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.listlayout, parent, false);
        return v;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView schdule = (TextView)view.findViewById(R.id.listschedule);
        final TextView location = (TextView)view.findViewById(R.id.listlocation);
        final TextView date = (TextView)view.findViewById(R.id.listdate);

        schdule.setText("일정:"+cursor.getString(cursor.getColumnIndex("schedule")));
        location.setText("위치 :"+cursor.getString(cursor.getColumnIndex("location")));
        date.setText("날짜 :"+cursor.getString(cursor.getColumnIndex("year"))+"년"+cursor.getString(cursor.getColumnIndex("month"))+"월"+cursor.getString(cursor.getColumnIndex("day"))+"일");



    }
}