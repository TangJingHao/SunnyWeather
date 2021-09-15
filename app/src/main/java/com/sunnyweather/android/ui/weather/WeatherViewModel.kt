package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/13 15:49
 */

class WeatherViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<Location>()
    var locationLng=""
    var locationLat=""
    var placeName=""

    val weatherLiveData=Transformations.switchMap(locationLiveData){location->
        Repository.refreshWeather(location.lng,location.lat)
    }
    //暴露给外部进行刷新数据
    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value= Location(lng, lat)
    }
}