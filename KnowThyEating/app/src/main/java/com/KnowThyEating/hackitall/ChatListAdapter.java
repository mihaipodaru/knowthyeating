package com.KnowThyEating.hackitall;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            mSnapshotList.remove(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name) {

        mActivity = activity;
        mDisplayName = name;
        // common error: typo in the db location. Needs to match what's in MainChatActivity.
        mDatabaseReference = ref.child(mDisplayName).child("Bank");
        mDatabaseReference.addChildEventListener(mListener);

        mSnapshotList = new ArrayList<>();
    }

    private static class ViewHolder{
        TextView foodName;
        TextView size;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public Food getItem(int position) {

        DataSnapshot snapshot = mSnapshotList.get(position);
        Log.d("Debug","clicked on item no. " + position);
        return snapshot.getValue(Food.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.foodName = convertView.findViewById(R.id.foodName);
            holder.size = convertView.findViewById(R.id.textView6);
            holder.params = (LinearLayout.LayoutParams) holder.foodName.getLayoutParams();
            convertView.setTag(holder);

        }

        final Food food = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();



        String foodName = food.getmDish();
        String foodSize = String.valueOf(food.getmSize());
        holder.foodName.setText(foodName);
        holder.size.setText(foodSize);


        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {


        holder.params.gravity = Gravity.START;
        holder.foodName.setLayoutParams(holder.params);

    }


    void removeFromList(int i){
        mSnapshotList.remove(i);
    }

    void cleanup() {

        mDatabaseReference.removeEventListener(mListener);
    }


}
