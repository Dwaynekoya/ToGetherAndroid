package com.example.together.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

        adapter = new TaskHabitAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        TaskFetcher taskFetcher = new TaskFetcher(1, adapter);
        taskFetcher.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked task
                Task clickedTask = (Task) adapter.getItem(position);

                launchEditTaskActivity(clickedTask);
            }
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
