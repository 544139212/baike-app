package com.smx.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.smx.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class FaceAdapter extends ArrayAdapter<Integer> {

    Context context;
    int resource;
    List<Integer> objects;

    public FaceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            newsViewHolder = new NewsViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (NewsViewHolder)convertView.getTag();
        }
        newsViewHolder.ivImg.setImageResource(objects.get(position));
        return convertView;
    }

    public class NewsViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;

        public NewsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
