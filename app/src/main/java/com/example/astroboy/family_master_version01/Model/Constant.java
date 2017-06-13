package com.example.astroboy.family_master_version01.Model;

/**
 * Created by AstroBoy on 2016/11/26.
 */

public class Constant {
    //private static String servlet_ip = "http://172.19.67.241:8080/family/family/family/";
    //public static String servlet_ip_short = "http://172.19.67.241:8080/family";
    private static String servlet_ip = "http://123.206.8.158:8080/family/family/family/";
    public static String servlet_ip_short = "http://123.206.8.158:8080/family";
    ///////////////////////////////////////////////////////////////////////////////////////
    public static String user_login = servlet_ip + "user!appLogin.action";                      //用户登录
    public static String user_register = servlet_ip + "user!appReg.action";                     //用户注册
    ///////////////////////////////////////////////////////////////////////////////////////
    public static String essay_publish = servlet_ip + "activity!appCreateEssay.action";         //用户发表动态
    public static String essay_getAll = servlet_ip + "activity!appGetEssay.action";             //获取所有动态
    public static String essay_getEssayByFM_ID = servlet_ip + "activity!appGetEssayByFm.action";//查看单个家庭动态
    public static String essay_addReview = servlet_ip + "activity!appReview.action";            //用户发表动态评论
    ///////////////////////////////////////////////////////////////////////////////////////
    public static String profile_uploadProfile = servlet_ip + "user!appEditUser.action";        //上传用户信息
    ///////////////////////////////////////////////////////////////////////////////////////
    public static String family_Families_Members = servlet_ip + "family!appFamilyListAndMember.action";//获取按照用户加入的家庭下的成员列表
    public static String family_create = servlet_ip + "family!appCreateFamily.action";          //创建家庭
    public static String family_search = servlet_ip + "family!appApplyFamily.action";           //申请家庭
    public static String family_getApplyList = servlet_ip + "family!appInformList.action";     //获取申请列表
    public static String family_AgreeFamily = servlet_ip + "user!appAgreeApply.action";
    public static String family_IgnoreFamily = servlet_ip + "user!appIgnoreApply.action";
    public static String family_apply = servlet_ip + "family!appApplyFamilyDo.action";


}