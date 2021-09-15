package com.sunnyweather.android.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityWeatherBinding
    val mViewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(mBinding.root)
        initData()
        //返回一个result对象,result在仓库层内部封装了一个weather
        mViewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                showMsg("无法获取天气信息")
                result.exceptionOrNull()?.printStackTrace()
            }
            //刷新结束，刷新消失
            mBinding.swipeRefresh.isRefreshing=false
            showMsg("刷新数据成功！")
        })
        mBinding.swipeRefresh.setColorSchemeResources(R.color.fresh)
        refreshWeather()
        mViewModel.refreshWeather(mViewModel.locationLng, mViewModel.locationLat)
        mBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
    }

    private fun initData() {
        if (mViewModel.locationLng.isEmpty()) {
            mViewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (mViewModel.locationLat.isEmpty()) {
            mViewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (mViewModel.placeName.isEmpty()) {
            mViewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
    }

    fun refreshWeather(){
        mViewModel.refreshWeather(mViewModel.locationLng,mViewModel.locationLat)
        //显示下拉显示条
        mBinding.swipeRefresh.isRefreshing=true
    }

    /**
     * 沉浸式布局
     */
    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT
        }
        mBinding = ActivityWeatherBinding.inflate(LayoutInflater.from(this))
        mBinding.now.navBtn.setOnClickListener {
            mBinding.drawLayout.openDrawer(GravityCompat.START)
        }
        mBinding.drawLayout.addDrawerListener(object:DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                //关闭滑动窗口后隐藏输入法
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }

    private fun showWeatherInfo(weather: Weather) {
        mBinding.now.placeName.text = mViewModel.placeName
        Log.d("====",mBinding.now.placeName.text.toString())
        /**
         * now.xml布局加载
         */
        drawNowXml(weather)
        /**
         * forecast.xml布局加载
         */
        drawForecastXml(weather)
        /**
         * life_index.xml布局加载
         */
        drawLifeIndex(weather)
    }

    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun drawNowXml(weather: Weather) {
        val realtime = weather.realtime
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        mBinding.now.currentTemp.text = currentTempText
        mBinding.now.currentSky.text = getSky(realtime.skycon).info
        mBinding.now.currentAQI.text = currentPM25Text
        mBinding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
    }

    private fun drawForecastXml(weather: Weather) {
        mBinding.forecast.forecastLayout.removeAllViews()
        val daily = weather.daily
        val days = weather.daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val sky = getSky(skycon.value)
            val temperature = daily.temperature[i]
            val tempText = "${temperature.min}~${temperature.max} ℃"
            //第二个参数获取本地国家代码
            val forecastItemBinding:ForecastItemBinding= ForecastItemBinding.inflate(LayoutInflater.from(this))
            forecastItemBinding.dateInfo.text = simpleDateFormat.format(skycon.date)
            forecastItemBinding.skyIcon.setImageResource(sky.icon)
            forecastItemBinding.skyInfo.text = sky.info
            forecastItemBinding.temperatureInfo.text = tempText
            mBinding.forecast.forecastLayout.addView(forecastItemBinding.root)

        }
    }

    private fun drawLifeIndex(weather: Weather){
        val lifeIndex = weather.daily.lifeIndex
        //只展现当天数据
        mBinding.lifeIndex.coldRiskText.text=lifeIndex.coldRisk[0].desc
        mBinding.lifeIndex.dressingText.text=lifeIndex.dressing[0].desc
        mBinding.lifeIndex.ultravioletText.text=lifeIndex.ultraviolet[0].desc
        mBinding.lifeIndex.carWashingText.text=lifeIndex.carWashing[0].desc
        mBinding.weatherLayout.visibility=View.VISIBLE
    }
}