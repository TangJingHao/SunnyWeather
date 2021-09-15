package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/11 15:24
 */

object ServiceCreator {
    private const val BASE_URL="https://api.caiyunapp.com/"

    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun<T>create(serviceClass:Class<T>):T= retrofit.create(serviceClass)
    //指定对象类型关键字reified让泛型实例化可以获得相应的class文件
    //泛型实例化关键字（inline和reified）
    inline fun <reified T> create():T= create(T::class.java)
}