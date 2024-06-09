package com.example.together.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.together.R;
import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;

import com.example.together.dboperations.DBUsers;
import com.example.together.view.ConfirmationDialogFragment;
import com.example.together.view.ViewSwitcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity implements ConfirmationDialogFragment.ConfirmationDialogListener {

    private Button buttonLogout, buttonDeleteAllTasks, buttonLeaveAllGroups, buttonChangeUsername, buttonChangePassword;
    private EditText editTextUsername, editTextPassword;
    private int action; // 1 means DELETE TASKS, 2 means LEAVE GROUPS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        buttonLogout = findViewById(R.id.button);
        buttonDeleteAllTasks = findViewById(R.id.button2);
        buttonLeaveAllGroups = findViewById(R.id.button3);
        buttonChangeUsername = findViewById(R.id.buttonChangeUsername);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLogout.setOnClickListener(this::logOut);
        buttonDeleteAllTasks.setOnClickListener(this::deleteAllTasks);
        buttonLeaveAllGroups.setOnClickListener(this::leaveAllGroups);
        buttonChangeUsername.setOnClickListener(this::changeUsername);
        buttonChangePassword.setOnClickListener(this::changePassword);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    /**
     * Logs out current user and sends app back to the login view
     * @param view log out button
     */
    public void logOut(View view) {
        Utils.loggedInUser = null;
        ViewSwitcher.switchView(this, ViewSwitcher.View.LOGIN);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens confirmation dialog and assigns value (1) to action to identify current operation
     * @param view delete all tasks button
     */
    public void deleteAllTasks(View view) {
        action = 1;
        showConfirmationDialog();
    }

    /**
     * Opens confirmation dialog and assigns value (2) to action to identify current operation
     * @param view leave all groups button
     */
    public void leaveAllGroups(View view) {
        action = 2;
        showConfirmationDialog();
    }

    /**
     * Shows a dialog fragment to confirm action
     */
    private void showConfirmationDialog() {
        ConfirmationDialogFragment dialog = new ConfirmationDialogFragment(this);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // positive button on popup
        switch (action) {
            case 1:
                DBTask.deleteAllTasks();
                Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                DBGroup.leaveAllGroups();
                Toast.makeText(this, "Left all groups", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Invalid action", Toast.LENGTH_SHORT).show();
                break;
        }
        action = 0;
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this, "Cancelled action", Toast.LENGTH_SHORT).show();
    }

    public void changeUsername(View view) {
        String newUsername = editTextUsername.getText().toString().trim();
        if (!newUsername.isEmpty()) {
            // Assuming DBUser.updateUsername() is a method to update the username in your database
            DBUsers.editUser("username", newUsername);
            Utils.loggedInUser.setUsername(newUsername);
            Toast.makeText(this, "Username updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(View view) {
        String newPassword = editTextPassword.getText().toString().trim();
        if (!newPassword.isEmpty()) {
            // Assuming DBUser.updatePassword() is a method to update the password in your database
            DBUsers.editUser("password", newPassword);
            Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
