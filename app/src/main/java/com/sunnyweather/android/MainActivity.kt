package com.sunnyweather.android

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(R.layout.activity_main)
    }

    /**
     * 沉浸式布局
     */
    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}