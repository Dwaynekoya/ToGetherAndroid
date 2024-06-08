package com.example.together.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBUsers;
import com.example.together.model.Group;
import com.example.together.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListActivity extends AppCompatActivity {

    private ListView taskListView;
    private Set<Group> groupSet = new HashSet<>();
    private Set<User> userSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist);

        taskListView = findViewById(R.id.taskListView);
        taskListView.setBackground(null);

        Intent intent = getIntent();
        String listType = intent.getStringExtra("listType");

        if (listType != null && listType.equals("groups")) {
            groupSet = fetchGroups();
            List<String> groupNames = new ArrayList<>();
            for (Group group : groupSet) {
                groupNames.add(group.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupNames);
            taskListView.setAdapter(adapter);
            taskListView.setOnItemClickListener((parent, view, position, id) -> showGroup(groupSet, position));
        } else {
            userSet = fetchUsers();
            removeLoggedInUser();
            List<String> userNames = new ArrayList<>();
            for (User user : userSet) {
                userNames.add(user.getUsername());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
            taskListView.setAdapter(adapter);
            taskListView.setOnItemClickListener((parent, view, position, id) -> showUser(userSet, position));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void removeLoggedInUser() {
        for (User user: userSet){
            if (user.equals(Utils.loggedInUser)) userSet.remove(user);
        }
    }

    private Set<Group> fetchGroups() {
        return DBGroup.searchGroupsFromMember(Utils.loggedInUser);
    }

    private Set<User> fetchUsers() {
        return DBUsers.searchFollowing(Utils.loggedInUser);
    }

    private void showGroup(Set<Group> groups, int position) {
        Group group = new ArrayList<>(groups).get(position);
        // TODO: show group
    }

    private void showUser(Set<User> users, int position) {
        User user = new ArrayList<>(users).get(position);
        // TODO: show user
    }
}
