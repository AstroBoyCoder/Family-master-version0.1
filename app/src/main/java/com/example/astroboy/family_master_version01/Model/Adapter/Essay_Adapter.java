package com.example.astroboy.family_master_version01.Model.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.astroboy.family.GreenDao.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.astroboy.family_master_version01.Model.Bean.Essay_Bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_UserService;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.View.CustomView.PopDialog.GlobalDialog;
import com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.R.id.comment;
import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;
import static com.lzy.imagepicker.ImagePicker.TAG;

/**
 * Created by AstroBoy on 2016/12/24.
 */

public class Essay_Adapter extends BaseAdapter {

    private Context context;
    private List<Essay_Bean> essayData;
    private LayoutInflater mInflater;
    private Handler mHandler;
    private String reCode = "";
    private String url = "http://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=http%3A%2F%2Fup.qqjia.com%2Fz%2F04%2Ftu5475_10.jpg&thumburl=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D4131325112%2C3955040644%26fm%3D21%26gp%3D0.jpg";
    //private String imgUrl = "http://123.57.255.1:8068/group1/M00/02/B9/ezn_AVap8DmADopDAAGtRCAjGyg590.jpg";
    public void setData(List<Essay_Bean> essayData) {
        this.essayData = essayData;
        notifyDataSetChanged();
    }

    public Essay_Adapter(Context context, List<Essay_Bean> essayData) {
        this.context = context;
        this.essayData = essayData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return essayData.size();
    }

