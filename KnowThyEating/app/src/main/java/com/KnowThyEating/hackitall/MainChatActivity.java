package com.KnowThyEating.hackitall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    private ImageButton mEatButton;
    private ImageButton mAddButton;
    private ImageButton mThrownButton;
    private ImageButton mCreateButton;
    private ImageButton mViewStats;
    private ImageButton mSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        mEatButton = findViewById(R.id.btnEat);
        mAddButton = findViewById(R.id.btnAddFood);
        mCreateButton = findViewById(R.id.btnNewDish);
        mThrownButton = findViewById(R.id.btnThrown);
        mViewStats = findViewById(R.id.btnViewStats);
        mSignOut = findViewById(R.id.btnSignOut);

        mEatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, FoodTable.class);
                startActivity(intent);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, AddFoodActivity.class);
                startActivity(intent);
            }


        });

        mCreateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, CreateDishActivity.class);
                startActivity(intent);
            }


        });

        mThrownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, ThrownActivity.class);
                startActivity(intent);
            }
        });

        mViewStats.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChatActivity.this, Statistics.class);
                startActivity(intent);
            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainChatActivity.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });

    }


}
