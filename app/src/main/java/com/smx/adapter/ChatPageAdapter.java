package com.smx.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by vivo on 2017/10/4.
 */

public class ChatPageAdapter extends PagerAdapter {

    List<GridView> views;
    public ChatPageAdapter(List<GridView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
//        return 0;
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
//        return false;
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }
}
