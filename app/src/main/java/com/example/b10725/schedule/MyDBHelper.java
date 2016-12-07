package com.example.b10725.schedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.b10725.schedule.R.id.date;

/**
 * Created by kangeunjin on 2016-11-22.
 */

public class MyDBHelper extends SQLiteOpenHelper {  //데이터베이스 클래스

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
// TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
        db.execSQL("CREATE TABLE SCHEDULEDB(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "schedule TEXT, " + "location TEXT , " + "year INTEGER, "+ "month INTEGER, "
                + "day INTEGER, "+ "starthour INTEGER, "+ "startmin INTEGER, "+ "endhour INTEGER, "
                + "endmin INTEGER, " + "memo TEXT,"+"date TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS SCHEDULEDB");
        Log.i("uuud","akdf=");
        onCreate(db);
    }

    public void insert(String schedule, String location,int year,int month,int day,int starthour,int startmin,int endhour,int endmin,String memo,String date) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO SCHEDULEDB VALUES(null, '" + schedule + "', '" + location + "', " + year + ","+month+","+day+","+starthour+","+startmin+","+endhour+","+endmin+",'"+memo+"','"+date+"');");
        db.close();
    }

    public void delete(String schedule) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE SCHEDULEDB WHERE * ;");
        db.close();
    }

    public void update(int year, int month, int day) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE SCHEDULEDB SET * WHERE item='" + year + "';");
        db.close();
    }





    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM SCHEDULEDB", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + ": "
                    + cursor.getInt(3)
                    + "년 "
                    + cursor.getInt(4)
                    + "월 "
                    + cursor.getInt(5)
                    + "일 "
                    + cursor.getInt(6)
                    + "시"
                    + cursor.getInt(7)
                    + "분 부터 "
                    + cursor.getInt(8)
                    + "시 "
                    + cursor.getInt(9)
                    + "분까지 "
                    + cursor.getString(10)
                    + "\n"
                    + cursor.getString(11)
                    + "\n";

        }

        return result;
    }








}