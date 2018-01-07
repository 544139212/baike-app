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
import com.smx.dialog.ImageDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class ImageAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    List<String> objects;

    public ImageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final NewsViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            newsViewHolder = new NewsViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (NewsViewHolder)convertView.getTag();
        }

        Picasso.with(convertView.getContext()).load("https://unsplash.it/400/800/?random").into(newsViewHolder.ivImg);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog dialog = new ImageDialog(context, objects);
                dialog.show();
            }
        });

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
