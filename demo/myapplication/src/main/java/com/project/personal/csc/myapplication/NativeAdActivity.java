package com.project.personal.csc.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nativesdk.ad.nt.AdSetting;
import nativesdk.ad.nt.INativeAd;
import nativesdk.ad.nt.NativeAd;
import nativesdk.ad.nt.NativeAdListener;


/**
 * Created by hongwu on 5/17/17.
 */

public class NativeAdActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String[] unitIds = {"eic19i15cj543c1", "369ih8bf29fed2b", "fagdajb5cjf43c1", "6295bj15295432b", "24666i15295432b"}; // 线上环境
    private INativeAd mNativeAd;
    private Button load, show;
    private Spinner mStyle;
    private FrameLayout mNativeContainer;
    private ArrayAdapter<CharSequence> styleAdapter;
    private RelativeLayout rootview, pbContainer;
    private ProgressBar progressbar;
    private String[] styles = {"Small", "Medium", "Large-image", "Large-video", "Carousel"};
    private static final String TAG = "NativeAdActivity: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativead);
        initView();
    }

    private void initView() {
        mStyle = (Spinner) findViewById(R.id.style);
        styleAdapter = ArrayAdapter.createFromResource(this, R.array.styles, android.R.layout.simple_spinner_item);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStyle.setAdapter(styleAdapter);
        mStyle.setOnItemSelectedListener(this);
        SharedPreferences sp = getSharedPreferences("native_style", MODE_PRIVATE);
        String ct = sp.getString("style", "Small");
        int position = getIndex(styles, ct);
        if (position >= 0) {
            mStyle.setSelection(position);
        }

        rootview = (RelativeLayout) findViewById(R.id.native_rootview);
        pbContainer = (RelativeLayout) findViewById(R.id.native_pb_container);
        progressbar = (ProgressBar) findViewById(R.id.native_pb);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        load = (Button) findViewById(R.id.load);
        load.setOnClickListener(this);
        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        mNativeContainer = (FrameLayout) findViewById(R.id.native_container);
        hideProgressbar();
    }

    private void initNative(int idx) {
        //For testing, please add your own test device to replace below id for fb or admob ad.
        List<String> fbids = new ArrayList<>();
        fbids.add("c993a6d1ccc75af93221ad939170a8ee");
        AdSetting.addFbTestDevices(this, fbids);
        AdSetting.addAdmobTestDevice(this, "4E924B13B234A72ABE5732B7C7B54686");

        mNativeAd = new NativeAd(this, unitIds[idx]);
        mNativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onAdLoaded() {
                hideProgressbar();
                Toast.makeText(NativeAdActivity.this, "Ad loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                hideProgressbar();
                Toast.makeText(NativeAdActivity.this, "Load error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadAd() {
        showProgressbar();
        mNativeAd.loadAd();
    }

    private void show() {
        if (mNativeAd.isLoaded()) {
            mNativeAd.show(mNativeContainer);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                loadAd();
                break;
            case R.id.show:
                show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sp = getSharedPreferences("native_style", MODE_PRIVATE);
        sp.edit().putString("style", styles[position]).apply();
        initNative(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getIndex(String[] arr, String item) {
        for (int i = 0; i < arr.length; i++) {
            if (styles[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    private void showProgressbar() {
        rootview.removeView(pbContainer);
        rootview.addView(pbContainer);
    }

    private void hideProgressbar() {
        rootview.removeView(pbContainer);
    }
}
