package com.navdeep.goaltracker.utility;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.navdeep.goaltracker.pojo.MilestoneImage;
import com.navdeep.goaltracker.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private MilestoneImage[] images;
    public ImageAdapter(Context context, ArrayList<MilestoneImage> bitmapList){
        Log.i("Image","ImageAddapter");
        this.context = context;
        images = new MilestoneImage[bitmapList.size()];
        for(int i=0; i<images.length;++i){
         images[i] = bitmapList.get(i);
        }
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView( context);
        File file = new File(images[position].getImageUri());
        if(file.exists()) {
            Picasso.with(context).load(Uri.fromFile(file)).fit().centerCrop().into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.setCropToPadding(true);
            }
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setBackgroundResource(R.drawable.imagevew_border);

        }
        else {
            Log.d("File Status", "File has been deleted in the gallery");
        }
        return imageView;
    }
}
