package com.smx;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.summary)
    TextView summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(this);
        tvCenter.setText("关于我们");

        String str = String.format("<a href=\"%s\">%s</a>", "http://www.baidu.com", "http://www.baidu.com");
        summary.setText(Html.fromHtml(getString(R.string.summary, str)));
        summary.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            finish();
        }
    }
}
