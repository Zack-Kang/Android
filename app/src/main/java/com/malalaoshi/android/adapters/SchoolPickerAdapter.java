package com.malalaoshi.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malalaoshi.android.R;
import com.malalaoshi.android.core.base.MalaBaseAdapter;
import com.malalaoshi.android.entity.School;

/**
 * Created by kang on 16/1/5.
 */
public class SchoolPickerAdapter extends MalaBaseAdapter<School> {

    public SchoolPickerAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(int position, ViewGroup parent) {
        View convertView = View.inflate(context, R.layout.item_city_gridview, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city_name);
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    protected void fillView(int position, View convertView, School data) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvCityName.setText(data.getName());
    }

    class ViewHolder {
        public TextView tvCityName;
    }
}
