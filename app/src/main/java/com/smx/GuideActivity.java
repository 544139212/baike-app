package com.smx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.smx.adapter.GuideAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends BasicActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ButterKnife.bind(this);

        viewPager.setOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        button.setOnClickListener(this);

        GuideAdapter guideAdapter = new GuideAdapter(getSupportFragmentManager());
        viewPager.setAdapter(guideAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int item = 0;
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i).getId() == checkedId) {
                item = i;
                break;
            }
        }
        viewPager.setCurrentItem(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        int id = radioGroup.getChildAt(i).getId();
        radioGroup.check(id);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            SharedPreferences preferences = getSharedPreferences("GUIDE", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("INDICATOR", "Y");
            editor.commit();

            goAndFinish(GuideActivity.this, MainActivity.class);
        }
    }
}
