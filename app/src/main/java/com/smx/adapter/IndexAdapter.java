package com.smx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smx.R;
import com.smx.dto.LinkWsDTO;
import com.smx.util.RandomStringUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class IndexAdapter extends BaseAdapter {

    Context context;
    List<LinkWsDTO> objects;

    public  IndexAdapter(@NonNull Context context, @NonNull List<LinkWsDTO> objects) {
        super();
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LinkWsDTO dto = objects.get(position);
        int type = getItemViewType(position);
        if (type == 0) {
            final IndexViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_0, parent, false);
                newsViewHolder = new IndexViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (IndexViewHolder) convertView.getTag();
            }
            try {
                newsViewHolder.tvTitle.setText(RandomStringUtil.getRandomJianHan(50));
                ImageAdapter imageAdapter = new ImageAdapter(context, R.layout.item_img_index, Arrays.asList("1,2,3,4,5".split(",", 3)));
                newsViewHolder.gvImg.setAdapter(imageAdapter);
                newsViewHolder.tvPublisher.setText(RandomStringUtil.getRandomJianHan(5));
                newsViewHolder.tvDate.setText("45分钟前");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (type == 1) {
            final ClipViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_1, parent, false);
                newsViewHolder = new ClipViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (ClipViewHolder) convertView.getTag();
            }
            try {
                newsViewHolder.tvTitle.setText(RandomStringUtil.getRandomJianHan(50));
                newsViewHolder.tvPublisher.setText(RandomStringUtil.getRandomJianHan(5));
                newsViewHolder.tvDate.setText("刚刚");
                Picasso.with(convertView.getContext()).load("https://unsplash.it/400/800/?random").into(newsViewHolder.ivImg);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
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
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 2;
    }

    public class IndexViewHolder {
        /*@BindView(R.id.iv_index_img)
        ImageView ivIndexImg;
        @BindView(R.id.tv_index_publisher)
        TextView tvIndexPublisher;
        @BindView(R.id.tv_index_date)
        TextView tvIndexDate;
        @BindView(R.id.tv_index_share)
        TextView tvIndexShare;*/

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.gv_img)
        GridView gvImg;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;

        public IndexViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class ClipViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;
        @BindView(R.id.iv_img)
        ImageView ivImg;

        public ClipViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
