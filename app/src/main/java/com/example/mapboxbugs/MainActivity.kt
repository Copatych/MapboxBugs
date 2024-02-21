package com.example.mapboxbugs

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.expressions.generated.Expression
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
            Point.fromLngLat(-122.48404, 37.830497)
        )

        mapView.mapboxMap.loadStyle(Style.LIGHT) { style ->
            val pointsSource = "pointsSource"
            style.addSource(geoJsonSource(pointsSource) {
                featureCollection(
                    FeatureCollection.fromFeatures(
                        listOf(
                            Feature.fromGeometry(
                                LineString.fromLngLats(points.subList(0, 2))
                            ).apply { addStringProperty("type", Type.RED.name) },
                            Feature.fromGeometry(
                                LineString.fromLngLats(points.subList(1, 3))
                            ).apply { addStringProperty("type", Type.GREEN.name) },
                            Feature.fromGeometry(
                                LineString.fromLngLats(points.subList(2, 4))
                            ).apply { addStringProperty("type", Type.BLUE.name) },
                            Feature.fromGeometry(
                                LineString.fromLngLats(points.subList(3, 5))
                            ).apply { addStringProperty("type", null) },
                            Feature.fromGeometry(
                                LineString.fromLngLats(points.subList(4, 6))
                            ),
                        )
                    )
                )
            })

            fun Expression.ExpressionBuilder.getColor(type: Type) {
                stop {
                    literal(type.name)
                    color(type.color)
                }
            }

            style.addLayer(
                lineLayer("pointsLayer", pointsSource) {
                    lineWidth(10.0)

                    // there is IncorrectNumberOfArgumentsInExpression
                    lineColor(
                        match {
                            get("type")
                            getColor(Type.RED)
                            getColor(Type.GREEN)
                            getColor(Type.BLUE)

                            color(Color.BLACK)
                        }
                    )

                    // there is no IncorrectNumberOfArgumentsInExpression
//                    lineColor(
//                        match {
//                            get("type")
//                            stop {
//                                literal(Type.RED.name)
//                                color(Type.RED.color)
//                            }
//
//                            stop {
//                                literal(Type.GREEN.name)
//                                color(Type.GREEN.color)
//                            }
//
//                            stop {
//                                literal(Type.BLUE.name)
//                                color(Type.BLUE.color)
//                            }
//
//                            color(Color.BLACK)
//                        }
//                    )
                }
            )

            mapView.postDelayed(150) {
                val edgeInsets = EdgeInsets(100.0, 100.0, 100.0, 100.0)
                val cameraOptions = mapView.mapboxMap.cameraForCoordinates(points, edgeInsets)

                mapView.mapboxMap.setCamera(cameraOptions)
            }
        }
    }

    enum class Type(@ColorInt val color: Int) {
        RED(Color.RED),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE);
    }
}