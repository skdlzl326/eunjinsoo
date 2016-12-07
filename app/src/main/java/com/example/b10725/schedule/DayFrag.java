package com.example.b10725.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;
import static com.example.b10725.schedule.MonthFrag.ClickD;
import static com.example.b10725.schedule.MonthFrag.ClickM;
import static com.example.b10725.schedule.MonthFrag.ClickY;

public class DayFrag extends Fragment {

    MyDBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;
    DBAdapter dbAdapter;
    private TextView tvDate;
    private GridView gridView;
    private Calendar dCal;
    private  Integer D = getDayInt();//세계 일
    private static Integer Max; // 달의 마지막 날 (30 or 31)
    private static Integer chanD; // 달력을 그릴 때 기준이 되는 날
    private Integer cases=0;// 전 날 버튼을 눌렀는 지 다음 날 버튼을 눌렀는지
    final static String dbName="SCHEDULEDB.db";
    public DayFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("일별 일정");

        final View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date); // 달력위에 년 월 표시
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        dbHelper= new MyDBHelper(getActivity(),dbName,null,3);
        Button mlastbtn = (Button) rootView.findViewById(R.id.MLastbutton); //지난달 버튼

        mlastbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chanD==1){
                    dCal=getLastMonth(dCal);
                    Max = dCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    chanD=Max;

                }
                else{chanD = chanD-1;}
                cases=4;
                getCalendar();
            }
        });
        Button mnextbtn = (Button) rootView.findViewById(R.id.MNextbutton); // 다음달 버튼

        mnextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                chanD= chanD+1;
                cases=5;
                getCalendar();

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",cursor.getInt(cursor.getColumnIndex("_id")));
                /*Log.i("uuud","id=" +cursor.getInt(cursor.getColumnIndex("_id")) );*/
                startActivity(intent);
            }
        });


        return rootView;


    }

    @Override
    public void onResume()
    { // 처음 현재 날짜에 관한 캘린더 출력
        super.onResume();
        dCal = Calendar.getInstance(Locale.KOREA);
        chanD=D;

        if(ClickY>0){ // 월별, 주별 보기에서 날을 선택한 경우 현재 날짜를 선택한 날로 바꿔준다.
            dCal.set(ClickY,ClickM,ClickD);
            chanD=ClickD;
        }

        Max = dCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        getCalendar();
        tvDate.setText((dCal.get(Calendar.MONTH) +1 ) + "월 " + chanD + "일");
    }

    //현재 날짜 텍스트뷰에 뿌려줌
    private void getCalendar() {
        //연,월,일을 따로 저장
        int a = dCal.get(Calendar.YEAR);
        int b = dCal.get(Calendar.MONTH) + 1;
        int c = chanD;

        String syear = Integer.toString(a);
        String smonth = Integer.toString(a);
        String sday = Integer.toString(a);

        setCalendarDate();

        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM SCHEDULEDB WHERE year=" + a + " AND month=" + b + " AND day=" + c;
        cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            getActivity().startManagingCursor(cursor);
            dbAdapter = new DBAdapter(getActivity(), cursor);
            gridView.setAdapter(dbAdapter);
        } else {
            dbAdapter = new DBAdapter(getActivity(), null);
            gridView.setAdapter(dbAdapter);
        }
    }

    private void setCalendarDate() { // 몇월인지 확인하고 그에 맞는 일수를 리스트에 채워줌

        if(chanD <=Max) {
            tvDate.setText((dCal.get(Calendar.MONTH) +1 ) + "월 " + chanD + "일");
        }

        else if(chanD > Max){
            if(cases==4)
            {
                dCal=getLastMonth(dCal);
            }
            else if(cases==5)
            {
                dCal=getNextMonth(dCal);
            }

            chanD=1;
            Max = dCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            tvDate.setText((dCal.get(Calendar.MONTH) +1 ) + "월 " + chanD + "일");
        }
    }

    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        return calendar;
    }

    public static Integer getDayInt(){
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        return (cal.get(Calendar.DAY_OF_MONTH));
    }

}