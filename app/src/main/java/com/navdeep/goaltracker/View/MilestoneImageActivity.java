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
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.navdeep.goaltracker.ImageAdapter;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

/* The activity manages capture of image, its storage and retrieval*/
public class MilestoneImageActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    public static final String MILESTONE_ID = "milestonePosition";
    public static final String GOAL_ID = "goalId";
    private GridView gridView;
    private Button addImage;
    private int milestoneId, goalId;
    private Milestone milestone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_image);
        gridView =  findViewById(R.id.grid_imageview);
        addImage = findViewById(R.id.add_image);
        setGoalPositionAndId();
        milestone = getMilestone();
        gridView.setAdapter(new ImageAdapter(MilestoneImageActivity.this, milestone.getBitmapList()));
        setListenersOnImageButton();
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

    private void setListenersOnImageButton() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ask for camera permission from the user
                if(ContextCompat.checkSelfPermission(MilestoneImageActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(MilestoneInputActivity.this, "You have no permission to access camera", Toast.LENGTH_LONG).show();
                    //Explanation
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MilestoneImageActivity.this, Manifest.permission.CAMERA)){
                        Toast.makeText(MilestoneImageActivity.this, "More than once asking for permission",  Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(MilestoneImageActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }else{
                        ActivityCompat.requestPermissions(MilestoneImageActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap)bundle.get("data");
                /* TODO add a bitmap to a database
                 *  */
                // bitmaps.add(bitmap);
                MilestonePresenter.getMilestonePresenter().addImage(milestone.getMilestoneId(), bitmap);

                updateImageAdapter();

                // milestone.setBitmap(bitmaps);
                //   cameraImage.setImageBitmap(milestone.getBitmap());
            }
        }
    }


    private void updateImageAdapter(){
        ArrayList<Bitmap> bitmaps = MilestonePresenter.getMilestonePresenter().getImages(milestone.getMilestoneId());
        ImageAdapter imageAdapter = new ImageAdapter(MilestoneImageActivity.this, bitmaps);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateImageAdapter();
    }
}
