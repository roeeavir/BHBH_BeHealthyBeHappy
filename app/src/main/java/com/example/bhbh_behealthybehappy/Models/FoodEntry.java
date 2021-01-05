package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class FoodEntry extends ItemEntry{
    // Variables
    private int weight = 0;
    private int carbs = 0;

    public FoodEntry(String name, Enums.SCORE score, String notes, int weight, int carbs) {
        super(name, score, notes);
        this.weight = weight;
        this.carbs = carbs;
    }

    public int getWeight() {
        return weight;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
}
