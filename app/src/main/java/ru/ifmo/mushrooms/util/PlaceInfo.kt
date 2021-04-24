package ru.ifmo.mushrooms.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import java.io.File

data class PlaceInfo(
    val name: String,
    val comment: String,
    val image: String,
    val latitude: Double,
    val longitude: Double
)

fun PlaceInfo.serializePlaceInfo(path: File?): File {
    val dir = File(path, "placesInfo")
    if (!dir.exists()) {
        dir.mkdir()
    }
    val fileName = System.currentTimeMillis().toString() + ".txt"
    val file = File(dir, fileName)
    if (file.exists()) file.delete()
    file.createNewFile()
    val info = Gson().toJson(this)
    file.bufferedWriter().use { out ->
        out.write(info)
    }
    return file
}

fun deserializePlaceInfo(file: File): PlaceInfo? {
    val text = file.readText()
    return Gson().fromJson<PlaceInfo?>(text, PlaceInfo::class.java)
}

fun savePlaceInfoFromIntentData(activity: AppCompatActivity, data: Intent) {
    val name = data.getStringExtra("name")
    val comment = data.getStringExtra("comment")
    val image = data.getStringExtra("image")
    if (name == null || comment == null || image == null) {
        return
    }
    val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val location: Location?
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ) {
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    } else {
        return
    }
    val longitude: Double = location?.longitude ?: return
    val latitude: Double = location.latitude
    val placeInfo = PlaceInfo(
        name,
        comment,
        image,
        latitude,
        longitude
    )
    placeInfo.serializePlaceInfo(activity.cacheDir)
}