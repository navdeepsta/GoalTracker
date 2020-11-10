package com.navdeep.goaltracker.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.navdeep.goaltracker.pojo.MilestoneImage;
import com.navdeep.goaltracker.presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

public class ImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MILESTONE_ID = "milestoneid";
    private static final String ARG_IMAGE_ID = "imageid";
    private int milestoneId, imageId;

    private OnFragmentInteractionListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(int milestoneId, int imageId) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MILESTONE_ID, milestoneId);
        args.putInt(ARG_IMAGE_ID, milestoneId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            milestoneId = getArguments().getInt(ARG_MILESTONE_ID);
            imageId = getArguments().getInt(ARG_IMAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        PhotoView imageView = view.findViewById(R.id.milestone_image);
        ArrayList<MilestoneImage> images = MilestonePresenter.getMilestonePresenter().getImages(milestoneId);
        for(MilestoneImage image : images) {
            if(image.getImageId() == imageId) {
                imageView.setTag(image.getImageUri());
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
