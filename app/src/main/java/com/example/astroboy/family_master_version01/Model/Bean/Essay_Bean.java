package com.example.astroboy.family_master_version01.Model.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AstroBoy on 2016/12/19.
 */

public class Essay_Bean implements Serializable{

    private String User_Name;               //文章发布人
    private String FM_Name;
    private int User_ID;                    //文章发布人ID
    private String Essay_Content;           //文章内容
    private int Essay_ID;                   //文章ID
    private String Publish_Date;            //文章发布日期
    private int FM_ID;                      //文章发布所在的家庭
    private int Member_ID;                  //家庭内成员ID
    private String Essay_IMG;               //文章所附带的图片
    private List<Comment_Bean> reviews;
    //private ArrayList<Essay_IMG> Essay_IMGS;

    public Essay_Bean() {
        super();
    }

    public String getEssay_IMG() {
        return Essay_IMG;
    }

    public void setEssay_IMG(String essay_IMG) {
        Essay_IMG = essay_IMG;
    }

    public String getEssay_Content() {
        return Essay_Content;
    }

    public void setEssay_Content(String essay_Content) {
        Essay_Content = essay_Content;
    }

    public int getEssay_ID() {
        return Essay_ID;
    }

    public void setEssay_ID(int essay_ID) {
        Essay_ID = essay_ID;
    }

    public String getPublish_Date() {
        return Publish_Date;
    }

    public void setPublish_Date(String publish_Date) {
        Publish_Date = publish_Date;
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

    public String getUser_Name() {
        return User_Name;
    }

    public Essay_Bean setUser_Name(String user_Name) {
        User_Name = user_Name;
        return this;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public Essay_Bean setUser_ID(int user_ID) {
        User_ID = user_ID;
        return this;
    }

    public String getFM_Name() {
        return FM_Name;
    }

    public Essay_Bean setFM_Name(String FM_Name) {
        this.FM_Name = FM_Name;
        return this;
    }

    public List<Comment_Bean> getReviews() {
        return reviews;
    }

    public Essay_Bean setReviews(List<Comment_Bean> reviews) {
        this.reviews = reviews;
        return this;
    }

    @Override
    public String toString() {
        return "Essay_Bean{" +
                "User_Name='" + User_Name + '\'' +
                ", FM_Name='" + FM_Name + '\'' +
                ", User_ID=" + User_ID +
                ", Essay_Content='" + Essay_Content + '\'' +
                ", Essay_ID=" + Essay_ID +
                ", Publish_Date='" + Publish_Date + '\'' +
                ", FM_ID=" + FM_ID +
                ", Member_ID=" + Member_ID +
                ", Essay_IMG='" + Essay_IMG + '\'' +
                ", reviews=" + reviews +
                '}';
    }
}
