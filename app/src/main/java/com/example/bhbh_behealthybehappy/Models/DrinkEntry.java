package com.example.bhbh_behealthybehappy.Models;

public class DrinkEntry extends ItemEntry{

    // Variables
    private int amount = 0;
    private int carbs = 0;

    public DrinkEntry() {
    }

    public int getAmount() {
        return amount;
    }

    public int getCarbs() {
        return carbs;
    }

    public DrinkEntry setAmount(int amount) {
        this.amount = amount;
        return this;

    }

    public ItemEntry setCarbs(int carbs) {
        this.carbs = carbs;
        return this;

    }
}
