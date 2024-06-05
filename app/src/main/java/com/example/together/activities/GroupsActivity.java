package com.example.together.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.TaskHabitAdapter;
import com.example.together.view.ViewSwitcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupsActivity extends AppCompatActivity {
    private LinearLayout container;


    private Button newGroupButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        container = findViewById(R.id.feedLinearLayout);

        fetchGroupTasks();
        displayGroups(Utils.loggedInUser.getGroups());

        newGroupButton = findViewById(R.id.buttonNew);
        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSwitcher.switchView(GroupsActivity.this, ViewSwitcher.View.NEWGROUP);
            }
        });
        newGroupButton.setVisibility(View.VISIBLE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void fetchGroupTasks() {
        HashSet<Group> groups = DBGroup.searchGroupsFromMember(Utils.loggedInUser);
        if (groups!=null) Utils.loggedInUser.setGroups(groups);
        for (Group group : Utils.loggedInUser.getGroups()) {
            String json = DBGroup.getTasks(group.getId());
            tasksHabitsFromJSON(json, group);
        }
    }

    private void displayGroups(Set<Group> groups) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (groups == null) {
            TextView textView = new TextView(this);
            textView.setText("You are not in any groups");
            container.addView(textView);
            return;
        }

        for (Group group : groups) {
            View groupView = inflater.inflate(R.layout.group_item, container, false);

            TextView groupNameLabel = groupView.findViewById(R.id.group_name);
            ListView tasklist = groupView.findViewById(R.id.grouptasklist);
            Button addTaskButton = groupView.findViewById(R.id.add_task_button);

            groupNameLabel.setText(group.getName());

            //display tasks
            ArrayList tasksFromGroup = new ArrayList<>(group.getSharedTasks());
            tasksFromGroup.addAll(group.getSharedHabits());

            TaskHabitAdapter adapter = new TaskHabitAdapter(this, tasksFromGroup);
            tasklist.setAdapter(adapter);

            tasklist.setOnItemClickListener((parent, view, position, id) -> {
                Task clickedTask = (Task) adapter.getItem(position);
                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("task", clickedTask);
                startActivity(intent);
            });

            addTaskButton.setOnClickListener(v -> addGroupTask(group));

            container.addView(groupView);
        }
    }


    private void addGroupTask(Group group) {
        Utils.groupNewTask = group;
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Added group task", Toast.LENGTH_SHORT);
    }

    private void tasksHabitsFromJSON(String json, Group group) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        Set<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<Set<Task>>() {}.getType());
        Set<Habit> fetchedHabits = gson.fromJson(jsonObject.getAsJsonArray("habits"), new TypeToken<Set<Habit>>() {}.getType());

        //Manually checking which tasks are habits, so they can be shown in the habits list and not in the tasks
        Map<Integer, Habit> habitMap = new HashMap<>();
        for (Habit habit : fetchedHabits) {
            habitMap.put(habit.getId(), habit);
        }
        Iterator<Task> taskIterator = fetchedTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            if (habitMap.containsKey(task.getId())) {
                Habit habit = habitMap.get(task.getId());
                //System.out.println("Matching Task and Habit ID: " + task.getId());

                // Update habit with matching task attributes
                habit.setName(task.getName());
                habit.setDate(task.getDate());
                habit.setInfo(task.getInfo());

                taskIterator.remove();
            }
        }

        group.setSharedTasks(new HashSet<>(fetchedTasks));
        group.setSharedHabits(new HashSet<>(fetchedHabits));
    }
}
