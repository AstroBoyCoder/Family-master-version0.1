package com.example.astroboy.family_master_version01.View.UserLoadApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.astroboy.family.GreenDao.Family;
import com.astroboy.family.GreenDao.User;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.Model.Bean.Family_bean;
import com.example.astroboy.family_master_version01.Model.MsharedPrefrenece;
import com.example.astroboy.family_master_version01.Model.Bean.User_bean;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_FamilyService;
import com.example.astroboy.family_master_version01.Util.Db_UserService;
import com.example.astroboy.family_master_version01.Util.ExitApplication;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.verifyPermissions;
import com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family_Login_Activity extends AppCompatActivity {

    private final static String TAG = "Family_Login";
    private Context context = Family_Login_Activity.this;

    Button login;
    TextView register;
    TextView forgetpsw;
    CheckBox remPsw;
    CheckBox autoLogin;
    EditText userName;
    EditText userPsw;

    Handler mHandler;
    Handler uiHandler;

    String reCode = "";
    String User_ID = "";
    String state = "4";
    String un = "";
    String pw = "";

    User_bean user;
    List<Family_bean> family;

    @Override
    protected void onResume() {
        super.onResume();
        clearData();
        if (getIntent().getStringExtra("Username")!=null){
            userName.setText(getIntent().getStringExtra("Username"));
            userPsw.setText(getIntent().getStringExtra("Password"));
        }
    }

    private void clearData() {
        reCode = "";
        User_ID = "";
        state = "4";
        un = "";
        pw = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_login_activity);
        init();
        verifyPermissions.verifyPermissions(this);
        checkUserState();
        ExitApplication.getInstance().addActivity(this);
        StatusBarUtil.setTranslucent(this, 0);
    }

    private void checkUserState() {
        String[] tempInfo = MsharedPrefrenece.GetUserLoginSetting(context);
        if (tempInfo[0] != null) {
            Log.d(TAG,"检查用户上次登录是否选择记住密码或自动登录:"+"un:"+tempInfo[0]+"pw"+tempInfo[1]+"rem:"+tempInfo[2]+"auto:"+tempInfo[3]);
            if (tempInfo[2].equals("1")&&tempInfo[3].equals("1")) {
                userName.setText(tempInfo[0]);
                userPsw.setText(tempInfo[1]);
                remPsw.setChecked(true);
                autoLogin.setChecked(true);
                jumpToMainPage();
            }else if (tempInfo[2].equals("1")){
                userName.setText(tempInfo[0]);
                userPsw.setText(tempInfo[1]);
                remPsw.setChecked(true);
            }
        }
    }

    private void init() {
        login = (Button) findViewById(R.id.login_ensureLogin_btn);
        register = (TextView) findViewById(R.id.login_register);
        forgetpsw = (TextView) findViewById(R.id.login_forgetPassword);
        userName = (EditText) findViewById(R.id.login_edit_userName);
        userPsw = (EditText) findViewById(R.id.login_edit_password);
        remPsw = (CheckBox) findViewById(R.id.login_set_remPassword);
        autoLogin = (CheckBox) findViewById(R.id.login_set_autoLogin);

        login.setOnClickListener(new loginBtnListener());
        register.setOnClickListener(new registerBtnListenter());
        forgetpsw.setOnClickListener(new forgetPswListener());
        autoLogin.setOnClickListener(new autoLoginListener());
        remPsw.setOnClickListener(new remLoginListener());
        //userName.setOnClickListener(new forgetPswListener());
        //userPsw.setOnClickListener(new forgetPswListener());
    }

    private class loginBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            un = userName.getText().toString();
            pw = userPsw.getText().toString();
            final Boolean isRem = remPsw.isChecked();
            final Boolean isAuto = autoLogin.isChecked();
            if (un.equals("")) {
                userName.setError("请输入用户名");
            } else if (pw.equals("")) {
                userPsw.setError("请输入密码");
            } else {
                userLoginThread(Constant.user_login, un, pw);
                uiHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 0:
                                Toast.makeText(context, "账号或密码错误", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                if (isRem || isAuto) {
                                    String Rem = "0";
                                    String Auto = "0";
                                    if (isRem) Rem = "1";
                                    if (isAuto) Auto = "1";
                                    MsharedPrefrenece.SetUserLoginSetting(context, un, pw, Rem, Auto);
                                    Log.d(TAG,"检查用户本次登录是否选择记住密码或自动登录:"+"un:"+un+"pw"+pw+"rem:"+Rem+"auto:"+Auto);
                                }
                                jumpToMainPage();
                                break;
                            case 2:
                                userName.setError("");
                                Toast.makeText(context, "账号不存在", Toast.LENGTH_LONG).show();
                                userName.setFocusable(true);
                                break;
                            case 5:
                                Toast.makeText(context,"登录失败:"+msg.obj,Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(context, "未成功登陆,请重试", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };

            }
        }
    }

    private class registerBtnListenter implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent in = new Intent(context, Family_Register_Activity.class);
            startActivity(in);
        }
    }

    private class forgetPswListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(context, "功能暂未开放,尽情期待~", Toast.LENGTH_LONG).show();
        }
    }

    private class autoLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!autoLogin.isChecked()) {
                remPsw.setChecked(false);
            }else {
                remPsw.setChecked(true);
            }
        }
    }

    private class remLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (autoLogin.isChecked()){
                autoLogin.setChecked(false);
            }
        }
    }

    public void userLoginThread(final String url, final String userName, final String password) {

        userLoginHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "URL:" + url + " userName:" + userName + " passWord" + password);
                    Map<String, String> data = new HashMap<>();
                    data.put("name", userName);
                    data.put("password", password);
                    reCode = Post_userLogin.getStringCha(url, data);//向服务器提交用户头像上传请求
                    Log.d(TAG, "Post userLogin result:" + reCode);//服务器端返回的结果 100 失败 200 成功
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 5;
                    msg.obj = e;
                    uiHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void userLoginHandler(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String response = msg.getData().getString("response");
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d(TAG, "Json内容" + json);
                    String Msg = json.getString("msg");
                    setUser(JSON.parseObject(json.getString("User"),User_bean.class));
                    Log.d(TAG,"User"+getUser());
                    setFamily(JSON.parseArray(json.getString("familys"),Family_bean.class));
                    Log.d(TAG,"Families"+getFamily().toString());
                    //String UserId = String.valueOf(json.getInt("UserId"));
                    String loginFlag = json.getString("loginFlag");
                    state = loginFlag;
                    if (state.equals("1"))
                        User_ID = String.valueOf(getUser().getUser_ID());
                    Log.d("解析好的数据", Msg + " " + User_ID + " " + loginFlag);

                } catch (JSONException e) {
                    Log.d(TAG, "登陆捕获异常:" + e);
                    state = "3";
                }
                Message uiMsg = new Message();//消息处理机制
                uiMsg.what = Integer.parseInt(state);
                Log.d(TAG,"uiMsg.what"+String.valueOf(uiMsg.what));
                uiHandler.sendMessage(uiMsg);
            }
        };
    }

    public User_bean getUser() {
        return user;
    }

    public void setUser(User_bean user) {
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
            Db_UserService.getInstance(context).saveUser(singleUser);
        }
    }

    public List<Family_bean> getFamily() {
        return family;
    }

    public void setFamily(List<Family_bean> family) {
        this.family = family;
        if (family!=null&&getUser()!=null){
            /*List<Family> families = Db_FamilyService.getInstance(context).loadAllFamily();
            if (families!=null){
                for (Family single : families){
                    if (single.getUser_ID()==getUser().getUser_ID()){
                        Db_FamilyService.getInstance(context).deleteFamily(single);
                    }
                }
            }*/
            Db_FamilyService.getInstance(context).deleteAllFamily();
            List<Family> families = new ArrayList<>();
            for (Family_bean family_bean : family) {
                Family single = new Family();
                single.setCreate_Time(family_bean.getCreate_Time());
                single.setFM_Address(family_bean.getFM_Address());
                single.setFM_ID(family_bean.getFM_ID());
                single.setFM_Name(family_bean.getFM_Name());
                single.setMember_ID(family_bean.getMember_ID());
                single.setMember_Type(family_bean.getId());
                single.setReserved_Tel(family_bean.getReserved_Tel());
                single.setUser_ID(family_bean.getUser_ID());
                families.add(single);
            }
            if (families.size()!=0){
                Db_FamilyService.getInstance(context).saveFamilyLists(families);
            }

        }
    }

    private void jumpToMainPage(){
        Intent in = new Intent(context, Family_Fragment_Switcher_Activity.class);
        in.putExtra("UID",User_ID);
        startActivity(in);
        finish();
    }
}
