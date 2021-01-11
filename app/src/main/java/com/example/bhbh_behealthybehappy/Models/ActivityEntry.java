package com.example.bhbh_behealthybehappy.Models;

public class ActivityEntry extends ItemEntry{

    //Variables
    private int time = 0;
    private int caloriesPerHour = 0;

    public ActivityEntry() {

    }

    public int getTime() {
        return time;
    }

    public int getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public ActivityEntry setTime(int time) {
        this.time = time;
        return this;

    }

    public ActivityEntry setCaloriesPerHour(int caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
        return this;

    }
}
