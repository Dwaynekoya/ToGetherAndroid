package com.example.together.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.TaskFetcher;
import com.example.together.model.Task;
import com.example.together.view.TaskHabitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TasklistActivity extends AppCompatActivity {

    private ListView listView;
    private TaskHabitAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist);

        listView = findViewById(R.id.taskListView);

        adapter = new TaskHabitAdapter(this, new ArrayList<>()); //TODO: finish task photo uploading
        listView.setAdapter(adapter);

        TaskFetcher taskFetcher = new TaskFetcher(Utils.loggedInUser.getId(), adapter);
        taskFetcher.start();

        //to be able to trigger this, focusable on button and checkbox must be off
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Task clickedTask = (Task) adapter.getItem(position);
            launchEditTaskActivity(clickedTask);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }
    private void launchEditTaskActivity(Task task) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }
}
