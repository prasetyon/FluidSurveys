package com.mobile4day.tryzoom;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import pl.polidea.view.ZoomView;

public class MainActivity extends AppCompatActivity {

    private ZoomView zoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_main, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        zoomView = new ZoomView(this);
        zoomView.addView(v);

        LinearLayout llView = (LinearLayout) findViewById(R.id.llView);
        llView.setVisibility(View.GONE);

        LinearLayout main_container = (LinearLayout) findViewById(R.id.llZoom);
        main_container.addView(zoomView);
    }
}
