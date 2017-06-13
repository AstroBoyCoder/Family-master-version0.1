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

import com.example.astroboy.family_master_version01.Model.Bean.Family_bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.RoundImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;
import static com.lzy.imagepicker.ImagePicker.TAG;

/**
 * Created by AstroBoy on 2016/12/27.
 */

public class FamilySearchListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Family_bean> families;
    private Context context;


    public FamilySearchListAdapter(List<Family_bean> families, Context context) {
        this.families = families;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<Family_bean> families){
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
            view = mInflater.inflate(R.layout.listview_search_item, viewGroup, false);
            view.setTag(new FamilySearchListAdapter.ViewHolder(view));
        }
        final FamilySearchListAdapter.ViewHolder holder = (FamilySearchListAdapter.ViewHolder) view.getTag();

        holder.homeName.setText("家庭名称:"+families.get(i).getFM_Name()+"(ID:"+families.get(i).getFM_ID()+")");
        holder.homeDetail.setText("地址:"+families.get(i).getFM_Address());

        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apply(view,i,"apply");
            }
        });


        return view;

    }

    public class ViewHolder implements View.OnClickListener {

        RoundImageView homeIcon;
        TextView homeName,homeDetail;
        Button apply;

        private PopupWindow window;
        private int position1;
        private String reCode = "";

        public ViewHolder(View view) {
            init(view);
        }

        private void init(View v) {
            homeIcon = (RoundImageView) v.findViewById(R.id.search_img);
            homeName = (TextView) v.findViewById(R.id.search_title);
            homeDetail = (TextView) v.findViewById(R.id.search_address);
            apply = (Button) v.findViewById(R.id.search_apply);

        }



        public void apply(View view, final int position, final String type) {
            position1 = position;
            Toast.makeText(context, "申请中", Toast.LENGTH_SHORT).show();
            postIgnoreOrAgreeThread(type,families.get(position).getFM_ID(),families.get(position).getMember_ID());

        }

        private void postIgnoreOrAgreeThread(final String type, final int FM_ID, final int MEM_ID) {
            final Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 0:
                            Toast.makeText(context,"服务器请求失败,请重试",Toast.LENGTH_SHORT).show();
                        case 1:
                            Toast.makeText(context,"请求发送成功",Toast.LENGTH_SHORT).show();
                            setData(families);
                            break;
                        case 2:
                            Toast.makeText(context,"您已申请此家庭,请耐心等待",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(context,"您已是此家庭的成员,不能再加入啦",Toast.LENGTH_SHORT).show();
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
                        Log.d(TAG, "CONTENT:"+data);
                        if (type.equals("apply")) {
                            reCode = Post_userLogin.getStringCha(Constant.family_apply, data);//向服务器提交用户上传请求
                        }
                        JSONObject json = new JSONObject(reCode);
                        Message msg = new Message();
                        if (json.get("flag").equals("1")){
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }else if (json.get("flag").equals("0")){
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        }else if (json.get("flag").equals("2")){
                            msg.what = 2;
                            mHandler.sendMessage(msg);
                        }else if (json.get("flag").equals("3")){
                            msg.what = 3;
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
