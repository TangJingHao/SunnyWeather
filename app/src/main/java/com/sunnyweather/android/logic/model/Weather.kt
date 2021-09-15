package com.sunnyweather.android.logic.model

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/13 15:08
 */

data class Weather(val realtime:RealtimeResponse.Realtime,val daily:DailyResponse.Daily)
