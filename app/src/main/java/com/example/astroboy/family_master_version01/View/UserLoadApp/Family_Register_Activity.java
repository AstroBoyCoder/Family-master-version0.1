package com.example.astroboy.family_master_version01.View.UserLoadApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.ExitApplication;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Family_Register_Activity extends AppCompatActivity {

    private final static String TAG = "Family_Register";

    private EditText editUserName, editPassword, editPasswordAgain;
    private String userName, passWord;
    private String reCode = "";
    private String status = "4";

    private Handler mHandler;
    private Handler uiHandler;

    @Override
    protected void onResume() {
        super.onResume();
        clearData();
    }

    private void clearData() {
        editUserName.setText("");
        editPassword.setText("");
        editPasswordAgain.setText("");
        reCode = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_register_activity);
        StatusBarUtil.setTranslucent(this, 0);
        ExitApplication.getInstance().addActivity(this);
        checkRegistHandler();
        initView();
    }

    private void initView() {
        editUserName = (EditText) findViewById(R.id.register_edit_userName);
        editPassword = (EditText) findViewById(R.id.register_edit_password);
        editPasswordAgain = (EditText) findViewById(R.id.register_edit_password_again);
        Button ensureRegist = (Button) findViewById(R.id.register_ensureRegister_btn);
        ensureRegist.setOnClickListener(new registBtnListener());
    }

    private class registBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String tempUserName = editUserName.getText().toString();
            String tempPassword = editPassword.getText().toString();
            String tempPasswordAgain = editPasswordAgain.getText().toString();
            Log.d(TAG,"UN"+tempUserName+"PW"+tempPassword+"PWA"+tempPasswordAgain);

            if (tempUserName.equals("") || tempPassword.equals("") || tempPasswordAgain.equals("")) {
                if (tempUserName.equals(""))
                    editUserName.setError("请输入用户名");
                if (tempPassword.equals(""))
                    editPassword.setError("请输入密码");
                if (tempPasswordAgain.equals(""))
                    editPasswordAgain.setError("请输入密码");
            } else if (tempUserName.length() <= 11) {
                if ((tempPassword.length()>5)&&(tempPassword.length()<13)) {
                    if (tempPassword.equals(tempPasswordAgain)) {
                        userName = tempUserName;
                        passWord = tempPassword;
                        sendRegisterRequest(Constant.user_register);
                    } else {
                        editPasswordAgain.setError("前后密码不一致");
                    }
                }else {
                    editPassword.setError("密码长度不符合条件");
                }

            }else if (tempUserName.length() > 11) {
                editUserName.setError("手机号不能超过11位");
            } else {
                Toast.makeText(Family_Register_Activity.this, "输入不符合条件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendRegisterRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "URL:" + url + "userName:" + userName + " passWord" + passWord);
                    Map<String, String> data = new HashMap<>();
                    data.put("phone", userName);
                    data.put("password", passWord);
                    reCode = Post_userLogin.getStringCha(url, data);//向服务器提交用户头像上传请求
                    Log.d(TAG, "Post register result:" + reCode);//服务器端返回的结果 100 失败 200 成功
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String response = msg.getData().getString("response");
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d(TAG, "Json内容" + json);
                    String Msg = json.getString("msg");
                    String regFlag = json.getString("regFlag");
                    //1成功 2用户存在 3 0 注册失败
                    Log.d("解析好的数据", "msg:" + Msg + "regFlag:" + regFlag);
                    if (!regFlag.equals("")){
                        status = regFlag;
                    }else {
                        status = "3";
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "注册捕获异常:" + e);
                    status = "4";
                }
                Message uiMsg = new Message();//消息处理机制
                uiMsg.what = Integer.parseInt(status);
                Log.d("uiMsg.what", String.valueOf(uiMsg.what));
                uiHandler.sendMessage(uiMsg);
            }
        };
    }

    private void checkRegistHandler() {
        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        JumpToLogin(userName,passWord);
                        break;
                    case 2:
                        Toast.makeText(Family_Register_Activity.this,"用户已存在!",Toast.LENGTH_SHORT).show();
                        clearData();
                        break;
                    case 3:
                        Toast.makeText(Family_Register_Activity.this,"服务端注册失败,请重试~",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(Family_Register_Activity.this,"注册异常,请重试~",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Family_Register_Activity.this,"注册异常,请联网重试~",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void JumpToLogin(String un,String pw) {
        Intent in = new Intent(this,Family_Login_Activity.class);
        in.putExtra("Username",un);
        in.putExtra("Password",pw);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearData();
        finish();
    }
}
