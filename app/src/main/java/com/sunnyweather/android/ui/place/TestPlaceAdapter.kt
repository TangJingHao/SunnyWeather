package com.sunnyweather.android.ui.place

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/15 20:42
 * 用BaseQ完成
 */

class TestPlaceAdapter(layoutResId:Int,private val fragment: PlaceFragment,private val placeList: MutableList<Place>): BaseQuickAdapter<Place, BaseViewHolder>(layoutResId,placeList) {

    override fun convert(holder: BaseViewHolder, item: Place) {
        holder.setText(R.id.placeName,item.name)
        holder.setText(R.id.placeAddress,item.address)
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            val place=placeList[position]
            //判断是否处于天气活动
            val activity=fragment.activity
            if(activity is WeatherActivity){
                activity.mBinding.drawLayout.closeDrawers()
                activity.mViewModel.locationLat=place.location.lat
                activity.mViewModel.locationLng=place.location.lng
                activity.mViewModel.placeName=place.name
                activity.refreshWeather()
            }else{
                val intent= Intent(fragment.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng",place.location.lng)
                    putExtra("location_lat",place.location.lat)
                    putExtra("place_name",place.name)
                }
                fragment.lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO){
                        fragment.viewModel?.savePlace(place)
                    }
                    fragment.startActivity(intent)
                    activity?.finish()
                    withContext(Dispatchers.IO){
                        fragment.viewModel?.savePlace(place)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int=placeList.size


}