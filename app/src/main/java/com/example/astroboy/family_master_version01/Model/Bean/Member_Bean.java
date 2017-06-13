package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;

/**
 * Created by AstroBoy on 2016/12/28.
 */

public class Member_Bean implements Serializable{

    private int User_ID;
    private String User_Name;
    private int Member_ID;

    public Member_Bean() {
        super();
    }

    public int getUser_ID() {
        return User_ID;
    }

    public Member_Bean setUser_ID(int user_ID) {
        User_ID = user_ID;
        return this;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public Member_Bean setUser_Name(String user_Name) {
        User_Name = user_Name;
        return this;
    }

    public int getMember_ID() {
        return Member_ID;
    }

    public Member_Bean setMember_ID(int member_ID) {
        Member_ID = member_ID;
        return this;
    }

    @Override
    public String toString() {
        return "Member_Bean{" +
                "User_ID=" + User_ID +
                ", User_Name='" + User_Name + '\'' +
                ", Member_ID=" + Member_ID +
                '}';
    }

}
