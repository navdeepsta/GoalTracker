package com.navdeep.goaltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.navdeep.goaltracker.pojo.MilestoneImage;
import com.navdeep.goaltracker.presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    public static final String MILESTONE_ID = "milestoneid";
    public static final String IMAGE_ID = "imageid";
    private PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        imageView = (PhotoView) findViewById(R.id.milestone_image);

        int milestoneId = getIntent().getIntExtra(MILESTONE_ID, 0);
        int imageId = getIntent().getIntExtra(IMAGE_ID, 0);
        ArrayList<MilestoneImage> images = MilestonePresenter.getMilestonePresenter().getImages(milestoneId);

        for(MilestoneImage image : images) {
            if(image.getImageId() == imageId) {
                File file = new File(image.getImageUri());
                if(file.exists()) {
                    Picasso.with(ImageActivity.this).load(Uri.fromFile(file)).fit().centerCrop().into(imageView);
                }else{
                    if(!image.getImageUri().isEmpty()){
                        Picasso.with(this).load(Uri.parse(image.getImageUri())).fit().centerCrop().into(imageView);
                    }
                }
            }
        }

   imageView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

       }
   });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    }
}
