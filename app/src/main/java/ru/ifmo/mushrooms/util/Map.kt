package ru.ifmo.mushrooms.util

import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

fun setStartPosition(context: AppCompatActivity, map: MapView, zoom: Double) {
    val mapController = map.controller
    val provider = GpsMyLocationProvider(context)
    provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
    val locationOverlay = MyLocationNewOverlay(map)
    locationOverlay.enableFollowLocation()
    map.overlayManager.add(locationOverlay)
    map.controller.setCenter(locationOverlay.myLocation)
    mapController.setZoom(zoom)
}