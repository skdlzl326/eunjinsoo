package com.example.b10725.schedule;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static com.example.b10725.schedule.MonthFrag.ClickD;
import static com.example.b10725.schedule.MonthFrag.ClickM;
import static com.example.b10725.schedule.MonthFrag.ClickY;

public class EditActivity extends AppCompatActivity {

    private Calendar cal;
    private java.util.Calendar EditCal;
    final int DATE = 1;
    final int START_TIME = 2;
    final int END_TIME = 3;
    int YEAR;
    int MONTH;
    int DAY;
    int Starthour;
    int Startmin;
    int Endhour;
    int Endmin;
    MyDBHelper dbHelper;
    EditText scheduleText, locationText, memoText ;
    Date date;
    SQLiteDatabase sql;
    SimpleDateFormat dateFormat ;
    String strDate;
    String daydate;
    int intentid;
    public TextView text,result;
    int EditYear;
    int EditMonth;
    int EditDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dateFormat= new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        dbHelper = new MyDBHelper(getApplicationContext(),"SCHEDULEDB.db",null,3);
        Button b1 = (Button)findViewById(R.id.datebtn);
        Button b2 = (Button)findViewById(R.id.startbtn);
        Button b3 = (Button)findViewById(R.id.endbtn);
        Button b4 = (Button)findViewById(R.id.savebtn);
        scheduleText = (EditText) findViewById(R.id.sedit);
        locationText = (EditText)findViewById(R.id.locationedit);
        memoText = (EditText)findViewById(R.id.memoedit);
        result=(TextView)findViewById(R.id.resulttext);
        intentid=-1;
        EditCal = java.util.Calendar.getInstance(Locale.KOREA);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE);// 날짜 설정 다이얼로그 띄우기
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_TIME);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_TIME);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox option1 = (CheckBox)findViewById(R.id.checkBox);
                if(option1.isChecked()){
                    new AlarmHATT(getApplicationContext()).Alarm();
                }
                String schedulestring = scheduleText.getText().toString();
                String locationstring = locationText.getText().toString();
                String memostring = memoText.getText().toString();
                Toast.makeText(getApplicationContext(), "일정이 저장되었습니다.", Toast.LENGTH_LONG).show();
                dbHelper.insert(schedulestring,locationstring,YEAR,MONTH,DAY,Starthour,Startmin,Endhour,Endmin,memostring,daydate);
                result.setText(dbHelper.getResult());

            }
        });

    }
    public class AlarmHATT { // Manifest 부분에 진동, 홀드상태 활성화 두개의 퍼미션 추가
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(EditActivity.this, Receiver.class);

            PendingIntent sender = PendingIntent.getBroadcast(EditActivity.this, 0, intent, 0 );

            cal=Calendar.getInstance(Locale.KOREA);
            cal.set(Calendar.YEAR,YEAR);
            cal.set(Calendar.MONTH,MONTH-1);
            cal.set(Calendar.DAY_OF_MONTH,DAY);
            cal.set(Calendar.HOUR_OF_DAY, Starthour);
            cal.set(Calendar.MINUTE, Startmin);
            cal.set(Calendar.SECOND,00);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if(ClickY>0){
            EditYear = ClickY;
            EditMonth = ClickM;
            EditDay = ClickD;

        }
        else if(ClickY==0){
             EditYear = EditCal.get(java.util.Calendar.YEAR);
             EditMonth = EditCal.get(java.util.Calendar.MONTH);
             EditDay = EditCal.get(java.util.Calendar.DAY_OF_MONTH);
        }

        switch(id){
            case DATE :
                DatePickerDialog dpd = new DatePickerDialog
                        (EditActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {

                                        datefunction( year,  monthOfYear, dayOfMonth);
                                        date = new Date(year, monthOfYear, dayOfMonth);
                                        strDate = dateFormat.format(date);

                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                EditYear, EditMonth, EditDay); // 기본값 연월일

                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return dpd;


            case START_TIME :
                TimePickerDialog tpd =
                        new TimePickerDialog(EditActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {

                                        StartTime( hourOfDay,  minute);
                                            /*  TextView text = (TextView)findViewById(R.id.datetext);
                                            text.setText( hourOfDay +"시 " + minute+"분 을 선택했습니다");*/

                                    }
                                }, // 값설정시 호출될 리스너 등록
                                10,00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return tpd;


            case END_TIME :
                TimePickerDialog epd =
                        new TimePickerDialog(EditActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        EndTime(hourOfDay, minute);
                                            /* TextView text = (TextView)findViewById(R.id.datetext);
                                            text.setText( hourOfDay +"시 " + minute+"분 을 선택했습니다");*/

                                    }
                                }, // 값설정시 호출될 리스너 등록
                                10,00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return epd;
        }


        return super.onCreateDialog(id);
    }

    public void datefunction(int year, int monthOfYear,int dayOfMonth){
        YEAR=year;
        MONTH=monthOfYear+1;
        DAY = dayOfMonth;
        String a=Integer.toString(YEAR);
        String b=Integer.toString(MONTH);
        String c=Integer.toString(DAY);
        daydate=a+b+c;

    }
    /*public String getDateTime(){
        return dateFormat.format(date);

    }*/


    public void StartTime(int hourOfDay, int minute){
        Starthour=hourOfDay;
        Startmin=minute;
    }

    public void EndTime(int hourOfDay, int minute){
        Endhour=hourOfDay;
        Endmin=minute;

    }

  /*  public void update(int intent){

    }*/

}