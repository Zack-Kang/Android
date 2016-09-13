package com.malalaoshi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malalaoshi.android.R;
import com.malalaoshi.android.core.base.MalaBaseAdapter;
import com.malalaoshi.android.core.image.MalaImageView;
import com.malalaoshi.android.entity.School;
import com.malalaoshi.android.util.LocationUtil;

/**
 * Created by kang on 16/1/5.
 */
public class SchoolAdapter extends MalaBaseAdapter<School> {

    public SchoolAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(int position, ViewGroup parent) {
        View convertView = View.inflate(context, R.layout.school_list_item, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.ivSchoolPic = (MalaImageView) convertView.findViewById(R.id.iv_school_pic);
        viewHolder.tvSchoolName = (TextView) convertView.findViewById(R.id.tv_school_name);
        viewHolder.tvSchoolAddress = (TextView) convertView.findViewById(R.id.tv_school_address);
        viewHolder.tvSchoolDistance = (TextView) convertView.findViewById(R.id.tv_school_distance);
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    protected void fillView(int position, View convertView, School data) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        String imgUrl = data.getThumbnail();
        viewHolder.ivSchoolPic.loadImage(imgUrl, R.drawable.ic_default_img);

        viewHolder.tvSchoolName.setText(data.getName());
        viewHolder.tvSchoolAddress.setText(data.getAddress());
        Double distance = data.getDistance();
        if (distance != null && distance >= 0.0D) {
            String dis = LocationUtil.formatDistance(distance);
            viewHolder.tvSchoolDistance.setText(dis);
        }
    }

    class ViewHolder {
        public MalaImageView ivSchoolPic;
        public TextView tvSchoolName;
        public TextView tvSchoolAddress;
        public TextView tvSchoolDistance;
    }
}
