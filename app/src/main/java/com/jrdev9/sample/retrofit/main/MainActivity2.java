package com.jrdev9.sample.retrofit.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jrdev9.sample.retrofit.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getName();
    private ActivityMain2Binding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main2);
        _binding = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        _binding.testTv.setText("Hello world");
        _binding.testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _binding.testTv.setText("Hello world2");
            }
        });

        _binding.testBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateView();
            }
        });
    }

    private void inflateView() {
        // 正确写法
        com.jrdev9.sample.retrofit.databinding.DemoLayoutBinding.inflate(LayoutInflater.from(MainActivity2.this), _binding.testLy, true);
        // // 上面代码等价于
        // View insideView2 = LayoutInflater.from(MainActivity2.this).inflate(R.layout.demo_layout, _binding.testLy, false);
        // _binding.testLy.addView(insideView2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _binding = null;
        Log.i(TAG, "onDestroy: ");
    }
}
