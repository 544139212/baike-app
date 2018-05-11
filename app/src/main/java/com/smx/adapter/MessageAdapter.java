package com.smx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smx.ChatActivity;
import com.smx.R;
import com.smx.dto.BillListWsDTO;
import com.smx.util.RandomStringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class MessageAdapter extends ArrayAdapter<BillListWsDTO> {

    Context context;
    int resource;
    List<BillListWsDTO> objects;

    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BillListWsDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MessgaeViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            newsViewHolder = new MessgaeViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (MessgaeViewHolder)convertView.getTag();
        }
        try {
            Picasso.with(convertView.getContext()).load("https://unsplash.it/400/800/?random").into(newsViewHolder.ivMessageImg);
            newsViewHolder.tvMessagePublisher.setText(RandomStringUtil.getRandomJianHan(5));
            newsViewHolder.tvMessageTitle.setText(RandomStringUtil.getRandomJianHan(50));
            newsViewHolder.tvMessageDate.setText("10-26");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("MESSAGE_ID", objects.get(position).getDate());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class MessgaeViewHolder {
        @BindView(R.id.iv_message_img)
        ImageView ivMessageImg;
        @BindView(R.id.tv_message_publisher)
        TextView tvMessagePublisher;
        @BindView(R.id.tv_message_title)
        TextView tvMessageTitle;
        @BindView(R.id.tv_message_date)
        TextView tvMessageDate;

        public MessgaeViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
