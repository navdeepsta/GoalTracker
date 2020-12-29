package com.navdeep.goaltracker.view;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.navdeep.goaltracker.pojo.Milestone;
import com.navdeep.goaltracker.presenter.MilestonePresenter;
import com.navdeep.goaltracker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteFragment.OnNoteFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GOAL_ID = "param1";
    private static final String ARG_MILESTONE_ID = "param2";

    // TODO: Rename and change types of parameters
    private int goalId;
    private int milestoneId;
    private EditText titleTextView;
    private EditText descriptionTextView;
    private Milestone milestone;
    private OnNoteFragmentInteractionListener mListener;
    public NoteFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(int goalId, int milestoneId) {
        NoteFragment fragment = new NoteFragment();
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
         View view = inflater.inflate(R.layout.fragment_note, container, false);
         titleTextView= view.findViewById(R.id.noteTitle);
         descriptionTextView = view.findViewById(R.id.noteDescription);
         milestone = getMilestone();
         titleTextView.setText(milestone.getTitle());
         descriptionTextView.setText(milestone.getDescription());
         setListenerOnTitleEditText();
         setListenerOnDescriptionEditText();
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
    // TODO: Rename method, update argument and hook method into UI event
    private void setListenerOnTitleEditText() {
        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                milestone.setTitle(s.toString());
                updateMilestone();
            }
        });
    }

    private void setListenerOnDescriptionEditText() {
        descriptionTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                milestone.setDescription(s.toString());
                updateMilestone();
            }
        });
    }

    private void updateMilestone(){
        mListener.onNoteFragmentInteraction(milestone);
    }


    public void updateNoteFragmentChildViews(Milestone milestone){
        titleTextView.setText(milestone.getTitle());
        descriptionTextView.setText(milestone.getDescription());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteFragmentInteractionListener) {
            mListener = (OnNoteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MilestonePresenter.getMilestonePresenter().updateMilestone(milestone);
        mListener = null;
        Log.i("Milestone updated onDet", milestone.getTitle());
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
    public interface OnNoteFragmentInteractionListener {
        void onNoteFragmentInteraction(Milestone milestone);
    }
}
