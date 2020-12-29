package com.navdeep.goaltracker.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.adapters.GoalAdapter;
import com.navdeep.goaltracker.adapters.RecyclerAdapter;
import com.navdeep.goaltracker.presenter.GoalPresenter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {
    private RecyclerView goalRecycler;
    private GoalFragmentListener goalFragmentListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance(String param1, String param2) {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_goal, container, false);
        goalRecycler = v.findViewById(R.id.goal_recycler);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        FloatingActionButton floatingActionAddGoal = v.findViewById(R.id.floatingActionAddGoal);
        floatingActionAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goalFragmentListener.createGoalInputFragment();
            }
        });
        updateGoalRecyclerViewAdapter();
        return  v;
    }

    public void updateGoalRecyclerViewAdapter(){
        RecyclerAdapter goalAdapter = new GoalAdapter(GoalPresenter.getGoalPresenter().getGoals(), getContext());
        goalRecycler.setAdapter(goalAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        goalRecycler.setLayoutManager(layoutManager);
    }

    public interface GoalFragmentListener {
        void createGoalInputFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GoalFragmentListener) {
            goalFragmentListener = (GoalFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()+" must implement GoalFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        goalFragmentListener = null;
    }
}