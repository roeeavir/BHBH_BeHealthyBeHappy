package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

import org.jetbrains.annotations.NotNull;

public class ItemEntry {

    //Variables
    private String name = "";
    private Enums.ITEM_THEME itemType = Enums.ITEM_THEME.FOOD;
    private Enums.SCORE scoreType = Enums.SCORE.RED_HEART;
    private String notes = "";
    private int caloriesBurned = 0;
    private int calories = 0;
    private int carbs = 0;
    private double score = 0.0;

    // Empty constructor
    public ItemEntry() {
    }


    // Setters and Getters
    public String getName() {
        return name;
    }

    public Enums.SCORE getScoreType() {
        return scoreType;
    }

    public String getNotes() {
        return notes;
    }

    public ItemEntry setName(String name) {
        this.name = name;
        return this;

    }

    public ItemEntry setScoreType(Enums.SCORE scoreType) {
        this.scoreType = scoreType;
        return this;

    }

    public ItemEntry setNotes(String notes) {
        this.notes = notes;
        return this;

    }

    public int getCarbs() {
        return carbs;
    }


    public ItemEntry setCarbs(int carbs) {
        this.carbs = carbs;
        return this;

    }


    public int getCaloriesBurned() {
        return caloriesBurned;
    }


    public ItemEntry setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
        return this;

    }


    public Enums.ITEM_THEME getItemType() {
        return itemType;
    }

    public ItemEntry setItemType(Enums.ITEM_THEME itemType) {
        this.itemType = itemType;
        return this;
    }

    public int getCalories() {
        return calories;
    }

    public ItemEntry setCalories(int calories) {
        this.calories = calories;
        return this;
    }


    public double getScore() {
        return score;
    }

    public ItemEntry updateScore() {
        if (itemType == Enums.ITEM_THEME.ACTIVITY)
            this.score = (double) caloriesBurned / 30; // 30 calories burned equals 1 star
        else // Food or drink
            this.score = (double) calories / 100; // 100 calories equals 1 heart

        if (this.score - (int) this.score < 0.25) // Adjusting points
            this.score = (int) this.score;
        else if (this.score - (int) this.score < 0.75)
            this.score = 0.5 + (int) this.score;
        else
            this.score = 1 + (int) this.score;
        return this;
    }

    @NotNull
    @Override
    public String toString() {
        if (itemType == Enums.ITEM_THEME.ACTIVITY) {
            return "Calories Burned: " + caloriesBurned +
                    "\nNotes: " + notes;
        } else
            return "Carbs: " + carbs + " grams" +
                    "\nCalories: " + calories +
                    "\nNotes: " + notes;


    }
}
