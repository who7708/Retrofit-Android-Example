package com.jrdev9.sample.retrofit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jrdev9.sample.retrofit.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void retrofit1Click(View view) {
        Intent intent = new Intent(this, MVPWithRetrofit1Activity.class);
        startActivity(intent);
    }

    public void retrofit2Click(View view) {
        Intent intent = new Intent(this, MVPWithRetrofit2Activity.class);
        startActivity(intent);
    }

    public void rxjavaClick(View view) {
        Intent intent = new Intent(this, MVPWithRxjavaActivity.class);
        startActivity(intent);
    }

    public void dataBindingClick(View view) {
        Intent intent = new Intent(this, DataBindingActivity.class);
        startActivity(intent);
    }
}
