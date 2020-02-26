package com.navdeep.goaltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* After creating a goal, first milestone object will be created. The milestone activity
 * list will get updates automatically with time.
 */
public class MilestoneActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneView {
    private ListView mMilestoneListView;
    public static final String EXTRA_TIME = "time";
    public static final String DURATION = "duration";
    public static final String MILESTONE_TIME_PREFERENCE = "milestoneTimePreference";
    public static final String MILESTONE_TIME = "milestoneTime";
    public static final String MILESTONE_COUNT = "milestoneCount";
    public static final String POSITION = "position";
    public String currentTime, savedTime;
    private int count = 0, milestoneCount = 1;
    private int seconds, duration;
    Handler handler;
    Runnable runnable;
    public static MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone);
        String goalStartTime = getIntent().getStringExtra(MilestoneActivity.EXTRA_TIME);
        int position = getIntent().getIntExtra(MilestoneActivity.POSITION, 0);
       // String[] goalTime = goalStartTime.split(" ");
        //int gHour = Integer.valueOf(goalTime[0]);
      //  int gMinutes = Integer.valueOf(goalTime[1]);
      //  int gSeconds = Integer.valueOf(goalTime[2]);
        //get milestone presenter
        mMilestoneListView = findViewById(R.id.goalMilestoneView);

            milestonePresenter = new MilestonePresenter(this, position);
            //milestonePresenter.createMilestone("Day "+ 1, currentTime);

        currentTime = Calendar.getInstance().getTime().toString();
        mMilestoneListView = findViewById(R.id.goalMilestoneView);

        duration = getIntent().getIntExtra(DURATION, 1);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MilestoneActivity.this, MilestoneInputActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        mMilestoneListView.setOnItemClickListener(itemClickListener);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
       // milestonePresenter.getHandler().removeCallbacks(milestonePresenter.getRunnable());
//        handler.removeCallbacks(runnable);
        SharedPreferences sharedPreferences = getSharedPreferences(MILESTONE_TIME_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MILESTONE_TIME, currentTime);
        editor.putInt(MILESTONE_COUNT, count);
        editor.apply();
    }




    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(MILESTONE_TIME_PREFERENCE, MODE_PRIVATE);
        savedTime = sharedPreferences.getString(MILESTONE_TIME, currentTime);
        count = sharedPreferences.getInt(MILESTONE_COUNT, count);

       /* if(savedTime!=null && currentTime!=null) {
            SimpleDateFormat sdfCurrent = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat sdfSaved = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Calendar currentCalendar=null, savedCalendar=null;
            try {
                currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(sdfCurrent.parse(currentTime));
                savedCalendar = Calendar.getInstance();
                savedCalendar.setTime(sdfCurrent.parse(savedTime));
            }catch (Exception e){
                e.printStackTrace();
            }

            int minuteDifference = currentCalendar.get(Calendar.MINUTE) - savedCalendar.get(Calendar.MINUTE);

                for(int i=0; i < minuteDifference;++i) {
                    if (milestonePresenter.getMilestoneList().size() < 5) {
                        savedCalendar.add(Calendar.MINUTE, 1);
                        milestonePresenter.createMilestone("Minute "+savedCalendar.get(Calendar.MINUTE), savedCalendar.getTime().toString());
                    } else {
                        Toast.makeText(this, "goal completed", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
        }*/
    }

    @Override
    public void displayMilestones(ArrayList<Milestone> milestones) {
        ArrayAdapter<Milestone> adapter = new ArrayAdapter<>(this, R.layout.milestone_list_hold, milestones);
        mMilestoneListView.setAdapter(adapter);
    }

    private void runTimer() {
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                int secs = seconds%60;
                seconds++;

                if(seconds%60==0){
                    if(milestoneCount<duration)
                        updateMilestoneCollection();
                    else
                        handler.removeCallbacks(this);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void updateMilestoneCollection(){
        milestoneCount++;
        currentTime = Calendar.getInstance().getTime().toString();
        milestonePresenter.createMilestone("Day "+ milestoneCount, currentTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
