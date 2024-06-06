package com.example.together.activities;

import android.app.Activity;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTextText, editTextDate, editTextTextMultiLine, daysText;
    private CheckBox checkBox;
    private Button buttonSave, buttonCancel;
    private LinearLayout habitBox;
    private TextView textViewRequiredFields;

    private Task task;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtask);

        editTextText = findViewById(R.id.edittextGroupName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTextMultiLine = findViewById(R.id.editTextInfo);
        checkBox = findViewById(R.id.checkBox);
        buttonSave = findViewById(R.id.button8);
        buttonCancel = findViewById(R.id.button9);
        habitBox = findViewById(R.id.habitBox);
        daysText = findViewById(R.id.daysText);
        textViewRequiredFields = findViewById(R.id.textViewRequiredFields);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> habitBox.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        buttonSave.setText("Edit");
        buttonSave.setOnClickListener(v -> saveTask());
        buttonCancel.setOnClickListener(v -> cancelEdit());
        buttonCancel.setVisibility(View.GONE);

        // Retrieve the task data from intent extras
        task = getIntent().getParcelableExtra("task");

        if (task != null) {
            editTextText.setText(task.getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            if (task.getDate() != null) {
                editTextDate.setText(dateFormat.format(task.getDate()));
            }
            editTextTextMultiLine.setText(task.getInfo());
            checkBox.setChecked(task instanceof Habit);
            if (task instanceof Habit) {
                daysText.setText(String.valueOf(((Habit) task).getRepetition()));
            }
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void saveTask() {
        String taskName = editTextText.getText().toString();
        String date = editTextDate.getText().toString();
        String info = editTextTextMultiLine.getText().toString();
        boolean isHabit = checkBox.isChecked();

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

        task.setName(taskName);
        task.setDate(sqlDate);
        task.setInfo(info);

        if (isHabit) {
            int repeatInterval = Integer.parseInt(daysText.getText().toString());
            if (task instanceof Habit) {
                ((Habit) task).setRepetition(repeatInterval);
            } else {
                Habit newHabit = new Habit(task, repeatInterval);
                task = newHabit;
            }
        }


        DBTask.updateTask(task);
        Toast.makeText(this.getApplicationContext(), "Updated!", Toast.LENGTH_SHORT);
        // Go back to the previous activity
        //finish();
    }

    private void cancelEdit() {
        // Go back to the previous activity ?
        // finish();
    }
}
