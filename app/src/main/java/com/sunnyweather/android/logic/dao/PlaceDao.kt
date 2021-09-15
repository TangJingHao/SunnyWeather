package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/14 21:15
 */

object PlaceDao {
    //获取SP
    private suspend fun sharePreferences()=SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",Context.MODE_PRIVATE)
    //存储地址
    suspend fun savePlace(place:Place){
        sharePreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }
    //拿取数据
    suspend fun getSavePlace():Place{
        val placeJson= sharePreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }
    //判断是否存储了
    suspend fun isPlaceSaved()= sharePreferences().contains("place")

}