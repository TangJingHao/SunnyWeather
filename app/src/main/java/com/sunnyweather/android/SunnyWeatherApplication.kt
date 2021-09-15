package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/11 15:00
 */

class SunnyWeatherApplication:Application() {
    companion object{
        //加注释来不让出现内存泄露报错，全局只存在一份实例
        @SuppressLint
        lateinit var context:Context
        const val TOKEN="F6FpeVaXQXs9T3A9"
    }

    override fun onCreate() {
        super.onCreate()
        //其实是调用了get方法
        context=applicationContext
    }
}