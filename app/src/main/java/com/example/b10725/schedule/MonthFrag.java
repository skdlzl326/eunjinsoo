package com.example.b10725.schedule;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthFrag extends Fragment {

    private TextView tvDate;
    private MonthFrag.GridAdapter gridAdapter;
    private ArrayList<String> dayList; // 요일과 일을 표시할 리스트
    private ArrayList<Integer> box;
    private GridView gridView;
    private Calendar mCal;
    private  Integer Y = getYearInt();//세계 년
    private  Integer M = getMonthInt();//세계 달
    private  Integer D = getDayInt();//세계 일
    public static Integer ClickY=0;
    public static Integer ClickM=0;
    public static Integer ClickD=0;

    public MonthFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("월별 일정");

        final View rootView = inflater.inflate(R.layout.fragment_month, container, false);

        tvDate = (TextView) rootView.findViewById(R.id.tv_date); // 달력위에 년 월 표시
        gridView = (GridView) rootView.findViewById(R.id.gridview);

        Button mlastbtn = (Button) rootView.findViewById(R.id.MLastbutton); //지난달 버튼

        mlastbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mCal = getLastMonth(mCal);
                getCalendar(mCal);
            }
        });
        Button mnextbtn = (Button) rootView.findViewById(R.id.MNextbutton); // 다음달 버튼

        mnextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mCal = getNextMonth(mCal);

                getCalendar(mCal);

            }
        });
        //연,월,일을 따로 저장
        return rootView;
    }

    @Override
    public void onResume()
    { // 처음 현재 날짜에 관한 캘린더 출력
        super.onResume();
        mCal = Calendar.getInstance(Locale.KOREA);
        mCal.set(Calendar.DAY_OF_MONTH, 1);

        getCalendar(mCal);
        tvDate.setText(mCal.get(Calendar.YEAR) + "년 "
                + (mCal.get(Calendar.MONTH) + 1) + "월");

        ClickY=0; // 현재 날짜로 되돌리기위해 변수 초기화
        ClickM=0;
        ClickD=0;
    }

        //현재 날짜 텍스트뷰에 뿌려줌
        private void getCalendar(Calendar calendar) // 현재 날짜를 반환해서 캘린더에 내용을 채우는 함수
        {
            //연,월,일을 따로 저장
            dayList = new ArrayList();
            dayList.add("일");
            dayList.add("월");
            dayList.add("화");
            dayList.add("수");
            dayList.add("목");
            dayList.add("금");
            dayList.add("토");

            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);

            //1일 - 요일 매칭 시키기 위해 공백 add
            for (int i = 1; i < dayNum; i++) {
                dayList.add("");
            }

            setCalendarDate(calendar.get(Calendar.MONTH));
            gridAdapter = new MonthFrag.GridAdapter(getActivity(), dayList);
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(mItemClickListener);
        }
    public AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position){

            Toast.makeText(getContext(), mCal.get(Calendar.YEAR)+"년 " + (mCal.get(Calendar.MONTH) +1) +"월 "
                    + gridAdapter.getItem(position)+ "일", Toast.LENGTH_SHORT).show();
            int numInt = Integer.parseInt(gridAdapter.getItem(position));
            ClickD= numInt;
            ClickY=mCal.get(Calendar.YEAR); // 일별 보기로 넘겨줄 변수
            ClickM=mCal.get(Calendar.MONTH); // 일별 보기로 넘겨줄 변수

            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,new DayFrag()).addToBackStack(null).commit();


        }
    };

    private void setCalendarDate(int month) { // 몇월인지 확인하고 그에 맞는 일수를 리스트에 채워줌
        mCal.set(Calendar.MONTH, month);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

            dayList.add("" + (i + 1));

        }

    }

    public class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {

            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            MonthFrag.ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.fragment_gridview, parent, false);

                holder = new MonthFrag.ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);

            } else {

                holder = (MonthFrag.ViewHolder)convertView.getTag();

            }

            holder.tvItemGridView.setText("" + getItem(position));

            if(position % 7 == 0)
            {
                holder.tvItemGridView.setTextColor(Color.RED);
            }
            else if(position % 7 == 6)
            {
                holder.tvItemGridView.setTextColor(Color.BLUE);
            }
            else
            {
                holder.tvItemGridView.setTextColor(Color.BLACK);
            }
            //해당 날짜 텍스트 컬러,배경 변경

            //오늘 day 가져옴
            Integer year = mCal.get(Calendar.YEAR); // 기기내 년
            Integer month = mCal.get(Calendar.MONTH)+1; // 기기내 월
            String sDay = String.valueOf(D); // 기기 내 일의 스트링값


            if (Y.equals(year) && M.equals(month) && sDay.equals(getItem(position))) {

                holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            return convertView;
        }
    }

    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        tvDate.setText(calendar.get(Calendar.YEAR) + "년 "
                + (calendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        tvDate.setText(calendar.get(Calendar.YEAR) + "년 "
                + (calendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
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

