package com.example.together.activities;

import static com.example.together.view.TaskHabitAdapter.PICK_IMAGE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.dboperations.TaskFetcher;
import com.example.together.model.Task;
import com.example.together.view.TaskHabitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TasklistActivity extends AppCompatActivity {

    private ListView listView;
    private TaskHabitAdapter adapter;
    public static Task taskToFinish;


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
        //can't pass a null date in intent, so we temporarily change it
        System.out.println(task);
        if (task.getDate()==null){
            Date zeroDate = Date.valueOf("1970-01-01");
            task.setDate(zeroDate);
        }
        intent.putExtra("task", task);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();

                taskToFinish.setImage(imageUri.toString());

                new PhotoUploader(this, imageUri, taskToFinish).execute();

            }
        }
    }
}
