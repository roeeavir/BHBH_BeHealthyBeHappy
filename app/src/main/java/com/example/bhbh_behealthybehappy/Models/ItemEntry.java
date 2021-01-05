package com.example.bhbh_behealthybehappy.Models;

import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class ItemEntry {

    //Variables
    private String name = "";
    private Enums.SCORE score = Enums.SCORE.RED_HEART;
    private String notes = "";

    public ItemEntry(String name, Enums.SCORE score, String notes) {
        this.name = name;
        this.score = score;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public Enums.SCORE getScore() {
        return score;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Enums.SCORE score) {
        this.score = score;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
