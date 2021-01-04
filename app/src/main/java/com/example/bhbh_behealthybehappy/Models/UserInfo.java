package com.example.bhbh_behealthybehappy.Models;

public class UserInfo {


    private String userName = "";
    private int userAge = 0;
    private int userWeight = 0;
    private int userHeight = 0;

    public UserInfo(String userName, int userAge, int userWeight, int userHeight) {
        this.userName = userName;
        this.userAge = userAge;
        this.userWeight = userWeight;
        this.userHeight = userHeight;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }

    public void setUserHeight(int userHeight) {
        this.userHeight = userHeight;
    }
}
