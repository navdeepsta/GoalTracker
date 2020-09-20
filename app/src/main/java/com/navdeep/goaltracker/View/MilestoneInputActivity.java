package com.navdeep.goaltracker.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.Utility.MilestoneTimer;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class MilestoneInputActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneInputView
,NoteFragment.OnNoteFragmentInteractionListener, ImageInputFragment.OnImageInputFragmentInteractionListener, ImageFragment.OnFragmentInteractionListener{
    public static final String INPUT_FLAG = "inputflag";
    public static final String MILESTONE_ID = "milestonePosition";
    public static final String GOAL_ID = "goalId";
    private TextView timer;
    private ImageView start;
    private Button noteButton, imageButton;
    private MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;

    private Milestone milestone;
    MilestoneTimer milestoneTimer;
    private int goalId, milestoneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_input);
        noteButton = findViewById(R.id.note_button);
        imageButton = findViewById(R.id.image_button);
        setListenersOnNoteAndImageButton();
        setContentViewItems();
        setGoalPositionAndId();
        createNoteFragment();


        milestone = getMilestone();
        milestoneTimer = new MilestoneTimer();
        milestonePresenter = MilestonePresenter.getMilestonePresenter(this);
        setListenersOnActivityItems();
        // Hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setListenersOnNoteAndImageButton() {
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNoteFragment();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImageFragment();
            }
        });
    }


    private void createNoteFragment() {
        NoteFragment noteFragment = NoteFragment.newInstance(goalId, milestoneId);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameContainer, noteFragment)
                .commit();


    }
    private void createImageFragment() {
        ImageInputFragment imageInputFragment = ImageInputFragment.newInstance(goalId,milestoneId);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameContainer, imageInputFragment)
                .commit();
    }

    private void setContentViewItems() {
        timer = findViewById(R.id.timer);
        start = findViewById(R.id.start);

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
        setListenersOnStartButton();
    }

    private void setListenersOnStartButton() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!milestoneTimer.isRunning()) {
                    milestoneTimer.runTimer(timer);
                    milestoneTimer.setRunning(true);
                    start.setImageResource(R.drawable.ic_stop_black_50dp);
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
        start.setImageResource(R.drawable.ic_play_circle_filled_black_50dp);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void chooseFragment(View v) {
        switch (v.getId()) {
            case R.id.note_button:

                break;
            case R.id.image_button:

                break;
                default:
        }
    }

    @Override
    public void onNoteFragmentInteraction(Milestone milestone) {
       this.milestone = milestone;
    }

    @Override
    public void showDescription() {

    }

    @Override
    public void onImageInputFragmentInteraction(int imageId) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.MILESTONE_ID, milestoneId);
        intent.putExtra(ImageActivity.IMAGE_ID,imageId);
        startActivity(intent);
    }
}
