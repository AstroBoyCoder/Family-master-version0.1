package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AstroBoy on 2016/12/28.
 */

public class Family_Member_Bean implements Serializable {
    //private int id;
    private int User_ID;
    private String FM_Name;
    private String Create_Time;
    private String FM_Address;
    private String Reserved_Tel;
    private int FM_ID;
    private List<Member_Bean> members;
    //private int Member_ID;

    public Family_Member_Bean() {
        super();
    }

    public int getUser_ID() {
        return User_ID;
    }

    public Family_Member_Bean setUser_ID(int user_ID) {
        User_ID = user_ID;
        return this;
    }

    public String getFM_Name() {
        return FM_Name;
    }

    public Family_Member_Bean setFM_Name(String FM_Name) {
        this.FM_Name = FM_Name;
        return this;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public Family_Member_Bean setCreate_Time(String create_Time) {
        Create_Time = create_Time;
        return this;
    }

    public String getFM_Address() {
        return FM_Address;
    }

    public Family_Member_Bean setFM_Address(String FM_Address) {
        this.FM_Address = FM_Address;
        return this;
    }

    public String getReserved_Tel() {
        return Reserved_Tel;
    }

    public Family_Member_Bean setReserved_Tel(String reserved_Tel) {
        Reserved_Tel = reserved_Tel;
        return this;
    }

    public int getFM_ID() {
        return FM_ID;
    }

    public Family_Member_Bean setFM_ID(int FM_ID) {
        this.FM_ID = FM_ID;
        return this;
    }

    public List<Member_Bean> getMembers() {
        return members;
    }

    public Family_Member_Bean setMembers(List<Member_Bean> members) {
        this.members = members;
        return this;
    }

    @Override
    public String toString() {
        return "Family_Member_Bean{" +
                "User_ID=" + User_ID +
                ", FM_Name='" + FM_Name + '\'' +
                ", Create_Time='" + Create_Time + '\'' +
                ", FM_Address='" + FM_Address + '\'' +
                ", Reserved_Tel='" + Reserved_Tel + '\'' +
                ", FM_ID=" + FM_ID +
                ", members=" + members +
                '}';
    }

}
