package com.example.mapboxbugs

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

class FirstFragment : Fragment(R.layout.fragment_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.nextButton).setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

        val customMapView = view.findViewById<CustomMapView>(R.id.customMapView)

        customMapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(LONGITUDE, LATITUDE))
                .zoom(9.0)
                .build()
        )
    }

    companion object {
        private const val LATITUDE = 40.0
        private const val LONGITUDE = -74.5
    }
}