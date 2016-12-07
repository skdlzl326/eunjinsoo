package com.example.b10725.schedule;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.b10725.schedule.MonthFrag.ClickD;
import static com.example.b10725.schedule.MonthFrag.ClickM;
import static com.example.b10725.schedule.MonthFrag.ClickY;

public class WeekFrag extends Fragment {

    private TextView tvDate;
    private WeekFrag.GridAdapter gridAdapter;
    private ArrayList<String> dayList2; // 요일과 일을 표시할 리스트
    private GridView gridView;
    private Calendar wCal; // 현재날짜를 표현할 변수
    private static Integer Max; // 달의 마지막 날 (30 or 31)
    private Integer cases=0;// 한 주를 그릴 때 세가지 경우를 나타내는 변수
    private static Integer chanD; // 달력을 그릴 때 기준이 되는 날
    private Integer Y = getYearInt();//세계 년
    private Integer M = getMonthInt();//세계 달
    private Integer D = getDayInt();//세계 일
    private  String sDay = String.valueOf(D); // 기기 내 일의 스트링값
    private Integer temp; // 임시 변수

    public WeekFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("주별 일정");
        final View rootView = inflater.inflate(R.layout.fragment_week, container, false);

        tvDate = (TextView) rootView.findViewById(R.id.tv_date); // 달력위에 년 월 표시
        gridView = (GridView) rootView.findViewById(R.id.gridview);

        Button wlastbtn = (Button) rootView.findViewById(R.id.WLastbutton); //지난달 버튼
        wlastbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cases==3) {
                    wCal.add(Calendar.MONTH, -1);
                    int changeMax = wCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                    chanD= chanD-7+changeMax; // 일요일 구하는 식

                }
                else{chanD=chanD-7;}
                cases=0;
                getCalendar();

            }
        });
        Button wnextbtn = (Button) rootView.findViewById(R.id.WNextbutton); // 다음달 버튼
        wnextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(cases==2){
                    Max= wCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 전 달에 마지막 날 (30  or 31)
                    chanD=chanD+7-Max;
                    wCal.add(Calendar.MONTH, +1);
                }
                else{chanD=chanD+7;}
                cases=0;
                getCalendar();

            }
        });
        //연,월,일을 따로 저장
        return rootView;
    }

    @Override
    public void onResume() { // 처음 현재 날짜에 관한 캘린더 출력
        super.onResume();
        wCal = Calendar.getInstance();
        wCal.set(Calendar.DAY_OF_MONTH, D);
        chanD=D;
        int dayNum = (wCal.get(Calendar.DAY_OF_WEEK))-1;
        chanD= chanD - dayNum; // 이번 주의 일요일

        if(chanD+6>(wCal.getActualMaximum(Calendar.DAY_OF_MONTH))){
            cases = 2;
        }
        tvDate.setText(wCal.get(Calendar.YEAR) + "년 "
                + (wCal.get(Calendar.MONTH) + 1) + "월");

        getCalendar();

        ClickY=0; // 현재 날짜로 되돌리기위해 변수 초기화
        ClickM=0;
        ClickD=0;
    }

    //현재 날짜 텍스트뷰에 뿌려줌
    private void getCalendar() // 현재 날짜를 반환해서 캘린더에 내용을 채우는 함수
    {
        //연,월,일을 따로 저장
        dayList2 = new ArrayList();
        dayList2.add("일");
        dayList2.add("월");
        dayList2.add("화");
        dayList2.add("수");
        dayList2.add("목");
        dayList2.add("금");
        dayList2.add("토");

        setCalendarDate();

        gridAdapter = new WeekFrag.GridAdapter(getActivity(), dayList2);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(mItemClickListener);

    }
    public AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position){

            Toast.makeText(getContext(), wCal.get(Calendar.YEAR)+"년 " + (wCal.get(Calendar.MONTH) +1) +"월 "
                    + gridAdapter.getItem(position)+ "일", Toast.LENGTH_SHORT).show();
            int numInt = Integer.parseInt(gridAdapter.getItem(position));
            ClickD= numInt;
            ClickY=wCal.get(Calendar.YEAR); // 일별 보기로 넘겨줄 변수
            ClickM=wCal.get(Calendar.MONTH); // 일별 보기로 넘겨줄 변수

            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,new DayFrag()).addToBackStack(null).commit();


        }
    };

    private void setCalendarDate() {// 달력 내용 채우기

        tvDate.setText(wCal.get(Calendar.YEAR) + "년 "
                + (wCal.get(Calendar.MONTH) + 1) + "월");

        if(chanD+6 <= wCal.getActualMaximum(Calendar.DAY_OF_MONTH) && chanD > 0) { // 한 주를 그렸을 때 달이 바뀌지 않을경우
            for (int i = chanD; i <= chanD + 6; i++) {
                dayList2.add("" + (i));
            }
            cases=1;

            temp=chanD+6;

            if(temp.equals(wCal.getActualMaximum(Calendar.DAY_OF_MONTH))){
                cases=2;
            }
           else if(chanD<8){
                cases=3;
            }
        }

        else if(chanD+6 > wCal.getActualMaximum(Calendar.DAY_OF_MONTH)){
            Max= wCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 전 달에 마지막 날 (30  or 31)

            for (int i = chanD; i <= Max; i++) { //한 주를 그렸을 때 다음달로 넘어가는경우
                dayList2.add("" + (i));
            }
            for(int i =1 ; i<=(chanD+6)-Max; i++){
                dayList2.add("" + (i));
            }

            cases =2;
        }

        else if(chanD<1){
            wCal.add(Calendar.MONTH, -1);
            Max= wCal.getActualMaximum(Calendar.DAY_OF_MONTH);

            chanD= chanD + Max;
            for(int i =chanD ; i<=Max; i++){
                dayList2.add("" + (i));
            }

            for(int i = 1 ; i<=(chanD+6)-Max;i++){
                dayList2.add("" + (i));
            }
            cases=2;
        }

    }

    /**
     * 그리드뷰 어댑터
     */

    private class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override

        public int getCount() {

            return list.size();
        }

        @Override

        public String getItem(int position) {

            return list.get(position);
        }

        @Override

        public long getItemId(int position) {

            return position;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            WeekFrag.ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.fragment_gridview, parent, false);

                holder = new WeekFrag.ViewHolder();

                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);

            } else {

                holder = (WeekFrag.ViewHolder) convertView.getTag();

            }

            holder.tvItemGridView.setText("" + getItem(position));

            if (position % 7 == 0) {
                holder.tvItemGridView.setTextColor(Color.RED);
            } else if (position % 7 == 6) {
                holder.tvItemGridView.setTextColor(Color.BLUE);
            } else {
                holder.tvItemGridView.setTextColor(Color.BLACK);
            }
            //해당 날짜 텍스트 컬러,배경 변경

            int year = wCal.get(Calendar.YEAR); // 기기내 년
            int month = wCal.get(Calendar.MONTH) + 1; // 기기내 월
            if (Y.equals(year) && M.equals(month) && sDay.equals(getItem(position))) {


                holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

            return convertView;
        }
    }

    public static Integer getYearInt(){
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        return (cal.get(Calendar.YEAR));
    }

    public static Integer getMonthInt(){
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        return (cal.get(Calendar.MONTH) + 1);
    }

    public static Integer getDayInt(){
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        return (cal.get(Calendar.DAY_OF_MONTH));
    }

    private class ViewHolder {

        TextView tvItemGridView;
    }
}


