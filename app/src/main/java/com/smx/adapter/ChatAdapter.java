package com.smx.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smx.R;
import com.smx.dto.MessageWsDTO;
import com.smx.util.FaceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vivo on 2017/10/1.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    List<MessageWsDTO> objects;

    public ChatAdapter(@NonNull Context context, @NonNull List<MessageWsDTO> objects) {
        super();
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == 0) {
            final MsgViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
                newsViewHolder = new MsgViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (MsgViewHolder) convertView.getTag();
            }
            try {
                Picasso.with(convertView.getContext()).load("https://unsplash.it/400/800/?random").into(newsViewHolder.civAvator);
                newsViewHolder.tvMessage.setText(FaceUtil.convertNormalStringToSpannableString(context, 50, 50, objects.get(position).getMessage()));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (type == 1) {
            final MsgViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
                newsViewHolder = new MsgViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (MsgViewHolder) convertView.getTag();
            }
            try {
                newsViewHolder.tvMessage.setText(FaceUtil.convertNormalStringToSpannableString(context, 50, 50, objects.get(position).getMessage()));
                Picasso.with(convertView.getContext())
                        .load("https://unsplash.it/400/800/?random")
                        .into(newsViewHolder.civAvator);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } /*else if (type == 2) {
            final DateViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                newsViewHolder = new DateViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (DateViewHolder) convertView.getTag();
            }
            newsViewHolder.tvDate.setText("2017-10-25 17:00:12");
        }*/
        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        /*if (position % 3 == 1) {
            return 0;
        } else if (position % 3 == 2) {
            return 1;
        } else {
            return 2;
        }*/
        SharedPreferences preferences = context.getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
        String phone = preferences.getString("O_PHONE", "");
        if (objects.get(position).getoPhone().equals(phone)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 3;
    }


    @Override
    public boolean isEnabled(int position) {
//        return super.isEnabled(position);
        return false;
    }

    public class MsgViewHolder {
        @BindView(R.id.civ_avator)
        ImageView civAvator;
        @BindView(R.id.tv_message)
        TextView tvMessage;

        public MsgViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class DateViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;

        public DateViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
