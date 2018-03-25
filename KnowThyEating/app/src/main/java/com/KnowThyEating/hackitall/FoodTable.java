package com.KnowThyEating.hackitall;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodTable extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_table);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        setContentView(R.layout.activity_food_table);

        setupDisplayName();
        mChatListView = findViewById(R.id.chat_list_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);

        mChatListView.setAdapter(mAdapter);
        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                Log.d("GG"," i is = " + i);

                final Food selectedFood = (Food) mChatListView.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(FoodTable.this);
                builder.setTitle("Eating " + selectedFood.getmDish());

                final EditText input = new EditText(FoodTable.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();

                        try {
                            int eatenFood = Integer.valueOf(m_Text);
                            int currentSize = selectedFood.getmSize();

                            if (eatenFood > currentSize)
                                throw new NumberFormatException();

                            else {
                                selectedFood.setmSize(currentSize - eatenFood);
                                final Food newFood = new Food(selectedFood.getmDish(), selectedFood.getmSize(), selectedFood.getmFoodEaten() + eatenFood);
                                mAdapter.removeFromList(i);

                                final Dish dish = new Dish(selectedFood.getmDish(), selectedFood.getmFoodEaten(), selectedFood.getmSize());
                                if (selectedFood.getmSize() == 0) {
                                    UserInfo update = new UserInfo(mDisplayName, 0);
                                    mDatabaseReference.child(mDisplayName).child("Bank").child(selectedFood.getmDish()).removeValue();
                                }
                                else
                                    mDatabaseReference.child(mDisplayName).child("Bank").child(selectedFood.getmDish()).setValue(newFood);

                                mDatabaseReference.child(mDisplayName).child("Dishes").child(selectedFood.getmDish()).setValue(dish);

                            }
                        }
                        catch(NumberFormatException e) {
                            showErrorDialog("Insert a valid number!");
                        }

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

        mAdapter.cleanup();

    }


    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(LoginActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(LoginActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }

    private void showErrorDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
