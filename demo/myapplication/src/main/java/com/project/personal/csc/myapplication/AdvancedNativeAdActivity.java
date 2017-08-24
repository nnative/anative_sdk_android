package com.project.personal.csc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.ArrayList;
import java.util.List;

import nativesdk.ad.common.adapter.IAdvancedNativeAd;
import nativesdk.ad.common.adapter.INativeAd;
import nativesdk.ad.common.adapter.INativeAdLoadListener;
import nativesdk.ad.common.app.Constants;
import nativesdk.ad.common.common.utils.L;
import nativesdk.ad.common.database.AdInfo;
import nativesdk.ad.common.modules.activityad.imageloader.widget.BasicLazyLoadImageView;
import nativesdk.ad.nt.AdSetting;
import nativesdk.ad.nt.AdvancedNativeAd;
import nativesdk.ad.nt.mediation.adapter.apx.ApxMediaView;


/**
 * Created by hongwu on 5/17/17.
 */

public class AdvancedNativeAdActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AdvancedNativeAdActivity: ";
    private Context mContext;
    private static final String unitId = "fagdajb5cjf43c1";  //线上环境
    private Button load, show;
    private RelativeLayout progressbar;
    private IAdvancedNativeAd mAdvancedNativeAd;
    private FrameLayout mAdContainer;
    private final static int LOAD_AD_NUM = 1;
    private List<INativeAd> mAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_native);

        mContext = this;
        progressbar = (RelativeLayout) findViewById(R.id.advanced_pb);
        load = (Button) findViewById(R.id.load);
        load.setOnClickListener(this);
        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);
        mAdContainer = (FrameLayout) findViewById(R.id.ad_container);

        initNative();
    }

    private void initNative() {
        //For testing, please add YOUR OWN tset device to replace below id for fb or admob ad.
        List<String> fbids = new ArrayList<>();
        fbids.add("12aecd9687717842bcfa179a35f1cd99");
        AdSetting.addFbTestDevices(this, fbids);
        AdSetting.addAdmobTestDevice(this, "4E924B13B234A72ABE5732B7C7B54686");

        mAdvancedNativeAd = new AdvancedNativeAd(this, unitId); // use unitid with large image if you want image data
        mAdvancedNativeAd.setAdListener(new INativeAdLoadListener() {

            @Override
            public void onAdListLoaded(List<INativeAd> ads) {
                L.e("onAdListLoaded: size: ", ads.size());
                hideProgressbar();
                Toast.makeText(AdvancedNativeAdActivity.this, "Ad list loaded: " + ads.size(), Toast.LENGTH_SHORT).show();

                mAds = ads;
            }

            @Override
            public void onError(String error) {
                hideProgressbar();
                Toast.makeText(AdvancedNativeAdActivity.this, "Load error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAd() {
        showProgressbar();
        mAdvancedNativeAd.load(LOAD_AD_NUM);
    }

    private void showAd() {
        if (mAds != null) {
            if (mAds.size() == 1) { // single ad
                INativeAd ad = mAds.get(0);
                switch (ad.getAdType()) {
                    case Constants.NativeAdType.AD_SOURCE_APX:
                        inflateApxAdView(ad);
                        break;
                    case Constants.NativeAdType.AD_SOURCE_ADMOB_INSTALL:
                        inflateAdmobInstallAdView(ad);
                        break;
                    case Constants.NativeAdType.AD_SOURCE_ADMOB_CONTENT:
                        inflateAdmobContentAdView(ad);
                        break;
                    case Constants.NativeAdType.AD_SOURCE_FACEBOOK:
                        inflateFbNativeAdView(ad);
                        break;
                }
            } else if (mAds.size() > 1) { // multi ads

                //Todo: custom your own ui to contain multi-ads (carousel)
            }
        }
    }

    private void inflateApxAdView(INativeAd ad) {
        dumpAd(ad);

        View adView = LayoutInflater.from(mContext).inflate(R.layout.advanced_native_ad_apx, null);

        ApxMediaView nativeAdMedia = (ApxMediaView) adView.findViewById(R.id.anative_mediaview);
        nativeAdMedia.setNativeAd((AdInfo) ad.getAdObject());
        nativeAdMedia.setAutoPlay(true);

        BasicLazyLoadImageView iconView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_icon_image);
        iconView.requestDisplayURL(ad.getIconImageUrl());

        TextView titleView = (TextView) adView.findViewById(R.id.anative_ad_title);
        titleView.setText(ad.getTitle());

        TextView subtitleView = (TextView) adView.findViewById(R.id.anative_ad_subtitle_text);
        subtitleView.setText(ad.getBody());

        TextView ctaView = (TextView) adView.findViewById(R.id.anative_ad_cta_text);
        ctaView.setText(ad.getCallToActionText());

        BasicLazyLoadImageView choiceIconImage = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_choices_image);
        choiceIconImage.setImageResource(R.drawable.apx_native_ad_choices);

        ad.registerViewForInteraction(mAdContainer);
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
    }

    private void inflateAdmobInstallAdView(INativeAd ad) {
        dumpAd(ad);

        NativeAppInstallAdView adView = (NativeAppInstallAdView) LayoutInflater.from(mContext).inflate(R.layout.advanced_native_ad_apx, null);

        BasicLazyLoadImageView imageView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_mediaview);
        imageView.requestDisplayURL(ad.getCoverImageUrl());

        BasicLazyLoadImageView iconView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_icon_image);
        iconView.requestDisplayURL(ad.getIconImageUrl());
        adView.setIconView(iconView);

        TextView titleView = (TextView) adView.findViewById(R.id.anative_ad_title);
        titleView.setText(ad.getTitle());
        adView.setHeadlineView(titleView);

        TextView subtitleView = (TextView) adView.findViewById(R.id.anative_ad_subtitle_text);
        subtitleView.setText(ad.getBody());
        adView.setBodyView(subtitleView);

        TextView ctaView = (TextView) adView.findViewById(R.id.anative_ad_cta_text);
        ctaView.setText(ad.getCallToActionText());
        adView.setCallToActionView(ctaView);

        adView.setNativeAd((NativeAd) ad.getAdObject());

        ad.registerViewForInteraction(mAdContainer);
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
    }

    private void inflateAdmobContentAdView(INativeAd ad) {
        dumpAd(ad);

        NativeContentAdView adView = (NativeContentAdView) LayoutInflater.from(mContext).inflate(R.layout.advanced_native_ad_admob_content, null);

        BasicLazyLoadImageView imageView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_mediaview);
        imageView.requestDisplayURL(ad.getCoverImageUrl());
        adView.setImageView(imageView);

        BasicLazyLoadImageView iconView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_icon_image);
        iconView.requestDisplayURL(ad.getIconImageUrl());
        adView.setLogoView(iconView);

        TextView titleView = (TextView) adView.findViewById(R.id.anative_ad_title);
        titleView.setText(ad.getTitle());
        adView.setHeadlineView(titleView);

        TextView subtitleView = (TextView) adView.findViewById(R.id.anative_ad_subtitle_text);
        subtitleView.setText(ad.getBody());
        adView.setBodyView(subtitleView);

        TextView ctaView = (TextView) adView.findViewById(R.id.anative_ad_cta_text);
        ctaView.setText(ad.getCallToActionText());
        adView.setCallToActionView(ctaView);

        adView.setNativeAd((NativeAd) ad.getAdObject());

        ad.registerViewForInteraction(mAdContainer);
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
    }

    private void inflateFbNativeAdView(INativeAd ad) {
        dumpAd(ad);

        View adView = LayoutInflater.from(mContext).inflate(R.layout.advanced_native_ad_facebook, null);

        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.anative_mediaview);
        nativeAdMedia.setNativeAd((com.facebook.ads.NativeAd) ad.getAdObject());

        BasicLazyLoadImageView iconView = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_icon_image);
        iconView.requestDisplayURL(ad.getIconImageUrl());

        TextView titleView = (TextView) adView.findViewById(R.id.anative_ad_title);
        titleView.setText(ad.getTitle());

        TextView subtitleView = (TextView) adView.findViewById(R.id.anative_ad_subtitle_text);
        subtitleView.setText(ad.getBody());

        TextView ctaView = (TextView) adView.findViewById(R.id.anative_ad_cta_text);
        ctaView.setTextColor(Color.WHITE);
        ctaView.setText(ad.getCallToActionText());

        if (ad.getPrivacyIconUrl() != null) {
            L.e("privacyurl: " + ad.getPrivacyIconUrl());
            BasicLazyLoadImageView choiceIconImage = (BasicLazyLoadImageView) adView.findViewById(R.id.anative_ad_choices_image);
            choiceIconImage.requestDisplayURL(ad.getPrivacyIconUrl());
            ad.registerPrivacyIconView(choiceIconImage);
        }

        ad.registerViewForInteraction(mAdContainer);
        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                loadAd();
                break;
            case R.id.show:
                showAd();
        }
    }

    private void showProgressbar() {
        if (!progressbar.isShown()) {
            progressbar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressbar() {
        if (progressbar.isShown()) {
            progressbar.setVisibility(View.INVISIBLE);
        }
    }

    private void dumpAd(INativeAd ad) {
        L.e("adtype: " + ad.getAdType()
                + "\nimageurl: " + ad.getCoverImageUrl()
                + "\niconurl: " + ad.getIconImageUrl()
                + "\nsubtitle: " + ad.getSubtitle()
                + "\ntitle: " + ad.getTitle()
                + "\ncta: " + ad.getCallToActionText()
                + "\nid: " + ad.getId()
                + "\nbody: " + ad.getBody()
                + "\nrate: " + ad.getStarRating()
                + "\nadobject: " + ad.getAdObject()
                + "\nPrivacyIconUrl: " + ad.getPrivacyIconUrl()
                + "\nPlacementId: " + ad.getPlacementId()
                + "\nshowcount: " + ad.getShowCount()
                + "\nisshow: " + ad.isShowed()
                + "\nloadedtime: " + ad.getLoadedTime());
    }

    @Override
    protected void onDestroy() {
        mAdvancedNativeAd.destroy();
        super.onDestroy();
    }
}
