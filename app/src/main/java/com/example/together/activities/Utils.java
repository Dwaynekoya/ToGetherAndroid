package com.example.together.activities;


import android.app.Activity;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.example.together.R;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Task;
import com.example.together.model.User;
import com.example.together.view.ViewSwitcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.util.List;


public class Utils {
    public static User loggedInUser;
    public static User selectedUser; //for popups
    public static Group selectedGroup; //for popups
    public static Group groupNewTask; //to add a new task for a group
    //public static View previousView; //to go back to after closing add task view
    /**
     * Used to determine if the user has inputted all the data necessary to create an object
     * @param strings: fields that the user must fill
     * @return false if one or more of the fields have not been filled, true if all have assigned values
     */
    public static boolean checkDataValidity(List<String> strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }



    /**
     * Sets a character limit in a given text input
     * @param textInputControl textfield/textarea to add the limit to
     * @param limit number of max. characters
     */
    /*public static void setCharacterLimit(TextInputControl textInputControl, int limit) {
        TextFormatter<String> textFormatter = new TextFormatter<>(change ->
                change.getControlNewText().length() <= limit ? change : null);
        textInputControl.setTextFormatter(textFormatter);
    }*/ //TODO: set character limit

    /**
     * Transforms a json into a list of Task objects
     * @param json received from the DB
     * @return list of fetched tasks
     */
    public static List<Task> parseTasks(String json) {
        if (json==null) return null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        //System.out.println(json);
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        return fetchedTasks;
    }

    /**
     * Makes it so a editText can only take integer values
     * @param editText to set the new format to
     */
    public static void integerSpinner(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    public static void setUpBottomMenu(BottomNavigationView bottomNavigationView, Activity activity) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homeButton) {
                ViewSwitcher.switchView(activity, ViewSwitcher.View.FEED);
                return true;
            } else if (itemId == R.id.listButton) {
                ViewSwitcher.switchView(activity, ViewSwitcher.View.TASKLIST);
                return true;
            } else if (itemId == R.id.newButton) {
                ViewSwitcher.switchView(activity, ViewSwitcher.View.NEWTASK);
                return true;
            } else if (itemId == R.id.groupsButton) {
                ViewSwitcher.switchView(activity, ViewSwitcher.View.GROUPS);
                return true;
            } else if (itemId == R.id.profileButton) {
                ViewSwitcher.switchView(activity, ViewSwitcher.View.PROFILE);
                return true;
            }
            return false;
        });
    }
}
