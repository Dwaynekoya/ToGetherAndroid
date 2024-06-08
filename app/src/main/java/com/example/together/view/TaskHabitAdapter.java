package com.example.together.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.together.R;
import com.example.together.dboperations.DBTask;
import com.example.together.model.Habit;
import com.example.together.model.Task;

import java.util.List;

public class TaskHabitAdapter extends ArrayAdapter<Object> {

    public TaskHabitAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView nameLabel = convertView.findViewById(R.id.nameLabel);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        if (item instanceof Task) {
            Task task = (Task) item;
            nameLabel.setText(task.getName());
            checkBox.setChecked(task.isFinished());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setFinished(isChecked);
                task.setImage("http://localhost/ToGether/phpusers/uploads/placeholder.png");
                DBTask.finishTask(task);
            });
            deleteButton.setOnClickListener(v -> {
                DBTask.deleteTask(task);
                remove(task);
            });
        } else if (item instanceof Habit) {
            Habit habit = (Habit) item;
            nameLabel.setText(habit.getName());
            checkBox.setChecked(habit.isFinished());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                habit.setFinished(isChecked);
                DBTask.updateTask(habit);
            });
            deleteButton.setOnClickListener(v -> {
                DBTask.deleteTask(habit);
                remove(habit);
            });
        }

        return convertView;
    }

}
