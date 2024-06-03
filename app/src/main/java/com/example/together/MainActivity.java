package com.example.together;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.together.dboperations.DBUsers;
import com.example.together.view.ViewSwitcher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginButton, signupButton;
    EditText editTextUsername, editTextPassword;
    TextView infoLabel;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //access xml elements
        loginButton = findViewById(R.id.buttonLogin);
        signupButton = findViewById(R.id.buttonSignUp);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        infoLabel = findViewById(R.id.infoLabel);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            userLogin();
        } else if (view == signupButton) {
            userSignUp();
        }
    }

    private void userSignUp() {
        takeInputData();
        int newID = DBUsers.registerUser(username,password);
        switch (newID){
            case -1: infoLabel.setText("Error registering the user. Try again later :("); break;
            case -2: infoLabel.setText("Username has already been registered."); break;
            default: infoLabel.setText("User has been registered!"); break;
        }
    }

    private void userLogin() {
        takeInputData();
        int loginID = DBUsers.login(username, password);
        switch (loginID){
            case -1: infoLabel.setText("Username not found."); break;
            case -2: infoLabel.setText("Incorrect password.");break;
            default:infoLabel.setText("Login in process..."); launchApp(loginID); break;
        }
    }

    private void launchApp(int loginID) {
        Utils.loggedInUser = DBUsers.getUser(loginID);
        ViewSwitcher.switchView(this, ViewSwitcher.View.FEED);
    }

    private void takeInputData() {
        infoLabel.setText("");
        this.username = editTextUsername.getText().toString();
        this.password = editTextPassword.getText().toString();
    }
}