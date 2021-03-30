package ru.ifmo.mushrooms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.deserializePlaceInfo
import ru.ifmo.mushrooms.util.savePlaceInfoFromIntentData
import ru.ifmo.mushrooms.util.setStartPosition
import java.io.File


class MainActivity : AppCompatActivity() {
    private val REQ_LOCATION_CODE = 1
    private val MAP_ZOOM = 18.0
    private val ADD_PLACE_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMap()
        configureMenu(this, placesOnMapMenu)
        configureButtons()
        // val dir = File(cacheDir, "placesInfo")
        // println(dir.deleteRecursively())
        updateMap()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        placesOnMapMenu.setSelection(0)
    }


    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    private fun configureMap() {
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        map.setTileSource(TileSourceFactory.MAPNIK)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOCATION_CODE
            )
        } else {
            setStartPosition(this, map, MAP_ZOOM)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQ_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setStartPosition(this, map, MAP_ZOOM)
                }
            }
        }
    }


    private fun configureButtons() {
        addPlaceButton.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_CODE)
        }

        toListButton.setOnClickListener {
            val intent = Intent(this, PlacesListActivity::class.java)
            startActivity(intent)
        }
    }


    private fun updateMap() {
        val dir = File(cacheDir, "placesInfo")
        if (!dir.exists()) {
            return
        }

        val filesList = dir.listFiles() ?: return
        for (file in filesList) {
            val marker = Marker(map)
            val placeInfo = deserializePlaceInfo(file) ?: continue
            marker.title = placeInfo.name
            val alt = placeInfo.latitude
            val long = placeInfo.longitude
            marker.position = GeoPoint(alt, long)
            map.overlays.add(marker)
            marker.showInfoWindow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (resultCode) {
            ADD_PLACE_CODE -> {
                savePlaceInfoFromIntentData(this, data)
                updateMap()
            }
        }
    }
}