    @Override
    public Essay_Bean getItem(int position) {
        return essayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_evaluate, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        Essay_Bean item = getItem(position);

        holder.content.setText(item.getEssay_Content());
        holder.username.setText(item.getUser_Name());
        holder.createTime.setText(item.getPublish_Date());
        holder.familyName.setText("来自家庭:"+item.getFM_Name());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.more(view,position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.delete(view,position);
            }
        });
        //holder.grade.setRating(item.grade);

        //setImage(context, holder.avatar, item.avatar == null ? null : item.avatar.smallPicUrl);
        setImage(context,holder.avatar,url);
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        String imageDetails = item.getEssay_IMG();
        if (imageDetails != null) {
            //for (EvaluationPic imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                Log.d("imgUrl", Constant.servlet_ip_short+imageDetails);
                info.setThumbnailUrl(Constant.servlet_ip_short+imageDetails);
                info.setBigImageUrl(Constant.servlet_ip_short+imageDetails);
                //info.setThumbnailUrl(imgUrl);
                //info.setThumbnailUrl(imgUrl);
                imageInfo.add(info);
            //}
        }
        holder.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));

        if (item.getReviews() == null || item.getReviews().size()==0) {
        holder.comments.setVisibility(View.GONE);
        } else {
            holder.comments.setVisibility(View.VISIBLE);
            holder.comments.setAdapter(new CommentsAdapter(context, item.getReviews()));
        }
        return convertView;
    }

    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)//
                .placeholder(R.drawable.ic_default_color)//
                .error(R.drawable.ic_default_color)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    private class ViewHolder implements View.OnClickListener {

        TextView content;
        TextView familyName;
        NineGridView nineGrid;
        TextView username;
        TextView createTime;
        RatingBar grade;
        CircleImageView avatar;
        ListView comments;
        ImageView more;
        TextView delete;

        private PopupWindow window;
        private PopupWindow editWindow;
        private View rootView;
        private int position1;

        public ViewHolder(View convertView) {
            uiHandler();
            rootView = convertView;
            init(rootView);
        }

        private void init(View convertView) {
            content = (TextView) convertView.findViewById(R.id.tv_content);
            familyName = (TextView) convertView.findViewById(R.id.tv_familyName);
            nineGrid = (NineGridView) convertView.findViewById(R.id.nineGrid);
            username = (TextView) convertView.findViewById(R.id.tv_username);
            createTime = (TextView) convertView.findViewById(R.id.tv_createTime);
            grade = (RatingBar) convertView.findViewById(R.id.rb_grade);
            avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            comments = (ListView) convertView.findViewById(R.id.lv_comments);
            more = (ImageView) convertView.findViewById(R.id.more);
            delete = (TextView) convertView.findViewById(R.id.delete);
        }

        public void more(View view, int position) {
            position1 = position;
            View popupView = mInflater.inflate(R.layout.popup_reply, null);
            popupView.findViewById(R.id.favour).setOnClickListener(this);
            popupView.findViewById(comment).setOnClickListener(this);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setOutsideTouchable(true);
            window.setFocusable(true);
            window.setAnimationStyle(R.style.popup_more_anim);
            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            popupView.measure(0, 0);
            int xoff = -popupView.getMeasuredWidth();
            int yoff = -(popupView.getMeasuredHeight() + view.getHeight()) / 2;
            window.showAsDropDown(view, xoff, yoff);
        }

        public void delete(View view, int position) {
            position1 = position;
            final GlobalDialog delDialog = new GlobalDialog(context);
            delDialog.setCanceledOnTouchOutside(true);
            delDialog.getTitle().setText("提示");
            delDialog.getContent().setText("确定删除吗?");
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
                    delDialog.dismiss();
                }
            });
            delDialog.show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.favour:
                    Toast.makeText(context, "赞+1", Toast.LENGTH_SHORT).show();
                    if (window != null) window.dismiss();
                    break;
                case comment:
                    final View editView = mInflater.inflate(R.layout.replay_input, null);
                    editWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    editWindow.setOutsideTouchable(true);
                    editWindow.setFocusable(true);
                    editWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    Button send_btn = (Button) editView.findViewById(R.id.send_msg);
                    final EditText replyEdit = (EditText) editView.findViewById(R.id.reply);
                    replyEdit.setFocusable(true);
                    replyEdit.requestFocus();
                    // 以下两句不能颠倒
                    editWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    editWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                    // 显示键盘
                    final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    send_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("ESSAY_ADAPTER", String.valueOf(essayData.get(position1).getEssay_ID()));
                            final String essay_ID = String.valueOf(essayData.get(position1).getEssay_ID());
                            final String review = replyEdit.getText().toString();
                            final String UID = Family_Fragment_Switcher_Activity.UID;
                            final String FM_ID = String.valueOf(essayData.get(position1).getFM_ID());
                            final int addReviewPosition = essayData.get(position1).getReviews().size()+1;
                            final Essay_Bean e = new Essay_Bean();
                            Toast.makeText(context, "发送评论", Toast.LENGTH_SHORT).show();
                             if (review.length()>1&&!UID.equals("")&&!FM_ID.equals("")&&!essay_ID.equals("")){
                                 new Thread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             Map<String,String> data = new HashMap<>();
                                             data.put("Essay_ID",essay_ID);
                                             data.put("review",review);
                                             data.put("User_ID",UID);
                                             data.put("FM_ID",FM_ID);
                                             reCode = Post_userLogin.getStringCha(Constant.essay_addReview,data);//向服务器提交用户上传请求
                                             Log.d(TAG, "postEssayReviewThread result:" + reCode);
                                             JSONObject json = new JSONObject(reCode);
                                             String flag = json.getString("flag");
                                             if (flag.equals("1")){

                                                 /*Comment_Bean comment = new Comment_Bean();
                                                 List<Comment_Bean> r = new ArrayList<Comment_Bean>();
                                                 User user = queryUser();
                                                 comment.setEssay_ID(essay_ID);
                                                 comment.setUser_Name(user.getUser_Name());
                                                 comment.setFM_ID(Integer.parseInt(FM_ID));
                                                 comment.setReview(review);
                                                 comment.setUser_ID(Integer.parseInt(UID));
                                                 //comment.setId("");*/
                                                 //Log.d(TAG,"SavedComment:"+comment);
                                                 //r.add(comment);
                                                 //essayData.add(position1,e.setReviews(r));
                                                 Message msg = new Message();//消息处理机制
                                                 msg.what = 1;
                                                 mHandler.sendMessage(msg);
                                                 //setData(essayData);
                                             }
                                             //json.get
                                            /* Message msg = new Message();//消息处理机制
                                             Bundle bundle = new Bundle();
                                             bundle.putString("response", reCode);
                                             Log.d("postEssayThread", String.valueOf(bundle));
                                             msg.what = 1;
                                             msg.setData(bundle);
                                             mHandler.sendMessage(msg);*/
                                         } catch (IOException | JSONException e) {
                                             /*e.printStackTrace();
                                             Message msg = new Message();
                                             msg.what = 2;
                                             msg.obj = e;
                                             mHandler.sendMessage(msg);*/
                                         } catch (org.json.JSONException e1) {
                                             e1.printStackTrace();
                                         }
                                     }
                                 }).start();
                             }else {
                                 Toast.makeText(context,"缺少必要发表数据",Toast.LENGTH_SHORT).show();
                             }
                            if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                            if (editWindow != null) editWindow.dismiss();
                        }
                    });
                    editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                        }
                    });
                    if (window != null) window.dismiss();
                    break;
            }
        }
    }

    private User queryUser(){
        User currentUser = new User();
        List<User> users = Db_UserService.getInstance(context).loadAllUser();
        try {
            for (User single : users){
                if (single.getUser_ID()==Integer.parseInt(UID)){
                    currentUser = single;
                }
            }
        }catch (Exception e){
            Toast.makeText(context,"查找用户失败:"+e,Toast.LENGTH_SHORT).show();
        }
        return currentUser;
    }

    private void uiHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //new Family_dongtai_Fragment().onResume();
                        break;
                }
        }
        };
    }
}
