package com.navdeep.goaltracker.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.navdeep.goaltracker.ImageAdapter;
import com.navdeep.goaltracker.POJOs.Goal;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.POJOs.MilestoneImage;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/* The activity manages capture of image, its storage and retrieval*/
public class MilestoneImageActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    public static final String MILESTONE_ID = "milestonePosition";
    public static final String GOAL_ID = "goalId";
    private GridView gridView;
    private Button addImage;
    private int milestoneId, goalId;
    private Milestone milestone;
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_image);
        gridView =  findViewById(R.id.grid_imageview);
        addImage = findViewById(R.id.add_image);
        setGoalPositionAndId();
        milestone = getMilestone();
        gridView.setAdapter(new ImageAdapter(MilestoneImageActivity.this, milestone.getBitmapList()));
        setListenerOnImageButton();
        setOnItemClickListenerOnGridView();
        setOnLongPressListenerOnGridView();
        setMultiChoiceModeListenerOnGridView();


    }

    private void setOnLongPressListenerOnGridView() {
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.getChildAt(position).setBackgroundColor(Color.RED);
                return true;
            }
        });
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

    private void setListenerOnImageButton() {
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
                MilestoneImage image = new MilestoneImage(bitmap, Calendar.getInstance());
                MilestonePresenter.getMilestonePresenter().addImage(milestone.getMilestoneId(), image);

                updateImageAdapter();

                // milestone.setBitmap(bitmaps);
                //   cameraImage.setImageBitmap(milestone.getBitmap());
            }
        }
    }

    private void setOnItemClickListenerOnGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<MilestoneImage> images = getImages();
                int imageId =  images.get(position).getImageId();
             Intent intent = new Intent(MilestoneImageActivity.this, ImageActivity.class);
             intent.putExtra(ImageActivity.MILESTONE_ID, milestoneId);
             intent.putExtra(ImageActivity.IMAGE_ID,imageId);
             startActivity(intent);
            }
        });
     }


    private void setMultiChoiceModeListenerOnGridView() {
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new GridView.MultiChoiceModeListener() {
            ArrayList<MilestoneImage> images = new ArrayList<>();
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                MilestoneImage image = (MilestoneImage)imageAdapter.getItem(position);
                if(images.contains(image)){
                    gridView.getChildAt(position).setBackgroundColor(Color.parseColor("#009688"));
                    images.remove(image);
                }else {
                    images.add(image);
                    gridView.getChildAt(position).setBackgroundColor(Color.RED);
                }
                mode.setTitle(images.size()+" items selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.image_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                if(item.getItemId() == R.id.delete_image){
                    Toast.makeText(MilestoneImageActivity.this, "delete", Toast.LENGTH_SHORT).show();
                    MilestonePresenter.getMilestonePresenter().deleteMilestoneImages(images, milestoneId);
                    mode.finish();
                    imageAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                images.clear();
                updateImageAdapter();
            }
        });

    }

    private void updateImageAdapter(){
        imageAdapter =  new ImageAdapter(MilestoneImageActivity.this, getImages());
        gridView.setAdapter(imageAdapter);
    }

    private ArrayList<MilestoneImage> getImages(){
        return MilestonePresenter.getMilestonePresenter().getImages(milestone.getMilestoneId());
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateImageAdapter();
    }
}
