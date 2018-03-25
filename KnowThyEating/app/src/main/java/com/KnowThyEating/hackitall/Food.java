package com.KnowThyEating.hackitall;


import java.util.Date;

class Food {

    private String mDish;
    private int mSize;
    private Date mDateCreated;
    private int mFoodEaten;


    public Food(String mDish, int mSize) {
        this.mDish=mDish;
        this.mSize=mSize;
        mDateCreated = new Date();
        mFoodEaten = 0;
    }

    public Food(String mDish, int mSize, int mFoodEaten) {
        this.mDish=mDish;
        this.mSize=mSize;
        mDateCreated = new Date();
        this.mFoodEaten = mFoodEaten;
    }

    public Food() {



    }

    public String getmDish() {
        return mDish;
    }

    public void setmDish(String mDish) {
        this.mDish = mDish;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

    public Date getmDateCreated() {
        return mDateCreated;
    }

    public void setmDateCreated(Date mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public void setmFoodEaten(int mFoodEaten){ this.mFoodEaten = mFoodEaten;}

    public int getmFoodEaten(){return mFoodEaten;}
}
