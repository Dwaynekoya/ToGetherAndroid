package com.example.together.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.model.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class FeedActivity extends AppCompatActivity {

    private Set<Task> tasksFromFollowing = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        fetchFollowingTasks();
        populateScrollViewWithTasks();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void fetchFollowingTasks() {
        // TODO: fetch following tasks
    }

    private void populateScrollViewWithTasks() {
        LinearLayout linearLayout = findViewById(R.id.feedLinearLayout);
        linearLayout.removeAllViews();

        if (tasksFromFollowing.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No tasks from users you follow yet...");
            linearLayout.addView(textView);
        } else {
            for (Task task : tasksFromFollowing) {
                // TODO: taskbox
                TextView textView = new TextView(this);
                textView.setText(task.getName() + ": " + task.getInfo());
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));


                linearLayout.addView(textView);
            }
        }
    }
}
