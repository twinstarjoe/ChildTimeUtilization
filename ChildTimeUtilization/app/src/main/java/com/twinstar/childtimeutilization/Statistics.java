package com.twinstar.childtimeutilization;

/**
 * Created by JOE on 3/24/2016.
 */
public class Statistics {
    private int _id;
    private String strParentName;
    private String strChildFirstName;
    private String strChildLastName;
    private String strWentToBed;
    private String strWokeUp;
    private String strTotalScreenTime;
    private String strTotalFriendsAndFamilyTime;

    public Statistics(int _id,
                      String strParentName,
                      String strChildFirstName,
                      String strChildLastName,
                      String strTotalScreenTime,
                      String strTotalFriendsAndFamilyTime,
                      String strWentToBed,
                      String strWokeUp) {
        this._id = _id;
        this.strParentName = strParentName;
        this.strChildFirstName = strChildFirstName;
        this.strChildLastName = strChildLastName;
        this.strWentToBed = strWentToBed;
        this.strWokeUp = strWokeUp;
        this.strTotalScreenTime = strTotalScreenTime;
        this.strTotalFriendsAndFamilyTime = strTotalFriendsAndFamilyTime;
    }
    public void setId(int id) {
        this._id = id;
    }
    public int getId() {
        return this._id;
    }
    public void setParentName(String strParentName) {
        this.strParentName = strParentName;
    }
    public String getParentName() {
        return this.strParentName;
    }
    public void setChildFirstName(String strChildFirstName) {
        this.strChildFirstName = strChildFirstName;
    }
    public String getChildFirstName() {
        return this.strChildFirstName;
    }
    public void setChildLastName(String strChildLastName) {
        this.strChildLastName = strChildLastName;
    }
    public String getChildLastName() {
        return this.strChildLastName;
    }
    public void setWentToBed(String strWentToBed) {
        this.strWentToBed = strWentToBed;
    }
    public String getWentToBed() {
        return this.strWentToBed;
    }
    public void setWokeUp(String strWokeUp) {
        this.strWokeUp = strWokeUp;
    }
    public String getWokeUp() {
        return this.strWokeUp;
    }
    public void setTotalScreenTime(String strTotalScreenTime) {
        this.strTotalScreenTime = strTotalScreenTime;
    }
    public String getTotalScreenTime() {
        return this.strTotalScreenTime;
    }
    public void setTotalFriendsAndFamilyTime(String strTotalFriendsAndFamilyTime) {
        this.strTotalFriendsAndFamilyTime = strTotalFriendsAndFamilyTime;
    }
    public String getTotalFriendsAndFamilyTime() {
        return this.strTotalFriendsAndFamilyTime;
    }

}