package com.navdeep.goaltracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.POJOs.MilestoneImage;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private MilestoneImage[] bitmaps;
    public ImageAdapter(Context context, ArrayList<MilestoneImage> bitmapList){
        Log.i("Image","ImageAddapter");
        this.context = context;
        bitmaps = new MilestoneImage[bitmapList.size()];
        for(int i=0; i<bitmaps.length;++i){
         bitmaps[i] = bitmapList.get(i);
        }
    }
    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public Object getItem(int position) {
        return bitmaps[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView( context);
        imageView.setImageBitmap(bitmaps[position].getBitmap());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setCropToPadding(true);
        }
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        imageView.setBackgroundResource(R.drawable.imagevew_border);


        return imageView;
    }
}
