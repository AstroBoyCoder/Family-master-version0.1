package com.example.astroboy.family_master_version01.View.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.astroboy.family.GreenDao.User;
import com.example.astroboy.family_master_version01.Model.Bean.User_bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_UserService;
import com.example.astroboy.family_master_version01.Util.ExitApplication;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.RoundImageView;
import com.example.astroboy.family_master_version01.View.CustomView.BaseActivity;
import com.example.astroboy.family_master_version01.View.CustomView.PopDialog.Family_profile_modifyInfo_popWindow;
import com.example.astroboy.family_master_version01.View.UserLoadApp.Family_Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.View.CustomView.PopDialog.Family_profile_modifyInfo_popWindow.popView;
import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;

public class Family_profile_infoSetting_Activity extends BaseActivity implements View.OnClickListener{

    private Context context = Family_profile_infoSetting_Activity.this;
    private final static String TAG = "profile_infoSetting";
    private boolean isUserExist = false;
    private String reCode = "";

    LinearLayout Icon_ly,Name_ly,Address_ly,Phone_ly,Identity_ly,Mail_ly,Gender_ly,RealName_ly,Age_ly,Hobby_ly,Blood_ly;
    TextView Name_tv,Address_tv,Phone_tv,Identity_tv,Mail_tv,Gender_tv,RealName_tv,Age_tv,Hobby_tv,Blood_tv;
    RoundImageView Icon_iv;
    ImageButton info_back;

    User currentUser = null;
    User_bean user = null;

    Handler mHandler;
    Handler uiHandler;

