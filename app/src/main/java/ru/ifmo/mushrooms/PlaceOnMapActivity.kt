package ru.ifmo.mushrooms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_place_on_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.setStartPosition

class PlaceOnMapActivity : AppCompatActivity() {
    private val REQ_LOCATION_CODE = 1
    private val MAP_ZOOM = 18.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_on_map)

        placeOnMapTitle.text = intent.getStringExtra("name")
        configureMenu(this, placeOnMapMenu)
        configureMap()
        addMarker()
    }

    override fun onResume() {
        super.onResume()
        mapForPlace.onResume()
        placeOnMapMenu.setSelection(0)
    }


    override fun onPause() {
        super.onPause()
        mapForPlace.onPause()
    }


    private fun configureMap() {
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mapForPlace.setTileSource(TileSourceFactory.MAPNIK)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOCATION_CODE
            )
        } else {
            setStartPosition(this, mapForPlace, MAP_ZOOM)
        }
    }


    private fun addMarker() {
        val name = intent.getStringExtra("name")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        if (name == null) {
            return
        }
        val marker = Marker(mapForPlace)
        marker.title = name
        marker.position = GeoPoint(latitude, longitude)
        mapForPlace.overlays.add(marker)
        marker.showInfoWindow()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQ_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setStartPosition(
                        this,
                        mapForPlace,
                        MAP_ZOOM
                    )
                }
            }
        }
    }
}