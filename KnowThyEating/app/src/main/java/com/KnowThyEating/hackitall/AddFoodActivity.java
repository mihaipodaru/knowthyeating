package com.KnowThyEating.hackitall;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodActivity extends AppCompatActivity{

    private String mDisplayName;
    private ListView mFoodList;
    private DatabaseReference mDatabaseReference;
    private NamesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setupDisplayName();

        mFoodList = findViewById(R.id.addfood_list_view);
    }

    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(LoginActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(LoginActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }

    @Override
    public void onStart(){
        super.onStart();

        mAdapter = new NamesAdapter(this, mDatabaseReference, mDisplayName);
        mFoodList.setAdapter(mAdapter);

        mFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.d("GG"," i is = " + i);

                final Dish selectedDish = (Dish) mFoodList.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);

                if(selectedDish.getSuggested() == 0) {
                    if (selectedDish.getCurrent() > 0)
                        builder.setMessage("First time cooking " + selectedDish.getname() + ".\n" +
                                "You already have this food." + "\n" + "Enter if you want more." + "\n");
                    else
                        builder.setMessage("First time cooking " + selectedDish.getname() + ".\n ");
                }
                else {
                    if (selectedDish.getCurrent() > 0)
                        builder.setMessage("Suggested " + selectedDish.getSuggested() + " " + selectedDish.getname() + ".\n" +
                                "You already have this food." + "\n" + "Enter if you want more." + "\n");
                    else
                        builder.setMessage("Suggested " + selectedDish.getSuggested() + " " + selectedDish.getname() + ".\n");
                }

                final EditText input = new EditText(AddFoodActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        try {
                            int size = Integer.valueOf(m_Text);

                            if (size == 0)
                                throw new NumberFormatException("0");

                            Food food = new Food(selectedDish.getname(), size + selectedDish.getCurrent(), selectedDish.getSuggested());
                            final Dish newDish = new Dish(selectedDish.getname(), selectedDish.getSuggested(), food.getmSize());

                            mDatabaseReference.child(mDisplayName).child("Bank").child(selectedDish.getname()).setValue(food);
                            mDatabaseReference.child(mDisplayName).child("Dishes").child(selectedDish.getname()).setValue(newDish);
                        }
                        catch (NumberFormatException e) {
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
    public void onStop(){
        super.onStop();

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
