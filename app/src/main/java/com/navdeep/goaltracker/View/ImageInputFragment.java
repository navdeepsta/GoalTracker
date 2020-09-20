package com.navdeep.goaltracker.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.POJOs.MilestoneImage;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.Utility.ImageAdapter;

import java.util.ArrayList;
import java.util.Calendar;


public class ImageInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GOAL_ID = "param1";
    private static final String ARG_MILESTONE_ID = "param2";
    private static final int REQUEST_CAMERA = 1;
    private int milestoneId, goalId;
    private GridView gridView;
    private Button addImage;
    private Milestone milestone;
    private ImageAdapter imageAdapter;
    private OnImageInputFragmentInteractionListener mListener;

    public ImageInputFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ImageInputFragment newInstance(int goalId, int milestoneId) {
        ImageInputFragment fragment = new ImageInputFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GOAL_ID, goalId);
        args.putInt(ARG_MILESTONE_ID, milestoneId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goalId = getArguments().getInt(ARG_GOAL_ID);
            milestoneId = getArguments().getInt(ARG_MILESTONE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_image, container, false);
        gridView = view.findViewById(R.id.grid_imageview);
        addImage = view.findViewById(R.id.add_image);
        milestone = getMilestone();
        updateImageAdapter();
        setListenerOnImageButton();
        setOnItemClickListenerOnGridView();
        return view;
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
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(MilestoneInputActivity.this, "You have no permission to access camera", Toast.LENGTH_LONG).show();
                    //Explanation
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){
                        Toast.makeText(getActivity(), "More than once asking for permission",  Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    }
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                mListener.onImageInputFragmentInteraction(imageId);
            }
        });
    }
    private void updateImageAdapter(){
        imageAdapter =  new ImageAdapter(getActivity(), getImages());
        gridView.setAdapter(imageAdapter);
    }

    private ArrayList<MilestoneImage> getImages(){
        return MilestonePresenter.getMilestonePresenter().getImages(milestone.getMilestoneId());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageInputFragmentInteractionListener) {
            mListener = (OnImageInputFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnImageInputFragmentInteractionListener {
        // TODO: Update argument type and name
        void onImageInputFragmentInteraction(int imageId);
    }
}
