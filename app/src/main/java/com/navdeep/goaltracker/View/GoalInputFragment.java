package com.navdeep.goaltracker.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.navdeep.goaltracker.R;
import com.navdeep.goaltracker.presenter.GoalPresenter;
import com.navdeep.goaltracker.utility.GoalTime;

import java.util.Calendar;
import java.util.Objects;

public class GoalInputFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnClickListener {
    GoalPresenter goalPresenter;
    private GoalInputFragmentListener goalInputFragmentListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static int GOAL_ID_DEFAULT = 0;
    private EditText goalName;
    private Spinner years, months, days;
    private RadioGroup category;
    private RadioButton radioButton;
    private Button saveGoal, cancel;

    public GoalInputFragment() {
        goalPresenter = GoalPresenter.getGoalPresenter();
    }


    public static GoalInputFragment newInstance(String param1, String param2) {
        GoalInputFragment fragment = new GoalInputFragment();
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
        View view = inflater.inflate(R.layout.fragment_goal_input, container, false);
        findViewsFromLayout(view);
        displayGoalDuration(new GoalTime());

        setListeners();

        return view;
    }

    private void findViewsFromLayout(View view) {
        goalName = view.findViewById(R.id.goalName);
        years = view.findViewById(R.id.years);
        months =  view.findViewById(R.id.months);
        days =  view.findViewById(R.id.days);
        category = view.findViewById(R.id.category);
        radioButton = view.findViewById(category.getCheckedRadioButtonId());
        saveGoal = view.findViewById(R.id.saveGoal);
        cancel = view.findViewById(R.id.cancel);
    }

    private void setListeners() {
        setListenerOnSpinner();
        setListenerOnButton();
        setListenerOnRadioGroup();
    }

    private void setListenerOnSpinner() {
        years.setOnItemSelectedListener(this);
        months.setOnItemSelectedListener(this);
        days.setOnItemSelectedListener(this);

    }

    private void setListenerOnButton() {
        saveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goalName.getText().toString().length()<1){
                    Toast.makeText(getActivity(),"Please enter goal name",Toast.LENGTH_LONG).show();
                }else {
                    int duration = goalPresenter.calculateGoalDuration();
                    goalPresenter.createGoal(GOAL_ID_DEFAULT, radioButton.getText().toString(), goalName.getText().toString(), Calendar.getInstance().getTime().toString(), duration, 0);
                    goalInputFragmentListener.createGoalFragment();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalInputFragmentListener.createGoalFragment();
            }
        });

    }

    private void setListenerOnRadioGroup() {
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)category.findViewById(i);
                if(checkedRadioButton.isChecked()) {
                    radioButton.setText(checkedRadioButton.getText());
                }
            }
        });
    }

    public void displayGoalDuration(GoalTime goalTime) {
        years.setAdapter(getGoalSubDurationAdapter(goalTime.getYears()));
        months.setAdapter(getGoalSubDurationAdapter(goalTime.getMonths()));
        days.setAdapter(getGoalSubDurationAdapter(goalTime.getDays()));

    }


    private ArrayAdapter<String> getGoalSubDurationAdapter(String[] goalTimeUnit){
        ArrayAdapter<String> goalSubDuration = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, goalTimeUnit);

        goalSubDuration.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        return goalSubDuration;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId()){
            case R.id.years:
                String y = (String)parent.getItemAtPosition(position);
                int year = Integer.parseInt(y);
                goalPresenter.setYear(year);
                break;
            case R.id.months:
                String m = (String)parent.getItemAtPosition(position);
                int month = Integer.parseInt(m);
                goalPresenter.setMonth(month);
                break;
            case R.id.days:
                String d = (String)parent.getItemAtPosition(position);
                int day = Integer.parseInt(d);
                goalPresenter.setDay(day);
                break;
            default:
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == category.getCheckedRadioButtonId()) {
            radioButton = view.findViewById(view.getId());
        }
    }


    public interface GoalInputFragmentListener {
        void createGoalFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GoalFragment.GoalFragmentListener) {
            goalInputFragmentListener = (GoalInputFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()+" must implement GoalInputFragmentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        goalInputFragmentListener = null;
    }
}