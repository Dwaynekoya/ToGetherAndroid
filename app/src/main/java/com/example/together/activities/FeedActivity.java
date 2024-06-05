package com.example.together.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.together.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.List;
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
        // search for users current user follows
        HashSet<User> following = DBUsers.searchFollowing(Utils.loggedInUser);
        if (following!=null) Utils.loggedInUser.setFollowing(following);
        // add their finished tasks to the set
        for (User user: Utils.loggedInUser.getFollowing()){
            String json = DBTask.getTasks(user.getId(), true);
            List<Task> tasksFromUser = Utils.parseTasks(json);
            assert tasksFromUser != null;
            for (Task task: tasksFromUser) task.setOwner(user);
            tasksFromFollowing.addAll(tasksFromUser);
        }
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
            View taskView = inflater.inflate(R.layout.task_item, linearLayout, false);

            ImageView imageView = taskView.findViewById(R.id.task_image);
            TextView userLabel = taskView.findViewById(R.id.task_user);
            TextView nameLabel = taskView.findViewById(R.id.task_name);
            TextView descriptionLabel = taskView.findViewById(R.id.task_description);

            // TODO: change image urls in db
            // image loader
            Glide.with(context)
                    .load(task.getImage())
                    .into(imageView);

            userLabel.setText(task.getOwner().toString());
            nameLabel.setText(task.getName());
            descriptionLabel.setText(task.getInfo());

            // Set top and bottom margin
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 20, 0, 20);
            taskView.setLayoutParams(layoutParams);

            linearLayout.addView(taskView);
        }
    }

}
