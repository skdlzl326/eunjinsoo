package com.example.b10725.schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Month:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new MonthFrag()).commit();
                return true;
            case R.id.Week:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new WeekFrag()).commit();
                return true;
            case R.id.Day:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new DayFrag()).commit();
                return true;
            case R.id.Add:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

