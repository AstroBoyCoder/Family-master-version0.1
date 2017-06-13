package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;

/**
 * Created by AstroBoy on 2016/12/19.
 */

public class FamilyApplyList_bean implements Serializable{
    private int id;
    private String FM_Name;
    private String User_Name;
    private String Create_Time;
    private int FM_ID;
    private int Member_ID;

    public FamilyApplyList_bean() {
        super();
    }

    public int getId() {
        return id;
    }

    public FamilyApplyList_bean setId(int id) {
        this.id = id;
        return this;
    }

    public String getFM_Name() {
        return FM_Name;
    }

    public FamilyApplyList_bean setFM_Name(String FM_Name) {
        this.FM_Name = FM_Name;
        return this;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public FamilyApplyList_bean setUser_Name(String user_Name) {
        User_Name = user_Name;
        return this;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public FamilyApplyList_bean setCreate_Time(String create_Time) {
        Create_Time = create_Time;
        return this;
    }

    public int getFM_ID() {
        return FM_ID;
    }

    public FamilyApplyList_bean setFM_ID(int FM_ID) {
        this.FM_ID = FM_ID;
        return this;
    }

    public int getMember_ID() {
        return Member_ID;
    }

    public FamilyApplyList_bean setMember_ID(int member_ID) {
        Member_ID = member_ID;
        return this;
    }

    @Override
    public String toString() {
        return "FamilyApplyList_bean{" +
                "id=" + id +
                ", FM_Name='" + FM_Name + '\'' +
                ", User_Name='" + User_Name + '\'' +
                ", Create_Time='" + Create_Time + '\'' +
                ", FM_ID=" + FM_ID +
                ", Member_ID=" + Member_ID +
                '}';
    }
}
