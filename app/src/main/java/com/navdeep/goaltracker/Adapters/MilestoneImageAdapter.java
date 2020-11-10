package com.navdeep.goaltracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.navdeep.goaltracker.pojo.MilestoneImage;
import com.navdeep.goaltracker.presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.view.MilestoneInputActivity;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;

public class MilestoneImageAdapter extends RecyclerAdapter {
    private ArrayList<MilestoneImage> milestoneImages;
    private Context context;
    private boolean multiSelect = false;
    private ArrayList<MilestoneImage> selectedMilestoneImages;
    private ActionMode actionMode;
    private CardView cardView;
    private ImageView milestoneImageView;
    private TextView imageTime;
    private int milestoneId;

    public MilestoneImageAdapter(ArrayList<MilestoneImage> milestoneImages, Context context, int milestoneId) {
        this.milestoneImages = milestoneImages;
        this.context = context;
        this.milestoneId = milestoneId;
        selectedMilestoneImages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cardview, parent, false);
        return ViewHolder.getViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CardView cardView = holder.getCardView();
        findViewsFromCardView();
        final MilestoneImage milestoneImage = milestoneImages.get(position);
        setViewsOnCardView(milestoneImage);
        setListenersOnCardView(milestoneImage);

    }



    private void findViewsFromCardView() {
        milestoneImageView = cardView.findViewById(R.id.milestone_image);
        imageTime = cardView.findViewById(R.id.image_time);
    }

    private void setViewsOnCardView(MilestoneImage milestoneImage) {
        //
            File file = new File(milestoneImage.getImageUri());
            if (file.exists()) {
                Picasso.with(context).load(Uri.fromFile(file)).fit().centerCrop().into(milestoneImageView);
                milestoneImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageTime.setText(getTimeStamp(milestoneImage));

            } else {
               if(!milestoneImage.getImageUri().isEmpty()){

                    Picasso.with(context).load(Uri.parse(milestoneImage.getImageUri()))
                            .fit().centerCrop()
                            .into(milestoneImageView);
                    imageTime.setText(getTimeStamp(milestoneImage));
                   Log.d("File Status", "File has been deleted in the gallery");
                   Log.d("File", milestoneImage.getImageUri());
               }

            }
        }

    private String getTimeStamp(MilestoneImage milestoneImage) {
        Calendar calendar = milestoneImage.getCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
       return  hour + ":" + minutes + ":" + seconds;
    }


    private void setListenersOnCardView(MilestoneImage milestoneImage) {
        setOnClick(milestoneImage);
        setOnLongClick(milestoneImage);
    }

    private void setOnClick(final MilestoneImage milestoneImage) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(multiSelect){
                    changeBackgroundColorOfSelectedItems(view, milestoneImage);
                    actionMode.setTitle(selectedMilestoneImages.size()+" selected");
                }else {
                    openImage(milestoneImage);
                }
            }
        });

    }

    private void changeBackgroundColorOfSelectedItems(View view, MilestoneImage milestoneImage) {
        selectItem(milestoneImage);
        if (selectedMilestoneImages.contains(milestoneImage)) {
            view.setBackgroundColor( context.getResources().getColor(R.color.baselineErrorColor));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
    }

    private void openImage(MilestoneImage milestoneImage) {
        MilestoneInputActivity milestoneInputActivity = (MilestoneInputActivity) context;
        milestoneInputActivity.onImageInputFragmentInteraction(milestoneImage.getImageId());
    }

    private void setOnLongClick(final MilestoneImage milestoneImage) {
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!multiSelect) {
                    actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(milestoneImage);
                    view.setBackgroundColor(context.getResources().getColor(R.color.baselineErrorColor));
                    actionMode.setTitle(selectedMilestoneImages.size() + " selected");
                    return true;
                }
                return false;
            }

        });
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
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
            if(item.getItemId() == R.id.delete_image) {
                MilestonePresenter.getMilestonePresenter().deleteMilestoneImages(selectedMilestoneImages,milestoneId);
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedMilestoneImages.clear();
            MilestoneInputActivity activity = (MilestoneInputActivity) context;
           activity.createFragment();

        }
    };

    void selectItem(MilestoneImage milestoneImage) {
        if (multiSelect) {
            if (selectedMilestoneImages.contains(milestoneImage)) {
                selectedMilestoneImages.remove(milestoneImage);
                //    cardView.setBackgroundColor(Color.WHITE);
            } else {
                selectedMilestoneImages.add(milestoneImage);
                //  cardView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    @Override
    public int getItemCount() {
        return milestoneImages.size();
    }

}
