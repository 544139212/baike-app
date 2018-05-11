package com.smx.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.smx.WebViewActivity;
import com.smx.dto.BillListWsDTO;
import com.smx.util.RandomStringUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by vivo on 2017/10/1.
 */

public class IndexAdapter extends BaseAdapter {

    Context context;
    List<BillListWsDTO> objects;

    public  IndexAdapter(@NonNull Context context, @NonNull List<BillListWsDTO> objects) {
        super();
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BillListWsDTO dto = objects.get(position);
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
        } else if (type == 2) {
            final LinkViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_2, parent, false);
                newsViewHolder = new LinkViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (LinkViewHolder) convertView.getTag();
            }
            try {
                newsViewHolder.tvTitle.setText(RandomStringUtil.getRandomJianHan(50));
                newsViewHolder.jcVideoPlayer.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                Picasso.with(convertView.getContext())
                        .load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
                        .into(newsViewHolder.jcVideoPlayer.thumbImageView);
                newsViewHolder.tvPublisher.setText(RandomStringUtil.getRandomJianHan(5));
                newsViewHolder.tvDate.setText("2小时前");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    context.startActivity(intent);
                }
            });
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
        if (position % 3 == 0) {
            return 0;
        } else if (position % 3 == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 3;
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

    public class LinkViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.videoplayer)
        JCVideoPlayerStandard jcVideoPlayer;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;

        public LinkViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
