package com.chen.www.myviewindicator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/14.
 */
public class VpSimpleFragment extends Fragment {
    private final static String BUNDLE_TITLE = "title";

    private static VpSimpleFragment vpSimpleFragment;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String title = bundle.getString(BUNDLE_TITLE);
        textView = new TextView(getActivity());
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public static VpSimpleFragment getFragment(String title) {
        vpSimpleFragment = new VpSimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        vpSimpleFragment.setArguments(bundle);
        return vpSimpleFragment;
    }
}
