package com.example.bhbh_behealthybehappy.Models;

public class FoodEntry extends ItemEntry{
    // Variables
    private int weight = 0;
    private int carbs = 0;

    public FoodEntry() {
    }

    public int getWeight() {
        return weight;
    }

    public int getCarbs() {
        return carbs;
    }

    public FoodEntry setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public FoodEntry setCarbs(int carbs) {
        this.carbs = carbs;
        return this;

    }
}
