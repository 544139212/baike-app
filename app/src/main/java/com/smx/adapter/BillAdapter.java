package com.smx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.smx.R;
import com.smx.dto.DateBillWsDTO;
import com.smx.dto.BillWsDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class BillAdapter extends BaseExpandableListAdapter {

    Context context;
    List<DateBillWsDTO> objects;

    public BillAdapter(@NonNull Context context, @NonNull List<DateBillWsDTO> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getGroupCount() {
        return objects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return objects.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return objects.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return objects.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @NonNull
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final DateBillWsDTO dto = objects.get(groupPosition);
        final DateViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_date, parent, false);
            newsViewHolder = new DateViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (DateViewHolder) convertView.getTag();
        }
        try {
            newsViewHolder.tvDate.setText(dto.getDate());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return convertView;
    }

    @NonNull
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final BillWsDTO dto = objects.get(groupPosition).getList().get(childPosition);
        final BillViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_bill, parent, false);
            newsViewHolder = new BillViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (BillViewHolder) convertView.getTag();
        }
        try {
            newsViewHolder.tvTitle.setText(dto.getName());
            newsViewHolder.tvCost.setText(dto.getCost());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class DateViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;

        public DateViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class BillViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_cost)
        TextView tvCost;

        public BillViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
