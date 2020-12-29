/* @author : Navdeep Singh
*  
* */
package com.navdeep.goaltracker.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import com.navdeep.goaltracker.interfaces.GoalModelViewPresenter;
import com.navdeep.goaltracker.presenter.GoalPresenter;
import com.navdeep.goaltracker.R;

public class GoalActivity extends AppCompatActivity implements GoalModelViewPresenter.GoalView,
        GoalFragment.GoalFragmentListener, GoalInputFragment.GoalInputFragmentListener {

    private static GoalPresenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_recycler);
        goalPresenter = GoalPresenter.getGoalPresenter(this);
        createGoalFragment();

    }

    public void createGoalFragment() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GoalFragment goalFragment = GoalFragment.newInstance(null,null);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameContainer, goalFragment)
                .commit();
        hideKeypad();

    }

    private void hideKeypad() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createGoalInputFragment() {
        GoalInputFragment goalInputFragment = GoalInputFragment.newInstance(null,null);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameContainer, goalInputFragment)
                .commit();

    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.goal_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        goalPresenter.closeGoalTrackerDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        goalPresenter.initiateMilestones(); //update progress bar

    }

    @Override
    protected void onStart() {
        super.onStart();
        goalPresenter.createGoalTrackerDatabase();
        goalPresenter.initiateMilestones(); // update progress bar
        createGoalFragment();

    }
}
