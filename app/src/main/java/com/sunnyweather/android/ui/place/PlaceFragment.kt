package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.FragmentPlaceBinding
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author EDG Clearlove7
 * @Date 2021/9/12 20:56
 */

class PlaceFragment : Fragment() {
    //返回函数最后一行

    val viewModel by lazy { activity?.let { ViewModelProvider(it).get(PlaceViewModel::class.java) } }
    private lateinit var adapter: PlaceAdapter
    private lateinit var binding: FragmentPlaceBinding
    private var key = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            if (activity is MainActivity && viewModel?.isPlaceSaved() == true) {
                withContext(Dispatchers.IO) {
                    val place = viewModel?.getSavedPlace()
                    if (place != null) {
                        val intent = Intent(context, WeatherActivity::class.java).apply {
                            putExtra("location_lng", place.location.lng)
                            putExtra("location_lat", place.location.lat)
                            putExtra("place_name", place.name)
                        }
                        startActivity(intent)
                        key = 1
                    }
                }
            }
        }
        if (key == 1) {
            return
        }

        val layoutManger = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManger
        //不推荐
        adapter = PlaceAdapter(this, viewModel!!.placeList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener { text: Editable? ->
            val content = text.toString()
            if (content.isNotEmpty()) {
                viewModel?.searchPlaces(content)
            } else {
                viewModel?.placeList?.clear()
                adapter.notifyDataSetChanged()
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
            }
        }
        /**
         * 时刻监听数据
         */
        viewModel?.placeLiveData?.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel?.apply {
                    placeList.clear()
                    placeList.addAll(places)
                    adapter.notifyDataSetChanged()
                }
            } else {
                showMsg("未能查询到任何地点")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun showMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}