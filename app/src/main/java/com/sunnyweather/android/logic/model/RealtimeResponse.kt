package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/13 14:54
 */

data class RealtimeResponse(val status:String,val result:Result){
    data class Result(val realtime:Realtime)//最外层封装
    //json解析不规范，用注解
    data class Realtime(val skycon:String,val temperature:Float
    ,@SerializedName("air_quality")val airQuality:AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn:Float)
}