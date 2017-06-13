package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;

/**
 * Created by AstroBoy on 2016/12/19.
 */

public class User_bean implements Serializable{
    private int User_ID;
    private String User_Phone;
    private String User_age;
    private String User_Idenity;
    private String User_Address;
    private String User_realname;
    private String User_Name;
    private String User_sex;
    private String User_Password;
    private String User_hobby;
    private String User_blood;
    private String User_Mail;

    public User_bean() {
        super();
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        User_Phone = user_Phone;
    }

    public String getUser_age() {
        return User_age;
    }

    public void setUser_age(String user_age) {
        User_age = user_age;
    }

    public String getUser_Idenity() {
        return User_Idenity;
    }

    public void setUser_Idenity(String user_Idenity) {
        User_Idenity = user_Idenity;
    }

    public String getUser_Address() {
        return User_Address;
    }

    public void setUser_Address(String user_Address) {
        User_Address = user_Address;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_sex() {
        return User_sex;
    }

    public void setUser_sex(String user_sex) {
        User_sex = user_sex;
    }

    public String getUser_Password() {
        return User_Password;
    }

    public void setUser_Password(String user_Password) {
        User_Password = user_Password;
    }

    public String getUser_hobby() {
        return User_hobby;
    }

    public void setUser_hobby(String user_hobby) {
        User_hobby = user_hobby;
    }

    public String getUser_blood() {
        return User_blood;
    }

    public void setUser_blood(String user_blood) {
        User_blood = user_blood;
    }

    public String getUser_Mail() {
        return User_Mail;
    }

    public void setUser_Mail(String user_Mail) {
        User_Mail = user_Mail;
    }

    public String getUser_realname() {
        return User_realname;
    }

    public User_bean setUser_realname(String user_realname) {
        User_realname = user_realname;
        return this;
    }


}
