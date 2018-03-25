package com.KnowThyEating.hackitall;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThrownActivity extends AppCompatActivity {

    private String mDisplayName;
    private ListView mFoodList;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrown);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setupDisplayName();

        mFoodList = findViewById(R.id.thrownList);
    }

    private void setupDisplayName() {

        SharedPreferences prefs = getSharedPreferences(LoginActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(LoginActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mFoodList.setAdapter(mAdapter);

        mFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.d("GG", " i is = " + i);

                final Food selectedFood = (Food) mFoodList.getItemAtPosition(i);
                final Dish dish = new Dish(selectedFood.getmDish());

                mDatabaseReference.child("WastedFood").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            UserInfo user = dsp.getValue(UserInfo.class);
                            long x = dsp.getChildrenCount();
                            Log.d("GGGGGGGGGGGGGG", "HELLO");
                            if (user != null && user.getName().equals(mDisplayName)) {
                                Log.d("GGGGGGGGGGGGGG", String.valueOf(user.getWastedFood()));
                                FirebaseDatabase.getInstance().getReference().child("WastedFood").child(mDisplayName).setValue(new UserInfo(mDisplayName, user.getWastedFood() + selectedFood.getmSize()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                }
                );

                AlertDialog.Builder builder = new AlertDialog.Builder(ThrownActivity.this);
                builder.setTitle("Are you sure you want to throw " + selectedFood.getmDish() + "? \n");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFood.setmSize(0);
                        dish.setSuggested(selectedFood.getmFoodEaten());
                        dish.setCurrent(0);
                        mAdapter.removeFromList(i);
                        mDatabaseReference.child(mDisplayName).child("Bank").child(selectedFood.getmDish()).removeValue();
                        mDatabaseReference.child(mDisplayName).child("Dishes").child(selectedFood.getmDish()).setValue(dish);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}