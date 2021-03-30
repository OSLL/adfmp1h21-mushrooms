package ru.ifmo.mushrooms

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_place_description.*
import ru.ifmo.mushrooms.util.PlaceInfo
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.deserializePlaceInfo
import java.io.File

class PlaceDescriptionActivity : AppCompatActivity() {
    private var placeInfo: PlaceInfo? = null
    private val mutex = "placeInfoMutex"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_description)

        configureMenu(this, placeDescriptionMenu)
        addPlaceInfo()
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        placeDescriptionMenu.setSelection(0)
    }

    private fun configureButtons() {
        showOnMapButton.setOnClickListener {
            val intent = Intent(this, PlaceOnMapActivity::class.java)
            val info = synchronized(mutex) { placeInfo }
            if (info != null) {
                intent.putExtra("name", info.name)
                intent.putExtra("latitude", info.latitude)
                intent.putExtra("longitude", info.longitude)
            }
            startActivity(intent)
        }
    }

    private fun addPlaceInfo() {
        val placeName = intent.getStringExtra("placeName")
        placeDescriptionTitle.text = placeName

        val dir = File(cacheDir, "placesInfo")
        if (!dir.exists()) {
            return
        }

        val filesList = dir.listFiles() ?: return

        for (file in filesList) {
            val placeInfo = deserializePlaceInfo(file) ?: continue
            if (placeInfo.name == placeName) {
                commentBody.text = placeInfo.comment
                val drawable = Drawable.createFromPath(placeInfo.image)
                drawable?.let { placeDescriptionImage.setImageDrawable(it) }
                synchronized(mutex) { this.placeInfo = placeInfo }
            }
        }
    }
}