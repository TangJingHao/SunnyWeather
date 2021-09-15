package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/11 15:11
 */


data class PlaceResponse(val status: String, val places: List<Place>)

//注释是为了建立相应的映射关系
data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)