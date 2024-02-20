package com.example.mapboxbugs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnNextLayout
import androidx.core.view.postDelayed
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapView = MapView(this)
        setContentView(mapView)

        val points = listOf(
            Point.fromLngLat(-122.483696, 37.833818),
            Point.fromLngLat(-122.483482, 37.833174),
            Point.fromLngLat(-122.483396, 37.8327),
            Point.fromLngLat(-122.483568, 37.832056),
            Point.fromLngLat(-122.48404, 37.831141),
            Point.fromLngLat(-122.48404, 37.830497),
            Point.fromLngLat(-122.483482, 37.82992),
            Point.fromLngLat(-122.483568, 37.829548),
            Point.fromLngLat(-122.48507, 37.829446),
            Point.fromLngLat(-122.4861, 37.828802),
            Point.fromLngLat(-122.486958, 37.82931),
            Point.fromLngLat(-122.487001, 37.830802),
            Point.fromLngLat(-122.487516, 37.831683),
            Point.fromLngLat(-122.488031, 37.832158),
            Point.fromLngLat(-122.488889, 37.832971),
            Point.fromLngLat(-122.489876, 37.832632),
            Point.fromLngLat(-122.490434, 37.832937),
            Point.fromLngLat(-122.49125, 37.832429),
            Point.fromLngLat(-122.491636, 37.832564),
            Point.fromLngLat(-122.492237, 37.833378),
            Point.fromLngLat(-122.493782, 37.833683)
        )

//        mapView.mapboxMap.getStyle { style ->
        mapView.mapboxMap.loadStyle(Style.LIGHT) { style ->
            val pointsSource = "pointsSource"
            style.addSource(geoJsonSource(pointsSource) {
                feature(
                    Feature.fromGeometry(
                        LineString.fromLngLats(points)
                    )
                )
            })

            style.addLayer(
                lineLayer("pointsLayer", pointsSource) {
                    lineWidth(10.0)
                }
            )


            fun focusPoints() {
                val edgeInsets = EdgeInsets(100.0, 100.0, 100.0, 100.0)
//                val cameraOptions = mapView.mapboxMap.cameraForCoordinates(points, edgeInsets)
//                val cameraOptions = mapView.mapboxMap.cameraForGeometry(GeometryCollection.fromGeometries(points), edgeInsets)
                val cameraOptions = mapView.mapboxMap.cameraForCoordinateBounds(
                    CoordinateBounds(
                        Point.fromLngLat(points.maxOf { it.longitude() }, points.maxOf { it.latitude() }),
                        Point.fromLngLat(points.minOf { it.longitude() }, points.minOf { it.latitude() })
                    ),
                    edgeInsets
                )

                mapView.mapboxMap.setCamera(cameraOptions)
            }

            // In the doOnNextLayout, focusPoints() method does not work correctly, the zoom is detected incorrectly.
            mapView.doOnNextLayout {
                focusPoints()
            }

            // To verify that focusPoints() works as expected after delay
            mapView.postDelayed(5000) {
                focusPoints()
            }
        }
    }
}