package com.smx;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.smx.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.btnAgree)
    Button btnAgree;

    @BindView(R.id.btnRefuse)
    Button btnRefuse;

    @BindView(R.id.iv_loading)
    ImageView ivLoading;

    @BindView(R.id.tv_loading)
    TextView tvLoading;

    AnimationDrawable animationDrawable;

    @BindView(R.id.spinner)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = DialogUtils.getWinDialog(getBaseContext());
                dialog.show();
            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = DialogUtils.getCustomDialog(getBaseContext());
                dialog.show();
            }
        });

        ivLoading.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) ivLoading.getDrawable();
        animationDrawable.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                ivLoading.setVisibility(View.GONE);
                tvLoading.setVisibility(View.GONE);
            }
        }, 5000);

        List<String> objects = new ArrayList<String>();
        objects.add("下拉选项一");
        objects.add("下拉选项二");
        objects.add("下拉选项三");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, objects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
