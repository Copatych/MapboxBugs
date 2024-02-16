package com.example.mapboxbugs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapView = MapView(this)
        setContentView(mapView)
        mapView.mapboxMap
            .apply {
                setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(LONGITUDE, LATITUDE))
                        .zoom(9.0)
                        .build()
                )
            }
    }

    companion object {
        private const val LATITUDE = 40.0
        private const val LONGITUDE = -74.5
    }
}