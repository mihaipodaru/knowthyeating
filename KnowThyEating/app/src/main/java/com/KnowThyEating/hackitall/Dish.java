package com.KnowThyEating.hackitall;

public class Dish {
    private String name;
    private int suggested;
    private  int current;

    public Dish(String name){
        this.name=name;
        suggested = 0;
        current = 0;
    }

    public Dish(String name, int suggested){
        this.name=name;
        this.suggested = suggested;
        current = 0;
    }

    public Dish(String name, int suggested, int current){
        this.name=name;
        this.suggested = suggested;
        this.current = current;
    }

    public Dish(){

    }

    public String getname(){
        return name;
    }
    public void setname(String name){
        this.name=name;
    }

    public void setSuggested(int suggested){ this.suggested = suggested;}
    public int getSuggested(){return suggested;}

    public void setCurrent(int current){ this.current = current;}
    public int getCurrent(){return current;}
}
