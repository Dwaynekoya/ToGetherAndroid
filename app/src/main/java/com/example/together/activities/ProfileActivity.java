package com.example.together.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBUsers;
import com.example.together.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends Activity {

    private ImageView imageView;
    private TextView usernameLabel;
    private EditText editTextBio;
    private Button buttonChangeProfilePicture;
    private Button buttonEditBio;
    private Button buttonMyGroups;
    private Button buttonMyFriends;
    private Button buttonSearchMore;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        imageView = findViewById(R.id.imageView);
        usernameLabel = findViewById(R.id.usernameLabel);
        editTextBio = findViewById(R.id.editTextBio);
        buttonChangeProfilePicture = findViewById(R.id.buttonChangeProfilePicture);
        buttonEditBio = findViewById(R.id.buttonEditBio);
        buttonMyGroups = findViewById(R.id.buttonMyGroups);
        buttonMyFriends = findViewById(R.id.buttonMyFriends);
        buttonSearchMore = findViewById(R.id.buttonSearchMore);

        initUserData();

        buttonChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        buttonEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBio();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void initUserData() {
        User loggedInUser = Utils.loggedInUser;
        usernameLabel.setText(loggedInUser.getUsername());
        editTextBio.setText(loggedInUser.getBio());
        imageView.setImageBitmap(BitmapFactory.decodeFile(loggedInUser.getIcon()));
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(selectedImage);

                    // TODO: Replace with DB set icon
                    Utils.loggedInUser.setIcon(data.getData().toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void editBio() {
        String newBio = editTextBio.getText().toString();
        Utils.loggedInUser.setBio(newBio);
        DBUsers.editBio(newBio);
    }
    //TODO: buttons that open friend list and group list
    //TODO: button that opens search
}

