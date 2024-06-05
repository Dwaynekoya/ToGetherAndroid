package com.example.together.view;

import android.app.Activity;
import android.content.Intent;

import com.example.together.MainActivity;
import com.example.together.activities.FeedActivity;
import com.example.together.activities.GroupsActivity;
import com.example.together.activities.NewGroupActivity;
import com.example.together.activities.NewTaskActivity;
import com.example.together.activities.ProfileActivity;
import com.example.together.activities.SettingsActivity;
import com.example.together.activities.TasklistActivity;

public class ViewSwitcher {

    public static void switchView(Activity activity, View view) {
        Intent intent = null;
        switch (view) {
            case LOGIN:
                intent = new Intent(activity, MainActivity.class);
                break;
            case TASKLIST:
                intent = new Intent(activity, TasklistActivity.class);
                break;
            case FEED:
                intent = new Intent(activity, FeedActivity.class);
                break;
            case NEWTASK:
                intent = new Intent(activity, NewTaskActivity.class);
                break;
            case GROUPS:
                intent = new Intent(activity, GroupsActivity.class);
                break;
            case PROFILE:
                intent = new Intent(activity, ProfileActivity.class);
                break;
            case SETTINGS:
                intent = new Intent(activity, SettingsActivity.class);
                break;
            case NEWGROUP:
                intent = new Intent(activity, NewGroupActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown view: " + view);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public enum View {
        LOGIN,
        TASKLIST,
        SETTINGS,
        PROFILE,
        TASK,
        FEED,
        NEWTASK,
        GROUPS,
        NEWGROUP
        // TODO: add all views
    }
}
