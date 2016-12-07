package com.example.b10725.schedule;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ScheduleAddFrag extends DialogFragment {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    final int DIALOG_DATE = 1;
    final int DIALOG_TIME = 2;


    public ScheduleAddFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        Button btn1 =(Button)rootView.findViewById(R.id.date);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getActivity().showDialog(DIALOG_DATE); /// 날짜 설정 다이얼로그 띄우기
               /* Intent intent = new Intent (getActivity(), EditActivity.class);
                startActivity(intent);// 날짜 설정 다이얼로그 띄우기*/
            }
        });
        Button btn2 =(Button)rootView.findViewById(R.id.time);

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getActivity().showDialog(DIALOG_TIME); // 날짜 설정 다이얼로그 띄우기
                /*Intent intent = new Intent (getActivity(), EditActivity.class);
                startActivity(intent);// 날짜 설정 다이얼로그 띄우기*/
            }
        });
        return rootView;
    }

    protected Dialog onCreateDialog(int id) {
        if(id==DIALOG_DATE){

            DatePickerDialog dpd = new DatePickerDialog
                    (getActivity(), // 현재화면의 제어권자
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,int dayOfMonth) {

                                    Toast.makeText(getActivity().getApplicationContext(),
                                            year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                            //    호출할 리스너 등록
                            2016, 11, 20); // 기본값 연월일
            return dpd;
        }else{

            TimePickerDialog tpd =
                    new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            hourOfDay +"시 " + minute+"분 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }, // 값설정시 호출될 리스너 등록
                            4,19, false); // 기본값 시분 등록
            // true : 24 시간(0~23) 표시
            // false : 오전/오후 항목이 생김
            return tpd;
        }



    }


}
