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
import android.widget.TextView;

import com.smx.R;
import com.smx.WebViewActivity;
import com.smx.dto.LinkWsDTO;
import com.smx.util.RandomStringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by vivo on 2017/10/1.
 */

public class SongAdapter extends ArrayAdapter<LinkWsDTO> {

    Context context;
    int resource;
    List<LinkWsDTO> objects;

    public SongAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<LinkWsDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LinkWsDTO dto = objects.get(position);
        final LinkViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            newsViewHolder = new LinkViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (LinkViewHolder) convertView.getTag();
        }
        try {
            newsViewHolder.tvTitle.setText(RandomStringUtil.getRandomJianHan(50));
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
                intent.putExtra("type", "link");
                intent.putExtra("link", dto);
                context.startActivity(intent);
            }
        });
        return convertView;
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
