package com.smx.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smx.R;
import com.smx.dto.LocationWsDTO;
import com.smx.util.RandomStringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/1.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    List<LocationWsDTO> objects;

    public ChatAdapter(@NonNull Context context, @NonNull List<LocationWsDTO> objects) {
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
                newsViewHolder.tvMessage.setText(convertNormalStringToSpannableString(RandomStringUtil.getRandomJianHan(50)));
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
                newsViewHolder.tvMessage.setText(convertNormalStringToSpannableString(RandomStringUtil.getRandomJianHan(50)));
                Picasso.with(convertView.getContext())
                        .load("https://unsplash.it/400/800/?random")
                        .into(newsViewHolder.civAvator);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (type == 2) {
            final DateViewHolder newsViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                newsViewHolder = new DateViewHolder(convertView);
                convertView.setTag(newsViewHolder);
            } else {
                newsViewHolder = (DateViewHolder) convertView.getTag();
            }
            newsViewHolder.tvDate.setText("2017-10-25 17:00:12");
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
        if (position % 3 == 1) {
            return 0;
        } else if (position % 3 == 2) {
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

    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    private CharSequence convertNormalStringToSpannableString(String message) {
        // TODO Auto-generated method stub
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);

        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            // k = str2.lastIndexOf("[");
            // Log.i("way", "str2.length = "+str2.length()+", k = " + k);
            // str2 = str2.substring(k, m);
            if (m - k < 8) {
                if (true) {//map中包含str2这个key
                    int face = R.mipmap.ic_launcher_round;
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), face);
                    if (bitmap != null) {
                        ImageSpan localImageSpan = new ImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE);
                        value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }

    //解析表情
    private CharSequence convertNormalStringToSpannableString2(String message) {
        // TODO Auto-generated method stub
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);

        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            // k = str2.lastIndexOf("[");
            // Log.i("way", "str2.length = "+str2.length()+", k = " + k);
            // str2 = str2.substring(k, m);
            if (m - k < 8) {
                if (true) {//map中包含str2这个key
                    int face = R.mipmap.ic_launcher_round;
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), face);
                    if (bitmap != null) {
                        int rawHeigh = bitmap.getHeight();
                        int rawWidth = bitmap.getHeight();
                        int newHeight = 30;
                        int newWidth = 30;
                        // 计算缩放因子
                        float heightScale = ((float) newHeight) / rawHeigh;
                        float widthScale = ((float) newWidth) / rawWidth;
                        // 新建立矩阵
                        Matrix matrix = new Matrix();
                        matrix.postScale(heightScale, widthScale);
                        // 设置图片的旋转角度
                        // matrix.postRotate(-30);
                        // 设置图片的倾斜
                        // matrix.postSkew(0.1f, 0.1f);
                        // 将图片大小压缩
                        // 压缩后图片的宽和高以及kB大小均会变化
                        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
                        ImageSpan localImageSpan = new ImageSpan(context, newBitmap, ImageSpan.ALIGN_BASELINE);
                        value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }

}
