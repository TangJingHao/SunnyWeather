package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/12 17:58
 */

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private val weatherService=ServiceCreator.create(WeatherService::class.java)
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailyWeather(lng, lat).await()
    suspend fun getRealtimeWeather(lng:String,lat:String)= weatherService.getRealtimeWeather(lng, lat).await()

    /**
     *  定义一个Call的扩展函数，Call的上下文是retrofit2,泛型T为interface内部定义好的方法
     */
    private suspend fun <T> Call<T>.await(): T {
        //返回suspendCoroutine函数来挂起协程
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null){continuation.resume(body)}
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}