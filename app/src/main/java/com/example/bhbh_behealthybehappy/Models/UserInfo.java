package com.example.bhbh_behealthybehappy.Models;

public class UserInfo {


    private String userName = "";
    private int userAge = 0;
    private int userWeight = 0;
    private int userHeight = 0;

    public UserInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public int getUserWeight() {
        return userWeight;
    }

    public int getUserHeight() {
        return userHeight;
    }

    public UserInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo setUserAge(int userAge) {
        this.userAge = userAge;
        return this;
    }

    public UserInfo setUserWeight(int userWeight) {
        this.userWeight = userWeight;
        return this;
    }

    public UserInfo setUserHeight(int userHeight) {
        this.userHeight = userHeight;
        return this;
    }
}
