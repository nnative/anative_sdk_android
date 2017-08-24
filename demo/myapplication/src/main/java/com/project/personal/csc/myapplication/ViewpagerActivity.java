package com.project.personal.csc.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import nativesdk.ad.common.AdSdk;


/**
 * Created by csc on 15/12/21.
 */
public class ViewpagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }
        Fragment fr = AdSdk.getFeatureFragment(this);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(android.R.id.content, fr);
        ft.commit();
    }


}
