package com.twinstar.childtimeutilization;

import java.sql.Date;

/**
 * Created by JOE on 3/24/2016.
 */
public class ChildInfo {
    private String strParentName;
    private String strChildFirstName;
    private String strChildLastName;
    private String strMale;
    private String dob;
    private Integer _id;

    public ChildInfo(Integer _id,
                     String strParentName,
                     String strChildFirstName,
                     String strChildLastName,
                     String strMale,
                     String dob) {
        this._id = _id;
        this.strMale = strMale;
        this.strChildFirstName = strChildFirstName;
        this.strChildLastName = strChildLastName;
        this.strParentName = strParentName;
        this.dob = dob;
    }
    public ChildInfo() {
        // Empty because this construco is filled from ChildDBHandler
    }

    public void setParentName(String strParentName) { this.strParentName = strParentName; };
    public String getParentName() {return this.strParentName;};
    public void setChildFirstName(String strChildFirstName) { this.strChildFirstName = strChildFirstName; };
    public String getChildFirstName() {return this.strChildFirstName;};
    public void setChildLastName(String strChildLastName) { this.strChildLastName = strChildLastName; };
    public String getChildLastName() {return this.strChildLastName;};
    public void setMale(String strMale) { this.strMale = strMale; };
    public String getMale() {return this.strMale;};
    public void setDate(String dob) { this.dob = dob; };
    public String getDate() {return this.dob;};
    public void setId(Integer id) { this._id = id; };
    public Integer getId() {return this._id;};



    }
