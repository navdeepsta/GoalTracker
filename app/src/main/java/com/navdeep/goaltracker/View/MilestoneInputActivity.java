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
import android.widget.TextView;
import android.widget.Toast;

import com.navdeep.goaltracker.ImageAdapter;
import com.navdeep.goaltracker.Interfaces.MilestoneModelViewPresenter;
import com.navdeep.goaltracker.MilestoneTimer;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class MilestoneInputActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneInputView {
    private static final int REQUEST_CAMERA = 1;
    public static final String MILESTONE_ID = "milestonePosition";
    public static final String GOAL_ID = "goalId";
    private EditText description, title;
    private TextView timer;
    private Button start, stop, addImage, saveDetails;
    private GridView gridView;
    private FrameLayout frameLayout;

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
        milestonePresenter = new MilestonePresenter(this);
        gridView.setAdapter(new ImageAdapter(MilestoneInputActivity.this, milestone.getBitmapList()));
        setListenersOnActivityItems();
        // Hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    private void setContentViewItems() {
        frameLayout = findViewById(R.id.frameLayout);
        gridView =  findViewById(R.id.grid_imageview);
        timer = findViewById(R.id.timer);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        description = findViewById(R.id.description);
        title = findViewById(R.id.milestoneTitle);
        addImage = findViewById(R.id.add_image);
        saveDetails = findViewById(R.id.saveDetails);
    }

    private void setGoalPositionAndId() {
        milestoneId = getIntent().getIntExtra(MILESTONE_ID, 0);
        goalId = getIntent().getIntExtra(GOAL_ID, 0);
    }

    private Milestone getMilestone() {
        ArrayList<Milestone> milestones = MilestoneActivity.milestonePresenter.getMilestones(goalId);
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
        setListenerOnImageButton();
        setListenersOnStartButton();
        setListenersOnStopButton();
        setListenerOnSaveDetails();
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
                milestone.setDescription(s.toString());
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
                milestone.setTitle(s.toString());
            }
        });
    }

    private void setListenerOnImageButton() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ask for camera permission from the user
                if(ContextCompat.checkSelfPermission(MilestoneInputActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(MilestoneInputActivity.this, "You have no permission to access camera", Toast.LENGTH_LONG).show();
                    //Explanation
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MilestoneInputActivity.this, Manifest.permission.CAMERA)){
                        Toast.makeText(MilestoneInputActivity.this, "More than once asking for permission",  Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(MilestoneInputActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }else{
                        ActivityCompat.requestPermissions(MilestoneInputActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
    }

    private void setListenersOnStartButton() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milestoneTimer.runTimer(timer);
            }
        });
    }

    private void setListenersOnStopButton() {
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(milestoneTimer!=null){
                    milestoneTimer.setSeconds(Integer.parseInt(timer.getText().toString()));
                    milestoneTimer.getHandler().removeCallbacks(milestoneTimer.getRunnable());
                }
            }
        });
    }

    private void setListenerOnSaveDetails() {
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milestonePresenter.updateMilestone(milestone);
                Toast.makeText(MilestoneInputActivity.this, "Saved",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showDescription() {
    description.setText(milestone.getDescription());
    title.setText(milestone.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap)bundle.get("data");
                /* TODO add a bitmap to a database
                *  */
               // bitmaps.add(bitmap);
                milestonePresenter.addImage(milestone.getMilestoneId(), bitmap);

                updateImageAdapter();

               // milestone.setBitmap(bitmaps);
             //   cameraImage.setImageBitmap(milestone.getBitmap());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showDescription();
        updateImageAdapter();
    }

    private void updateImageAdapter(){
        ArrayList<Bitmap> bitmaps = milestonePresenter.getImages(milestone.getMilestoneId());
        ImageAdapter imageAdapter = new ImageAdapter(MilestoneInputActivity.this, bitmaps);
        gridView.setAdapter(imageAdapter);
    }

}
