package com.jrdev9.sample.retrofit.main;

import android.os.Bundle;

import com.jrdev9.sample.retrofit.R;

public class MVPWithRxjavaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        new MainPresenterWithRxjava(this);
    }
}
