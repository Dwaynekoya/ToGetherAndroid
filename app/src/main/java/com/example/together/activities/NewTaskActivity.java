package com.example.together.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.together.R;
import com.example.together.dboperations.DBTask;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTaskActivity extends Activity {

    private EditText editTextText, editTextDate, editTextTextMultiLine, daysText;
    private CheckBox checkBox;
    private Button buttonCreate, buttonCancel;
    private LinearLayout habitBox;
    private TextView textViewRequiredFields;

    private String taskName, date, info;
    private boolean isHabit = false;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtask);

        editTextText = findViewById(R.id.editTextText);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        checkBox = findViewById(R.id.checkBox);
        buttonCreate = findViewById(R.id.button8);
        buttonCancel = findViewById(R.id.button9);
        habitBox = findViewById(R.id.habitBox);
        daysText = findViewById(R.id.daysText);
        //TODO: textViewRequiredFields = findViewById(R.id.textViewRequiredFields);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> habitBox.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        buttonCreate.setOnClickListener(v -> createTask());
        buttonCancel.setOnClickListener(v -> cancelTask());

        // TODO: Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        /*bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Handle home action
                    return true;
                case R.id.navigation_group:
                    // Handle group action
                    return true;
                case R.id.navigation_list:
                    // Handle list action
                    return true;
                case R.id.navigation_settings:
                    // Handle settings action
                    return true;
            }
            return false;
        });*/
    }

    private void createTask() {
        taskName = editTextText.getText().toString();
        date = editTextDate.getText().toString();
        info = editTextTextMultiLine.getText().toString();
        isHabit = checkBox.isChecked();

        if (taskName.isEmpty()) {
            textViewRequiredFields.setVisibility(View.VISIBLE);
            return;
        } else {
            textViewRequiredFields.setVisibility(View.GONE);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        java.sql.Date sqlDate = null;
        try {
            if (!date.isEmpty()) {
                Date parsedDate = simpleDateFormat.parse(date);
                sqlDate = new java.sql.Date(parsedDate.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task newTask = new Task(0, taskName, sqlDate, info, false, false, "");
        if (isHabit) {
            int repeatInterval = Integer.parseInt(daysText.getText().toString());
            Habit newHabit = new Habit(newTask, repeatInterval);
            DBTask.addTask(newHabit);
        } else {
            DBTask.addTask(newTask);
        }

        // goes back to previous view
        finish();
    }

    private void cancelTask() {
        // goes back to previous view
        finish();
    }
}
