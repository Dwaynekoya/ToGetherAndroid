package com.example.together.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.together.R;
import com.example.together.Utils;
import com.example.together.dboperations.DBUsers;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.model.User;
import com.example.together.view.ViewSwitcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView usernameLabel;
    private EditText editTextBio;
    private Button buttonChangeProfilePicture;
    private Button buttonEditBio, buttonMyGroups, buttonMyFriends, buttonSearchMore, buttonSettings;

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
        buttonSettings = findViewById(R.id.settingsButton);

        initUserData();

        buttonListeners();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Utils.setUpBottomMenu(bottomNavigationView, this);
    }

    private void buttonListeners() {
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
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSwitcher.switchView(ProfileActivity.this, ViewSwitcher.View.SETTINGS);
            }
        });
        buttonMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListActivity.class);
                intent.putExtra("listType", "users");
                startActivity(intent);
            }
        });

        buttonMyGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListActivity.class);
                intent.putExtra("listType", "groups");
                startActivity(intent);
            }
        });
        buttonSearchMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSwitcher.switchView(ProfileActivity.this, ViewSwitcher.View.SEARCH);
            }
        });
    }

    private void initUserData() {
        User loggedInUser = Utils.loggedInUser;
        usernameLabel.setText(loggedInUser.getUsername());
        editTextBio.setText(loggedInUser.getBio());

        // image loader
        Glide.with(this.getApplicationContext())
                .load(loggedInUser.getIcon())
                .into(imageView);
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
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(selectedImage);

                    new PhotoUploader(this, imageUri, Utils.loggedInUser).execute();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Utility method to get real path from URI
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return "";
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void editBio() {
        String newBio = editTextBio.getText().toString();
        Utils.loggedInUser.setBio(newBio);
        DBUsers.editUser("bio", newBio);

        Toast.makeText(this.getApplicationContext(), "Changed your bio!", Toast.LENGTH_SHORT);
    }
}

