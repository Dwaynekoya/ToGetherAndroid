package com.example.together.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.DBUsers;
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
        populateScrollViewWithTasks(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void fetchFollowingTasks() {
        // TODO: fetch following tasks

    }

    private void populateScrollViewWithTasks(Context context) {
        LinearLayout linearLayout = findViewById(R.id.feedLinearLayout);
        linearLayout.removeAllViews();

        if (tasksFromFollowing.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No tasks from users you follow yet...");
            linearLayout.addView(textView);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        for (Task task : tasksFromFollowing) {
            // TODO: taskbox
            View taskView = inflater.inflate(R.layout.task_item, linearLayout, false);

            ImageView imageView = taskView.findViewById(R.id.task_image);
            TextView userLabel = taskView.findViewById(R.id.task_user);
            TextView nameLabel = taskView.findViewById(R.id.task_name);
            TextView descriptionLabel = taskView.findViewById(R.id.task_description);

            // image loader
            Glide.with(context)
                    .load(task.getImage())
                    .into(imageView);

            userLabel.setText(task.getOwner().toString());
            nameLabel.setText(task.getName());
            descriptionLabel.setText(task.getInfo());

            linearLayout.addView(taskView);
        }
    }

}
