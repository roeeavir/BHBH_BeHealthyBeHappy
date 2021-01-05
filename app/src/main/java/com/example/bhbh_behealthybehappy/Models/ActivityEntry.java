package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class ActivityEntry extends ItemEntry{

    //Variables
    private int time = 0;
    private int caloriesPerHour = 0;

    public ActivityEntry(String name, Enums.SCORE score, String notes, int time, int caloriesPerHour) {
        super(name, score, notes);
        this.time = time;
        this.caloriesPerHour = caloriesPerHour;
    }

    public int getTime() {
        return time;
    }

    public int getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setCaloriesPerHour(int caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
    }
}
