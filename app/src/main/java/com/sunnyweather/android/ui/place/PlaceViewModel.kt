package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/12 19:14
 */

class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    //对相应的数据进行相应的保存
    val placeList=ArrayList<Place>()
    //产生一个可以被观察的对象
    val placeLiveData=Transformations.switchMap(searchLiveData){query->
        Repository.searchPlaces(query)
    }
    //外部进行相应的调用
    fun searchPlaces(query:String){
        searchLiveData.value=query
    }

    suspend fun savePlace(place: Place)=Repository.savePlace(place)
    suspend fun getSavedPlace() =Repository.getSavedPlace()
    suspend fun isPlaceSaved()=Repository.isPlaceSaved()
}