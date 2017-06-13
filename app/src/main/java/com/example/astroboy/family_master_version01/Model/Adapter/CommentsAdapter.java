package com.example.astroboy.family_master_version01.Model.Adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.astroboy.family_master_version01.Model.Bean.Comment_Bean;
import com.example.astroboy.family_master_version01.R;

import java.util.List;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/22
 * 描    述：留言回复的Adapter
 * 修订历史：
 * ================================================
 */
public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private List<Comment_Bean> replies;

    public CommentsAdapter(Context context, List<Comment_Bean> Replies) {
        this.context = context;
        this.replies = Replies;
    }

    @Override
    public int getCount() {
        return replies.size();
    }

    @Override
    public Comment_Bean getItem(int position) {
        return replies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_evaluatereply, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Comment_Bean replyItem = getItem(position);
        SpannableString msp = new SpannableString(replyItem.getUser_Name() + ":" + replyItem.getReview());
        msp.setSpan(new ForegroundColorSpan(0xff6b8747), 0, replyItem.getUser_Name().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.reply.setText(msp);
        return convertView;
    }

    private static class ViewHolder {
        TextView reply;

        public ViewHolder(View convertView) {
            init(convertView);
        }

        private void init(View convertView) {
            reply = (TextView) convertView.findViewById(R.id.tv_reply);
        }
    }
}