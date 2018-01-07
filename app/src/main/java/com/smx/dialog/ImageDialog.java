package com.smx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.smx.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vivo on 2017/10/6.
 */

public class ImageDialog extends Dialog {

    @BindView(R.id.imageSwitcher)
    ImageSwitcher imageSwitcher;

    List<String> objects;

    /*public ImageDialog(@NonNull Context context) {
        super(context);
    }*/

    public ImageDialog(@NonNull Context context, List<String> objects) {
        super(context, R.style.AppTheme_NoTotitleBar);
        this.objects = objects;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);

        ButterKnife.bind(this);

        //如果不设置，不会报错，但dialog只有完全居中的很小一部分; 设置了之后，dialog仍旧不能全屏，左右上都有间距; 因此在构造函数中加入主题可同时解决所有问题
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.height = WindowManager.LayoutParams.MATCH_PARENT;
//        p.width = WindowManager.LayoutParams.MATCH_PARENT;
//        getWindow().setAttributes(p);

        final Context context = this.getContext();
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView i = new ImageView(context);
                i.setBackgroundColor(Color.BLACK);
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                i.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                //测试
                /*String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "baike";
                File file = new File(path);
                final File[] files = file.listFiles();
                if (files != null && files.length != 0 && files[0].canRead()) {
                    i.setImageDrawable(Drawable.createFromPath(files[0].getPath()));
                }*/

                return i;
            }
        });

        if (objects != null && !objects.isEmpty()) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "baike";
            File file = new File(path);
            final File[] files = file.listFiles();
            if (files != null && files.length != 0 && files[0].canRead()) {
                imageSwitcher.setImageURI(Uri.fromFile(files[0]));
                imageSwitcher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageSwitcher.setImageURI(Uri.fromFile(files[0]));
                    }
                });
            } else {
                imageSwitcher.setImageResource(R.mipmap.splash);
            }
        }

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right));
    }
}
