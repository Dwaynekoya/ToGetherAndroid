package com.example.together.activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.DateFilter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    private EditText editTextText, editTextDate, editTextTextMultiLine, daysText;
    private CheckBox checkBox;
    private Button buttonCreate;
    private LinearLayout habitBox;
    private TextView textViewRequiredFields;

    private String taskName, date, info;
    private boolean isHabit = false;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtask);

        editTextText = findViewById(R.id.edittextGroupName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTextMultiLine = findViewById(R.id.editTextInfo);
        checkBox = findViewById(R.id.checkBox);
        buttonCreate = findViewById(R.id.button8);
        habitBox = findViewById(R.id.habitBox);
        daysText = findViewById(R.id.daysText);
        textViewRequiredFields = findViewById(R.id.textViewRequiredFields);

        editTextDate.setFilters(new InputFilter[] { new DateFilter() });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> habitBox.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        buttonCreate.setOnClickListener(v -> createTask());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
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

        Task newTask = new Task(0, taskName, sqlDate, info, false, "");
        if (isHabit) {
            int repeatInterval = Integer.parseInt(daysText.getText().toString());
            Habit newHabit = new Habit(newTask, repeatInterval);
            DBTask.addTask(newHabit);
        } else {
            DBTask.addTask(newTask);

        }

        Toast.makeText(this.getApplicationContext(), "Task created succesfully!", Toast.LENGTH_SHORT);
    }
}
