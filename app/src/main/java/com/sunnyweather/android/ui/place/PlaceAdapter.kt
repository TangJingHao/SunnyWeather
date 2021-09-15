package com.sunnyweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/12 20:46
 */

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).
        inflate(R.layout.place_item,parent,false)
        val holder=ViewHolder(view)
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
                val intent=Intent(parent.context,WeatherActivity::class.java).apply {
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
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount()=placeList.size
}