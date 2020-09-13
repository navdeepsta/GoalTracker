package com.navdeep.goaltracker.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.navdeep.goaltracker.POJOs.MilestoneImage;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    public static final String MILESTONE_ID = "milestoneid";
    public static final String IMAGE_ID = "imageid";
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.milestone_image);

        int milestoneId = getIntent().getIntExtra(MILESTONE_ID, 0);
        int imageId = getIntent().getIntExtra(IMAGE_ID, 0);
        ArrayList<MilestoneImage> images = MilestonePresenter.getMilestonePresenter().getImages(milestoneId);

        for(MilestoneImage image : images) {
            if(image.getImageId() == imageId) {
                imageView.setImageBitmap(image.getBitmap());
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
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    }
}
