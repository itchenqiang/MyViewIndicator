package com.chen.www.myviewindicator.act;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chen.www.myviewindicator.R;
import com.chen.www.myviewindicator.fragment.VpSimpleFragment;
import com.chen.www.myviewindicator.view.ViewIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewIndicator viewindicator;
    private ViewPager viewpager;
    private ArrayList<VpSimpleFragment> mFragments = new ArrayList<VpSimpleFragment>();
    private PagerAdapter mAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        viewpager.setAdapter(mAdapter);
        viewindicator.setViewPager(viewpager, 0);
    }

    private void initData() {
        List<String> titles = Arrays.asList("条目1", "条目2", "条目3", "条目4", "条目5", "条目6");
        viewindicator.setTabTitle(titles);
        for (String title : titles) {
            VpSimpleFragment fragment = VpSimpleFragment.getFragment(title);
            mFragments.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
    }

    private void initView() {
        viewindicator = (ViewIndicator) findViewById(R.id.viewindicator);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }


}