    static String info = "modify_text";
    static String gender="modify_gender";
    static String icon="modify_icon";
    Family_profile_modifyInfo_popWindow popDialog_Info;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_profile_info_setting_activity);
        ExitApplication.getInstance().addActivity(this);
        initView();
        initData();
    }

    private void initView() {
        //addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        info_back = (ImageButton) findViewById(R.id.info_back);

        Icon_ly = (LinearLayout) findViewById(R.id.info_userIcon_ly);
        Name_ly = (LinearLayout) findViewById(R.id.info_userName_ly);
        Address_ly = (LinearLayout) findViewById(R.id.info_address_ly);
        Phone_ly = (LinearLayout) findViewById(R.id.info_userPhone_ly);
        Identity_ly = (LinearLayout) findViewById(R.id.info_identity_ly);
        Mail_ly = (LinearLayout) findViewById(R.id.info_mail_ly);
        Gender_ly = (LinearLayout) findViewById(R.id.info_gender_ly);
        RealName_ly = (LinearLayout) findViewById(R.id.info_realName_ly);
        Age_ly = (LinearLayout) findViewById(R.id.info_age_ly);
        Hobby_ly = (LinearLayout) findViewById(R.id.info_hobby_ly);
        Blood_ly = (LinearLayout) findViewById(R.id.info_blood_ly);

        Icon_iv = (RoundImageView) findViewById(R.id.info_userIcon_roundImg);
        Name_tv = (TextView) findViewById(R.id.info_userName_tv);
        Address_tv = (TextView) findViewById(R.id.info_address_tv);
        Phone_tv = (TextView) findViewById(R.id.info_userPhone_tv);
        Identity_tv = (TextView) findViewById(R.id.info_identity_tv);
        Mail_tv = (TextView) findViewById(R.id.info_mail_tv);
        Gender_tv = (TextView) findViewById(R.id.info_gender_tv);
        RealName_tv = (TextView) findViewById(R.id.info_realName_tv);
        Age_tv = (TextView) findViewById(R.id.info_age_tv);
        Hobby_tv = (TextView) findViewById(R.id.info_hobby_tv);
        Blood_tv = (TextView) findViewById(R.id.info_blood_tv);

        info_back.setOnClickListener(this);
        Icon_ly.setOnClickListener(this);
        Name_ly.setOnClickListener(this);
        Address_ly.setOnClickListener(this);
        Phone_ly.setOnClickListener(this);
        Identity_ly.setOnClickListener(this);
        Mail_ly.setOnClickListener(this);
        Gender_ly.setOnClickListener(this);
        RealName_ly.setOnClickListener(this);
        Age_ly.setOnClickListener(this);
        Hobby_ly.setOnClickListener(this);
        Blood_ly.setOnClickListener(this);
    }

    private void initData() {
        uiHandler();
        queryUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info_back:
                onBackPressed();
                break;
            case R.id.info_userIcon_ly:
                //canNotModifyToast();
                popDialogDisplay("modify_icon",icon);
                break;
            case R.id.info_userName_ly:
                popDialogDisplay("修改用户名",info);
                break;
            case R.id.info_address_ly:
                popDialogDisplay("修改地址",info);
                break;
            case R.id.info_userPhone_ly:
                //popDialogDisplay("修改手机号",info);
                canNotModifyToast();
                break;
            case R.id.info_identity_ly:
                canNotModifyToast();
                break;
            case R.id.info_mail_ly:
                popDialogDisplay("修改邮箱",info);
                break;
            case R.id.info_gender_ly:
                popDialogDisplay("修改性别",gender);
                break;
            case R.id.info_realName_ly:
                popDialogDisplay("修改姓名",info);
                break;
            case R.id.info_age_ly:
                popDialogDisplay("修改年龄",info);
                break;
            case R.id.info_hobby_ly:
                popDialogDisplay("修改爱好",info);
                break;
            case R.id.info_blood_ly:
                popDialogDisplay("修改血型",info);
                break;
        }
    }

    private void queryUser() {
        Message msg = new Message();
        List<User> users = Db_UserService.getInstance(this).loadAllUser();
        try {
            for (User single : users){
              if (single.getUser_ID()==Integer.parseInt(UID)){
                    currentUser = single;
                }
            }
        }catch (Exception e){
            Toast.makeText(this,"查找用户失败:"+e,Toast.LENGTH_SHORT).show();
            isUserExist = false;
        }
        if (currentUser!=null){
            msg.what = 1;
            uiHandler.sendMessage(msg);
        }else{
            msg.what = 2;
            uiHandler.sendMessage(msg);
        }
    }

    private void uiHandler(){
        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Name_tv.setText(currentUser.getUser_Name());
                        Address_tv.setText(currentUser.getUser_Address());
                        Phone_tv.setText(currentUser.getUser_Phone());
                        if (currentUser.getUser_Identity().equals("0")) Identity_tv.setText("普通用户");
                        else Identity_tv.setText("管理员");
                        Mail_tv.setText(currentUser.getUser_Mail());
                        Gender_tv.setText(currentUser.getUser_Sex());
                        RealName_tv.setText(currentUser.getUser_RealName());
                        Age_tv.setText(currentUser.getUser_Age());
                        Hobby_tv.setText(currentUser.getUser_Hobby());
                        Blood_tv.setText(currentUser.getUser_Blood());
                        isUserExist = true;
                        break;
                    case 2:
                        Toast.makeText(context,"获取用户信息失败,请重新登录",Toast.LENGTH_SHORT).show();
                        isUserExist = false;
                        jumpToLogin();
                        break;
                    default:
                        Toast.makeText(context,"Unknown Status:"+msg.what,Toast.LENGTH_SHORT).show();
                        isUserExist = false;
                        break;
                }
            }
        };
    }

    private void popDialogDisplay(String title,String input_Type) {
        popDialog_Info = new Family_profile_modifyInfo_popWindow(this,new popDialogItemsOnClick(),title,input_Type);
        //显示窗口
        popDialog_Info.showAtLocation(this.findViewById(R.id.student_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

    }

    private class popDialogItemsOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.modify_edit_confirm:
                    if (isUserExist){
                        TextView contentEdit = (TextView) popView.findViewById(R.id.modify_edit);
                        TextView typeTitle = (TextView) popView.findViewById(R.id.modify_text);
                        String content = contentEdit.getText().toString();
                        String infoType = typeTitle.getText().toString();
                        if (!content.equals("")) {
                                switch (infoType) {
                                    case "修改用户名":
                                        if (content.length()<=11){
                                            if (!content.equals(currentUser.getUser_Name())) {
                                                currentUser.setUser_Name(content);
                                                postUserProfileThread(currentUser);
                                            }
                                        }else {
                                            Toast.makeText(context,"用户名长度不得超过11位",Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "修改地址":
                                        if (!content.equals(currentUser.getUser_Address())) {
                                            currentUser.setUser_Address(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                    case "修改邮箱":
                                        if (!content.equals(currentUser.getUser_Mail())) {
                                            currentUser.setUser_Mail(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                    case "修改姓名":
                                        if (!content.equals(currentUser.getUser_RealName())) {
                                            currentUser.setUser_RealName(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                    case "修改年龄":
                                        if (!content.equals(currentUser.getUser_Age())) {
                                            currentUser.setUser_Age(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                    case "修改爱好":
                                        if (!content.equals(currentUser.getUser_Hobby())) {
                                            currentUser.setUser_Hobby(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                    case "修改血型":
                                        if (!content.equals(currentUser.getUser_Blood())) {
                                            currentUser.setUser_Blood(content);
                                            postUserProfileThread(currentUser);
                                        }
                                        break;
                                }
                            popDialog_Info.dismiss();
                        }else {
                            contentEdit.setError("请输入要更改的内容");
                        }
                    }else {
                        illegalStatus();
                    }
                    break;
                case R.id.modify_edit_cancel:
                    popDialog_Info.dismiss();
                    break;
                case R.id.modify_gender_confirm:
                    if (isUserExist) {
                        RadioButton male = (RadioButton) popView.findViewById(R.id.radio_male);
                        RadioButton female = (RadioButton) popView.findViewById(R.id.radio_female);
                        if ((!male.isChecked() && female.isChecked()) || (male.isChecked() && !female.isChecked())) {
                            String gender = "男";
                            if (!male.isChecked()) gender = "女";
                            if (!gender.equals(currentUser.getUser_Sex())) {
                                currentUser.setUser_Sex(gender);
                                postUserProfileThread(currentUser);
                            }
                            popDialog_Info.dismiss();
                        } else {
                            Toast.makeText(context, "请选择性别", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        illegalStatus();
                    }
                    break;
                case R.id.modify_gender_cancel:
                    popDialog_Info.dismiss();
                    break;
                case R.id.item_popupwindows_camera:
                    break;
                case R.id.item_popupwindows_Photo:
                    break;
                case R.id.modify_userIcon_cancel:
                    popDialog_Info.dismiss();
                    break;

            }
        }
    }

    private void postUserProfileThread(final User changedProfile) {
        postUserProfileHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "changedProfile:"+changedProfile);
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID",String.valueOf(changedProfile.getUser_ID()));
                    data.put("truename",changedProfile.getUser_RealName());
                    data.put("username",changedProfile.getUser_Name());
                    data.put("age",changedProfile.getUser_Age());
                    data.put("sex",changedProfile.getUser_Sex());
                    data.put("hobby",changedProfile.getUser_Hobby());
                    data.put("blood",changedProfile.getUser_Blood());
                    data.put("address",changedProfile.getUser_Address());
                    data.put("email",changedProfile.getUser_Mail());
                    reCode = Post_userLogin.getStringCha(Constant.profile_uploadProfile, data);//向服务器提交用户上传请求
                    Log.d(TAG, "Post userLogin result:" + reCode);
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.what = 1;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void postUserProfileHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String response = msg.getData().getString("response");
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d(TAG, "Json内容" + json);
                            String Msg = json.getString("msg");
                            String editFlag = json.getString("editFlag");
                            if (editFlag.equals("1")){
                                Db_UserService.getInstance(context).saveUser(currentUser);
                                queryUser();
                                Boolean isUpdated = setUser(JSON.parseObject(json.getString("user"),User_bean.class));
                                if (isUpdated){
                                    queryUser();
                                }
                            }else if (editFlag.equals("0")){
                                Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "更改用户信息捕获异常:" + e);
                        }
                        break;
                    case 2:
                        Toast.makeText(context,"更新失败:"+msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public boolean setUser(User_bean user) {
        this.user = user;
        if (user!=null){
            User singleUser = new User();
            singleUser.setUser_Name(user.getUser_Name());
            singleUser.setUser_Password(user.getUser_Password());
            singleUser.setUser_Phone(user.getUser_Phone());
            singleUser.setUser_ID(user.getUser_ID());
            singleUser.setUser_Address(user.getUser_Address());
            singleUser.setUser_Age(user.getUser_age());
            singleUser.setUser_Blood(user.getUser_blood());
            singleUser.setUser_Hobby(user.getUser_hobby());
            singleUser.setUser_Identity(user.getUser_Idenity());
            singleUser.setUser_Mail(user.getUser_Mail());
            singleUser.setUser_RealName(user.getUser_realname());
            singleUser.setUser_Sex(user.getUser_sex());
            try {
                Db_UserService.getInstance(context).saveUser(singleUser);
                return true;
            }catch (Exception e){
                Log.d(TAG,"exception:"+e);
                Toast.makeText(context,"存入数据库失败:"+e,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(context,"当前用户为空",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void jumpToLogin(){
        Intent intent = new Intent(this, Family_Login_Activity.class);
        startActivity(intent);
    }

    private void canNotModifyToast(){
        Toast.makeText(context,"当前信息不允许修改",Toast.LENGTH_SHORT).show();
    }

    private void illegalStatus(){
        Toast.makeText(context,"请重新登陆再试",Toast.LENGTH_SHORT).show();
    }
}
