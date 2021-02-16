package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class UserItemEntry implements Comparable<UserItemEntry> {

    //Variables
    private int quantity = 100; // Default quantity is 100 grams/milliliters
    private double score_by_quantity = 0.0;
    private ItemEntry itemEntry = new ItemEntry(); // Holds an item entry

    // Empty constructor
    public UserItemEntry() {
    }

    // Setters and Getters


    public int getQuantity() {
        return quantity;
    }

    public UserItemEntry setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ItemEntry getItemEntry() {
        return itemEntry;
    }

    public UserItemEntry setItemEntry(ItemEntry itemEntry) {
        this.itemEntry = itemEntry;
        return this;
    }

    public double getScore_by_quantity() {
        return score_by_quantity;
    }

    // Calculates score by its quantity
    public void updateScore_by_quantity() {
        if (itemEntry.getItemType() == Enums.ITEM_THEME.ACTIVITY)
            this.score_by_quantity = itemEntry.getScore() * quantity / 15; // adjusting quantity with base score
        else // Food or drink
            this.score_by_quantity = itemEntry.getScore() * quantity / 100; // adjusting quantity with base score


        if (this.score_by_quantity - (int) this.score_by_quantity < 0.25) // Adjusting points
            this.score_by_quantity = (int) this.score_by_quantity;
        else if (this.score_by_quantity - (int) this.score_by_quantity < 0.75)
            this.score_by_quantity = 0.5 + (int) this.score_by_quantity;
        else
            this.score_by_quantity = 1 + (int) this.score_by_quantity;
    }

    // Compares user_items by item name
    @Override
    public int compareTo(UserItemEntry o) {
        return this.getItemEntry().compareTo(o.getItemEntry());
    }
}
