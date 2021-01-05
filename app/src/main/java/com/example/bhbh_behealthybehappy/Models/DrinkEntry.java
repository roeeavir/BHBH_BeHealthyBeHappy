package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class DrinkEntry extends ItemEntry{

    // Variables
    private int amount = 0;
    private int carbs = 0;

    public DrinkEntry(String name, Enums.SCORE score, String notes, int amount, int carbs) {
        super(name, score, notes);
        this.amount = amount;
        this.carbs = carbs;
    }

    public int getAmount() {
        return amount;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
}
