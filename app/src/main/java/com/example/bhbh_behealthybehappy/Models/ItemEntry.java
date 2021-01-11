package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class ItemEntry {

    //Variables
    private String name = "";
    private Enums.ITEM_THEME itemType = Enums.ITEM_THEME.FOOD;
    private Enums.SCORE scoreType = Enums.SCORE.RED_HEART;
    private String notes = "";
    private int time = 0;
    private int caloriesPerHour = 0;
    private int weight = 0;
    private int amount = 0;
    private int calories = 0;
    private int carbs = 0;
    private double score = 0.0;


    public ItemEntry() {
    }

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

    public int getWeight() {
        return weight;
    }

    public int getCarbs() {
        return carbs;
    }

    public ItemEntry setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public ItemEntry setCarbs(int carbs) {
        this.carbs = carbs;
        return this;

    }

    public int getAmount() {
        return amount;
    }


    public ItemEntry setAmount(int amount) {
        this.amount = amount;
        return this;

    }

    public int getTime() {
        return time;
    }

    public int getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public ItemEntry setTime(int time) {
        this.time = time;
        return this;

    }

    public ItemEntry setCaloriesPerHour(int caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
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
            this.score = (double) getTime() / (caloriesPerHour * 60);
        else if (itemType == Enums.ITEM_THEME.DRINK) {
            this.score = (double) getAmount() * getCalories() / 100;
        } else
            this.score = (double) getWeight() * getCalories() / 100;

        return this;
    }


}
