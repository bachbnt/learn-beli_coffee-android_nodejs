package com.belicoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.belicoffee.Model.Option;

import com.belicoffee.Model.Option;
import com.belicoffee.R;

import java.util.ArrayList;

public class OptionAdapter extends ArrayAdapter<Option> {
    Context context;
    int resource;
    ArrayList<Option> objects;

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }

    public OptionAdapter(Context context, int resource, ArrayList<Option> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resource, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon_option);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_option);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Option option=objects.get(position);
        viewHolder.ivIcon.setImageResource(option.getIconImage());
        viewHolder.tvTitle.setText(option.getTitleText());
        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }
}

