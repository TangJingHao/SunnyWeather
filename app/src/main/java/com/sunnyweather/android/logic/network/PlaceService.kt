package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/11 15:16
 * json:https://api.caiyunapp.com/v2/place?query="北京"&token=F6FpeVaXQXs9T3A9&lang=zh_CN
 */

interface PlaceService {
    //Query中的值代替字符串拼接的query
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query")query:String): Call<PlaceResponse>
}