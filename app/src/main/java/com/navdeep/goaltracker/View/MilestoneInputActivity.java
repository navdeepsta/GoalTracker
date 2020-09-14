package com.navdeep.goaltracker.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.navdeep.goaltracker.GoalUtil;
import com.navdeep.goaltracker.ImageAdapter;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.MilestoneTimer;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MilestoneInputActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneInputView {
    public static final String INPUT_FLAG = "inputflag";
    public static final String MILESTONE_ID = "milestonePosition";
    public static final String GOAL_ID = "goalId";
    private EditText description, title;
    private TextView timer;
    private ImageView gallery;
    private Button start;


    private MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;

    private Milestone milestone;
    MilestoneTimer milestoneTimer;
    private int goalId, milestoneId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_input);
        setContentViewItems();
        setGoalPositionAndId();


        milestone = getMilestone();
        milestoneTimer = new MilestoneTimer();
        milestonePresenter = MilestonePresenter.getMilestonePresenter(this);
        setListenersOnActivityItems();
        // Hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    private void setContentViewItems() {
        timer = findViewById(R.id.timer);
        start = findViewById(R.id.start);
        description = findViewById(R.id.description);
        title = findViewById(R.id.milestoneTitle);
        gallery = findViewById(R.id.gallery);

    }

    private void setGoalPositionAndId() {
        milestoneId = getIntent().getIntExtra(MILESTONE_ID, 0);
        goalId = getIntent().getIntExtra(GOAL_ID, 0);
    }

    private Milestone getMilestone() {
        ArrayList<Milestone> milestones = MilestonePresenter.getMilestonePresenter().getMilestones(goalId);
        for(Milestone milestone : milestones){
            if(milestone.getMilestoneId() == milestoneId){
                return milestone;
            }
        }
        return null;
    }

    private void setListenersOnActivityItems() {
        setListenerOnDescriptionEditText();
        setListenerOnTitleEditText();
        setListenersOnStartButton();
        setListenerOnGalleryButton();
    }




    private void setListenerOnDescriptionEditText() {
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String description = s.toString();
                milestone.setDescription(description);
            }
        });
    }

    private void setListenerOnTitleEditText() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                milestone.setTitle(text);
            }
        });
    }



    private void setListenersOnStartButton() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!milestoneTimer.isRunning()) {
                    milestoneTimer.runTimer(timer);
                    milestoneTimer.setRunning(true);
                    start.setText("Stop");
                }else {
                    pauseTimer();
                    milestoneTimer.setRunning(false);

                }
            }
        });
    }


    private void pauseTimer(){
        if(milestoneTimer!=null){
            milestoneTimer.setSeconds(milestoneTimer.getSeconds());
            milestoneTimer.setRunTimerCounterToZero();
            if(milestoneTimer.getHandler()!=null) {
                milestoneTimer.getHandler().removeCallbacks(milestoneTimer.getRunnable());
            }
        }
        start.setText("Start");
    }
    private void setListenerOnGalleryButton() {
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MilestoneInputActivity.this, MilestoneImageActivity.class);
                intent.putExtra(MilestoneImageActivity.GOAL_ID, goalId);
                intent.putExtra(MilestoneImageActivity.MILESTONE_ID, milestoneId);
                startActivity(intent);
            }
        });

    }



    @Override
    public void showDescription() {
    description.setText(milestone.getDescription());
    title.setText(milestone.getTitle());
    }




    @Override
    protected void onResume() {
        super.onResume();
        showDescription();
        disableInputsForOldMilestones();
        updateTimerTextView();
    }

    private void disableInputsForOldMilestones(){
        boolean inputFlag = getIntent().getBooleanExtra(INPUT_FLAG, true);
        if(inputFlag){
            disableInputs();
        }
    }
    private void disableInputs(){
        start.setEnabled(false);
    }

    private void updateTimerTextView() {
        timer.setText(milestone.getTimer());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDetails();
        pauseTimer();
    }

    private void saveDetails() {
        milestoneTimer.setMilestoneTimer(timer.getText().toString());
        milestone.setTimer(milestoneTimer.getMilestoneTimer());
        milestonePresenter.updateMilestone(milestone);
    }
}
