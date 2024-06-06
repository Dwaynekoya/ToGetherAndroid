package com.example.together.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBUsers;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchText;
    private RadioButton radioButtonGroup;
    private RadioButton radioButtonUser;
    private List searchResult;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchText = findViewById(R.id.searchEditText);
        radioButtonGroup = findViewById(R.id.radioButtonGroup);
        radioButtonUser = findViewById(R.id.radioButtonUser);
        listView = findViewById(R.id.searchlist);

        // Retrieve extra to determine which radio button to select
        String type = getIntent().getStringExtra("type");
        if (type != null) {
            if (type.equals("group")) {
                radioButtonGroup.setChecked(true);
            } else if (type.equals("user")) {
                radioButtonUser.setChecked(true);
            }
        }

        RadioGroup radioGroup = findViewById(R.id.searchRadioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonGroup) {
                performGroupSearch();
            } else if (checkedId == R.id.radioButtonUser) {
                performUserSearch();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (radioButtonGroup.isChecked()) {
                    performGroupSearch();
                } else if (radioButtonUser.isChecked()) {
                    performUserSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void performGroupSearch() {
        String name = searchText.getText().toString();
        if (name.isEmpty()) return;
        searchResult = DBGroup.searchGroups(name);
    }

    private void performUserSearch() {
        String name = searchText.getText().toString();
        if (name.isEmpty()) return;
        searchResult = DBUsers.searchUsers(name);
    }
}
