package com.smx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(this);
        tvCenter.setText("撰写");
        tvRight.setText("保存");
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.tv_right) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }
    }
}
