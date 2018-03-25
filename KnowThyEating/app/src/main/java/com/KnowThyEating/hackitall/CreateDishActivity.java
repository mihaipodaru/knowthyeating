package com.KnowThyEating.hackitall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class CreateDishActivity extends AppCompatActivity {

    private EditText editName;
    private Button mCreate;
    private String mDisplayName;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish_activty);

        setupDisplayName();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        editName = findViewById(R.id.insertNameTxt);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText("");
            }
        });

        editName = findViewById(R.id.insertNameTxt);
        mCreate = findViewById(R.id.btnCreate);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dishName = editName.getText().toString();
                if (!dishName.equals("") && Pattern.matches("[a-zA-Z ]+", dishName)) {
                    Dish dish = new Dish(dishName);
                    mDatabaseReference.child(mDisplayName).child("Dishes").child(dishName).setValue(dish);
                    Intent intent = new Intent(CreateDishActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }
                else
                    showErrorDialog("Type a valid name!");
            }
        });

    }

    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(LoginActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(LoginActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }

    private void showErrorDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Not valid dish name")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
