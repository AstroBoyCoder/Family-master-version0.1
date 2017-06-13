package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;

/**
 * Created by AstroBoy on 2016/12/24.
 */

public class Comment_Bean implements Serializable{
    private int id;
    private int User_ID;
    private String Essay_ID;
    private String User_Name;
    private String review;
    private int FM_ID;

    public Comment_Bean() {
        super();
    }

    public int getId() {
        return id;
    }

    public Comment_Bean setId(int id) {
        this.id = id;
        return this;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public Comment_Bean setUser_ID(int user_ID) {
        User_ID = user_ID;
        return this;
    }

    public String getEssay_ID() {
        return Essay_ID;
    }

    public Comment_Bean setEssay_ID(String essay_ID) {
        Essay_ID = essay_ID;
        return this;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public Comment_Bean setUser_Name(String user_Name) {
        User_Name = user_Name;
        return this;
    }

    public String getReview() {
        return review;
    }

    public Comment_Bean setReview(String review) {
        this.review = review;
        return this;
    }

    public int getFM_ID() {
        return FM_ID;
    }

    public Comment_Bean setFM_ID(int FM_ID) {
        this.FM_ID = FM_ID;
        return this;
    }

    @Override
    public String toString() {
        return "Comment_Bean{" +
                "id=" + id +
                ", User_ID=" + User_ID +
                ", Essay_ID='" + Essay_ID + '\'' +
                ", User_Name='" + User_Name + '\'' +
                ", review='" + review + '\'' +
                ", FM_ID=" + FM_ID +
                '}';
    }
}
