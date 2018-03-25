package com.KnowThyEating.hackitall;

public class UserInfo {

    private String name;
    private int wastedFood;

    public UserInfo(String name, int wastedFood){
        this.name = name;
        this.wastedFood = wastedFood;
    }

    public UserInfo(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWastedFood(int wastedFood) {
        this.wastedFood = wastedFood;
    }

    public int getWastedFood() {
        return wastedFood;
    }
}
