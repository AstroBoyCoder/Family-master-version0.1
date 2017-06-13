package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;

/**
 * Created by AstroBoy on 2016/12/19.
 */

public class Family_bean implements Serializable{
    private int id;
    private int User_ID;
    private String FM_Name;
    private String Create_Time;
    private String FM_Address;
    private String Reserved_Tel;
    private int FM_ID;
    private int Member_ID;

    public Family_bean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getFM_Name() {
        return FM_Name;
    }

    public void setFM_Name(String FM_Name) {
        this.FM_Name = FM_Name;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public void setCreate_Time(String create_Time) {
        Create_Time = create_Time;
    }

    public String getFM_Address() {
        return FM_Address;
    }

    public void setFM_Address(String FM_Address) {
        this.FM_Address = FM_Address;
    }

    public String getReserved_Tel() {
        return Reserved_Tel;
    }

    public void setReserved_Tel(String reserved_Tel) {
        Reserved_Tel = reserved_Tel;
    }

    public int getFM_ID() {
        return FM_ID;
    }

    public void setFM_ID(int FM_ID) {
        this.FM_ID = FM_ID;
    }

    public int getMember_ID() {
        return Member_ID;
    }

    public void setMember_ID(int member_ID) {
        Member_ID = member_ID;
    }

    @Override
    public String toString() {
        return "Family_bean{" +
                "id=" + id +
                ", User_ID=" + User_ID +
                ", FM_Name='" + FM_Name + '\'' +
                ", Create_Time='" + Create_Time + '\'' +
                ", FM_Address='" + FM_Address + '\'' +
                ", Reserved_Tel='" + Reserved_Tel + '\'' +
                ", FM_ID=" + FM_ID +
                ", Member_ID=" + Member_ID +
                '}';
    }
}
