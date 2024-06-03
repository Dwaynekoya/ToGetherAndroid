package com.example.together.activities;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.together.R;
import com.example.together.dboperations.TaskFetcher;
import com.example.together.view.TaskHabitAdapter;

import java.util.ArrayList;

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
    }
}
