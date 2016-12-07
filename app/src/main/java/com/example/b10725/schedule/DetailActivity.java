package com.example.b10725.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


public class DetailActivity extends Activity {

    MyDBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;
    TextView scheduletxt;
    TextView locationtxt;
    TextView datetxt;
    TextView memotxt;
    Button edit;
    Button delete;
    int cursorposition;


    final static String dbName="SCHEDULEDB.db";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        scheduletxt = (TextView)findViewById(R.id.scheduletxt);
        locationtxt = (TextView)findViewById(R.id.locationtxt);
        datetxt=(TextView)findViewById(R.id.datetxt);
        memotxt=(TextView)findViewById(R.id.memotxt);
        edit=(Button)findViewById(R.id.editbtn);
        delete=(Button)findViewById(R.id.deletebtn);
        dbHelper= new MyDBHelper(this,dbName,null,3);



        Intent intent=getIntent();
        cursorposition=intent.getExtras().getInt("id");
        Log.i("uuud","cursor=" + cursorposition);

        selectDB();

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogHtmlView();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db.execSQL("DELETE FROM SCHEDULEDB WHERE _id="+cursorposition+";");
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                String schedulestring = scheduletxt.getText().toString();
                String locationstring = locationtxt.getText().toString();
                intent.putExtra("editid",schedulestring);
                intent.putExtra("editlocatin=onid",locationstring);


                startActivity(intent);
            }
        });
    }

    private void DialogHtmlView(){ // 삭제 확인 다이얼로그

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setMessage(Html.fromHtml("<strong><font color=\"#ff0000\">" + "정말 삭제하시겠습니까?")).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        db.execSQL("DELETE FROM SCHEDULEDB WHERE _id="+cursorposition+";");
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void selectDB() {
        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM SCHEDULEDB WHERE _id=" + cursorposition;
        cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            startManagingCursor(cursor);

            while (cursor.moveToNext()) {
                Log.i("uuud","while=" + cursorposition);
                scheduletxt.setText("일정 : "+cursor.getString(cursor.getColumnIndex("schedule")));
                locationtxt.setText("위치 : "+cursor.getString(cursor.getColumnIndex("location")));
                datetxt.setText(cursor.getString(cursor.getColumnIndex("year"))+"년 "+cursor.getString(cursor.getColumnIndex("month"))+"월 "+cursor.getString(cursor.getColumnIndex("day"))+"일 "+
                        cursor.getString(cursor.getColumnIndex("starthour"))+"시"+cursor.getString(cursor.getColumnIndex("startmin"))+"분 부터 "+cursor.getString(cursor.getColumnIndex("endhour"))+"시"+cursor.getString(cursor.getColumnIndex("endmin"))+"분 까지");
                memotxt.setText(cursor.getString(cursor.getColumnIndex("memo")));
            }
        }
    }

}
