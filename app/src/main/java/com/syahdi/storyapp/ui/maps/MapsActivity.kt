package com.syahdi.storyapp.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.syahdi.storyapp.R
import com.syahdi.storyapp.databinding.ActivityMapsBinding
import com.syahdi.storyapp.data.local.preferences.UserPreferences
import com.syahdi.storyapp.data.response.ListStoryItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MapsViewModel::class.java]
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun addManyMarker(token: String) {
        mapsViewModel.getAllStoriesWithLocation(token)
        mapsViewModel.stories.observe(this) { items ->
            setStoryData(items)
        }
    }

    private fun setStoryData(items: List<ListStoryItem>?) {
        items?.forEach { item ->
            val latLng = LatLng(item.lat!!.toDouble(), item.lon!!.toDouble())
            mMap.addMarker(MarkerOptions().position(latLng).title(item.name))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }

        val token = UserPreferences.getString("token", "")
        if (token != "") {
            addManyMarker(token)
        }
    }

}