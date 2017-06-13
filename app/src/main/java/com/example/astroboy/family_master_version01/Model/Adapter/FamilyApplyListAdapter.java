package com.example.astroboy.family_master_version01.Model.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astroboy.family_master_version01.Model.Bean.FamilyApplyList_bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.RoundImageView;
import com.example.astroboy.family_master_version01.View.CustomView.PopDialog.GlobalDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;
import static com.lzy.imagepicker.ImagePicker.TAG;

/**
 * Created by AstroBoy on 2016/12/27.
 */

public class FamilyApplyListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FamilyApplyList_bean> families;
    private Context context;


    public FamilyApplyListAdapter(List<FamilyApplyList_bean> families, Context context) {
        this.families = families;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<FamilyApplyList_bean> families){
        this.families = families;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return families.size();
    }

    @Override
    public Object getItem(int i) {
        return families.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.listview_apply_item, viewGroup, false);
            view.setTag(new FamilyApplyListAdapter.ViewHolder(view));
        }
        final FamilyApplyListAdapter.ViewHolder holder = (FamilyApplyListAdapter.ViewHolder) view.getTag();

        holder.homeName.setText("家庭名称:"+families.get(i).getFM_Name());
        holder.homeDetail.setText("申请人:"+families.get(i).getUser_Name());



        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ignoreOrAgree(view,i,"agree");
            }
        });

        holder.ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ignoreOrAgree(view,i,"ignore");
            }
        });


        return view;

    }

    public class ViewHolder implements View.OnClickListener {

        RoundImageView homeIcon;
        TextView homeName,homeDetail;
        Button agree,ignore;

        private PopupWindow window;
        private int position1;
        private String reCode = "";

        public ViewHolder(View view) {
            init(view);
        }

        private void init(View v) {
            homeIcon = (RoundImageView) v.findViewById(R.id.article_shot_img);
            homeName = (TextView) v.findViewById(R.id.family_title);
            homeDetail = (TextView) v.findViewById(R.id.family_address);
            agree = (Button) v.findViewById(R.id.agree);
            ignore = (Button) v.findViewById(R.id.ignore);
        }



        public void ignoreOrAgree(View view, final int position, final String type) {
            position1 = position;
            final GlobalDialog delDialog = new GlobalDialog(context);
            delDialog.setCanceledOnTouchOutside(true);
            delDialog.getTitle().setText("提示");
            if (type.equals("agree")) {
                delDialog.getContent().setText("确定同意吗?");
            }else if (type.equals("ignore")){
                delDialog.getContent().setText("确定忽略吗?");
            }
            delDialog.setLeftBtnText("取消");
            delDialog.setRightBtnText("确定");
            delDialog.setLeftOnclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                    delDialog.dismiss();
                }
            });
            delDialog.setRightOnclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
                    postIgnoreOrAgreeThread(type,families.get(position).getFM_ID(),families.get(position).getMember_ID());
                    delDialog.dismiss();
                }
            });
            delDialog.show();
        }

        private void postIgnoreOrAgreeThread(final String type, final int FM_ID, final int MEM_ID) {
            final Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                            families.remove(position1);
                            setData(families);
                            break;
                        case 2:
                            Toast.makeText(context,"服务器更改失败,请重试",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(context,"失败,请联网重试",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, String> data = new HashMap<>();
                        data.put("User_ID", UID);
                        data.put("FM_ID", String.valueOf(FM_ID));
                        data.put("Member_ID", String.valueOf(MEM_ID));
                        Log.d(TAG, "CONTENT:"+data);
                        if (type.equals("agree")) {
                            reCode = Post_userLogin.getStringCha(Constant.family_AgreeFamily, data);//向服务器提交用户上传请求
                        }else if (type.equals("ignore")){
                            reCode = Post_userLogin.getStringCha(Constant.family_IgnoreFamily, data);//向服务器提交用户上传请求
                        }
                        JSONObject json = new JSONObject(reCode);
                        Message msg = new Message();
                        if (json.get("flag").equals("1")){
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }else{
                            msg.what = 2;
                            mHandler.sendMessage(msg);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();

        }



        @Override
        public void onClick(View view) {

        }
    }
}
