package com.example.astroboy.family_master_version01.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.astroboy.family.GreenDao.Family;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.RoundImageView;

import java.util.List;

/**
 * Created by AstroBoy on 2016/12/27.
 */

public class FamilyListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Family> families;

    public FamilyListAdapter(List<Family> families,Context context) {
        this.families = families;
        this.mInflater = LayoutInflater.from(context);
        //this.context = context;
    }

    public void setData(List<Family> families){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.listview_item,null);

        RoundImageView homeIcon;
        TextView homeName,homeDetail;

        homeIcon = (RoundImageView) v.findViewById(R.id.article_shot_img);
        homeName = (TextView) v.findViewById(R.id.family_title);
        homeDetail = (TextView) v.findViewById(R.id.family_address);
        homeName.setText("");
        homeDetail.setText("");

        homeName.setText("家庭名称:"+families.get(i).getFM_Name());
        homeDetail.setText("门牌号(ID):"+families.get(i).getFM_ID());


        return v;

    }

    public class ViewHolder implements View.OnClickListener {



        @Override
        public void onClick(View view) {

        }
    }

}
