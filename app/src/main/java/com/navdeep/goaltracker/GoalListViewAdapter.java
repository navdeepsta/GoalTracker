package com.navdeep.goaltracker;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.navdeep.goaltracker.POJOs.Goal;
import java.util.ArrayList;

public class GoalListViewAdapter extends BaseAdapter {
    private ArrayList<Goal> goals;
    private Context context;

   public GoalListViewAdapter(Context context, ArrayList<Goal> goals){
       this.context = context;
       this.goals = goals;
   }
    @Override
    public int getCount() {
        return goals.size();
    }

    @Override
    public Object getItem(int position) {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.goal_list_hold,null);
        setConvertViewWidgets(position, convertView);
        return convertView;
    }

    private void setConvertViewWidgets(int position, View convertView) {
        TextView goalName = convertView.findViewById(R.id.goalName);
        TextView time = convertView.findViewById(R.id.time);
        CircularProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        TextView progressBarText = convertView.findViewById(R.id.progressBarText);
        Goal goal = goals.get(position);
        String progressText = goal.getGoalProgress()+"%";
        progressBar.setProgress(goal.getGoalProgress());
        progressBarText.setText(progressText);
        goalName.setText(goal.getGoalName());
        time.setText(goal.getGoalStartTimeWithFormat());
    }
}
