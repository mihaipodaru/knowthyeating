package com.KnowThyEating.hackitall;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Statistics extends AppCompatActivity{

    private final double GLOBAL = 1000000000007.00;
    private final double FOOD_PORTION = 150;
    private double totalThrownFood;
    private double yourThrownFood;
    private String mDisplayName;
    private TextView title;
    private TextView t1;
    private TextView t2;
    private TextView t3;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);



        title = (TextView) findViewById(R.id.textView7);

        t1 = (TextView) findViewById(R.id.textView8);

        t2 = (TextView) findViewById(R.id.textView9);

        t3 = (TextView) findViewById(R.id.textView10);

        setupDisplayName();
        FirebaseDatabase.getInstance().getReference().child("WastedFood").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalWastedFood = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    UserInfo user = dsp.getValue(UserInfo.class);
                    long x = dsp.getChildrenCount();
                    if (user != null) {
                        totalWastedFood += (double) user.getWastedFood();
                        Log.d(mDisplayName, user.getName());
                        if(user.getName().equals(mDisplayName)) {
                            yourThrownFood = (double) user.getWastedFood();
                        }
                    }
                }

                totalThrownFood = totalWastedFood;

                DecimalFormat decimalFormat = new DecimalFormat("#0.00");

                title.setText("Hello, " + mDisplayName + "! These are your food wasting stats:");

                t1.setText("You have wasted a total of " + yourThrownFood % 100000+ " G of food");

                t2.setText("You wasted " + decimalFormat.format(yourThrownFood/GLOBAL * 100) + " % of the globally wasted food, and also " + decimalFormat.format(yourThrownFood/totalThrownFood * 100) + " % of the food wasted by our users");

                t3.setText("A total of " + decimalFormat.format(yourThrownFood / 1000) + " could have been fed with the food you've wasted!");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        }
        );
    }

    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(LoginActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(LoginActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }
}
