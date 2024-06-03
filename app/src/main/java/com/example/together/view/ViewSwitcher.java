package com.example.together.view;

import android.app.Activity;
import android.content.Intent;

import com.example.together.MainActivity;
import com.example.together.R;
import com.example.together.activities.FeedActivity;

public class ViewSwitcher {

    public static void switchView(Activity activity, View view) {
        Intent intent = null;
        switch (view) {
            case LOGIN:
                intent = new Intent(activity, MainActivity.class);
                break;
            case TASKLIST:
                //intent = new Intent(activity, TaskListActivity.class);
                break;
            // Add other cases as needed
            case FEED:
                intent = new Intent(activity, FeedActivity.class);
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
        FEED
        // TODO: add all views
    }
}
