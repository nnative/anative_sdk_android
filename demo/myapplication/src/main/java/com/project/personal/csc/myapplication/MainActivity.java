package com.project.personal.csc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import nativesdk.ad.common.AdSdk;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onResume() {
        super.onResume();

        AdSdk.preloadMarketData(this.getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.market).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdSdk.showAppMarket(MainActivity.this);
            }
        });
        findViewById(R.id.market_fragmet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdSdk.setAppMarketFragmentMode(MainActivity.this, true);
                Intent i = new Intent(MainActivity.this, ViewpagerActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.nativeAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NativeAdActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.advancedNativeAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdvancedNativeAdActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
