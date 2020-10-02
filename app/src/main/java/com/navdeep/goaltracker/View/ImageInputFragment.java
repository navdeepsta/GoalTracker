package com.navdeep.goaltracker.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.navdeep.goaltracker.Adapters.MilestoneImageAdapter;
import com.navdeep.goaltracker.POJOs.Milestone;
import com.navdeep.goaltracker.POJOs.MilestoneImage;
import com.navdeep.goaltracker.Presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.Utility.GoalUtil;
import com.navdeep.goaltracker.Utility.ImageAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ImageInputFragment extends Fragment {
    private static final String ARG_GOAL_ID = "param1";
    private static final String ARG_MILESTONE_ID = "param2";
    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_PERMISSION = 2;
    private int milestoneId, goalId;
    private TextView dateTextView;
    private Button addImage;
    private Milestone milestone;
    private ImageAdapter imageAdapter;
    private MilestoneImageAdapter milestoneImageAdapter;
    private RecyclerView imageRecycler;
    private String currentPhotoPath;
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
        View view = inflater.inflate(R.layout.fragment_input_image_recycler, container, false);
        imageRecycler = view.findViewById(R.id.image_recycler);
        addImage = view.findViewById(R.id.add_image);
        dateTextView = view.findViewById(R.id.date);
        milestone = getMilestone();
        Calendar calendar = GoalUtil.getCalendarObject(milestone.getTime());
       String date = GoalUtil.dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)]+", "+calendar.get(Calendar.DATE)+" "
                +GoalUtil.months[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR);
       dateTextView.setText(date);
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
                askForCameraPermission();
            }
        });
    }

    private void askForCameraPermission() {
        //Check if the permission is granted. If granted, ask the user for camera permission. Else continue to take picture.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
        }else{
            dispatchTakePictureIntent();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(getActivity(), "Camera permission is required to take a picture", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.navdeep.goaltracker.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if(resultCode == RESULT_OK){
                File file = new File(currentPhotoPath);
                MilestoneImage image = new MilestoneImage(currentPhotoPath, Calendar.getInstance());
                MilestonePresenter.getMilestonePresenter().addImage(milestone.getMilestoneId(), image);
                updateImageAdapter();
               // Picasso.with(getActivity()).load(Uri.fromFile(file)).fit().centerCrop().into(image_camera_view);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
            }
        }
    }

    private void setOnItemClickListenerOnGridView() {
     /*   gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<MilestoneImage> images = getImages();
                int imageId =  images.get(position).getImageId();
                mListener.onImageInputFragmentInteraction(imageId);
            }
        });*/
    }

    private void updateImageAdapter(){
        milestoneImageAdapter =  new MilestoneImageAdapter(getImages(),getActivity(), milestoneId);
        imageRecycler.setAdapter(milestoneImageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        imageRecycler.setLayoutManager(layoutManager);
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
