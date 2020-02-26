package com.navdeep.goaltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MilestoneInputActivity extends AppCompatActivity implements MilestoneModelViewPresenter.MilestoneInputView{
int pos;
EditText description;
    MilestoneModelViewPresenter.MilestonePresenter milestonePresenter;
    ArrayList<Milestone> milestones;
    Milestone milestone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_input);
        pos = getIntent().getIntExtra("position", 0);

        milestonePresenter = new MilestonePresenter(this);
         milestones = MilestoneActivity.milestonePresenter.getMilestoneList();
        milestone = milestones.get(pos);

        Toast.makeText(this, milestone.getDescription(), Toast.LENGTH_LONG).show();
        description = findViewById(R.id.description);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                milestone.setDescription(s.toString());
            }
        });
        milestonePresenter.updateMilestone();
    }

    @Override
    public void showDescription() {
description.setText(milestone.getDescription());


    }
}
