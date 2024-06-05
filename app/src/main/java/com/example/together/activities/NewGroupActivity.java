package com.example.together.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBGroup;
import com.example.together.model.Group;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewGroupActivity extends AppCompatActivity {

    private EditText edittextGroupName, editTextInfo;
    private Button buttonCreate, buttonCancel;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroup);

        edittextGroupName = findViewById(R.id.edittextGroupName);
        editTextInfo = findViewById(R.id.editTextInfo);
        buttonCreate = findViewById(R.id.button8);
        buttonCancel = findViewById(R.id.button9);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        Utils.setUpBottomMenu(bottomNavigation, this);
    }

    private void createGroup() {
        String groupName = edittextGroupName.getText().toString().trim();
        String description = editTextInfo.getText().toString().trim();

        if (groupName.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            Group newGroup = new Group(groupName, description);
            DBGroup.createGroup(newGroup);
            Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    private void cancel() {
        //finish();
    }

}
