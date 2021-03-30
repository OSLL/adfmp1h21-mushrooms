package ru.ifmo.mushrooms

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_places_list.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.deserializePlaceInfo
import ru.ifmo.mushrooms.util.savePlaceInfoFromIntentData
import java.io.File

class PlacesListActivity : AppCompatActivity() {
    private val ADD_PLACE_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        loadItems()
        configureMenu(this, placesListMenu)
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        placesListMenu.setSelection(0)
    }


    private fun configureButtons() {
        addPlaceButtonList.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_CODE)
        }
        onMapButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loadItems() {
        val dir = File(cacheDir, "placesInfo")
        if (!dir.exists()) {
            return
        }

        val filesList = dir.listFiles() ?: return

        val names = mutableListOf<String>()
        val images = mutableListOf<String>()
        for (file in filesList) {
            val placeInfo = deserializePlaceInfo(file) ?: continue
            names.add(placeInfo.name)
            images.add(placeInfo.image)
        }

        val adapter = PlacesListAdapter(this, names, images)
        placesList.adapter = adapter

        placesList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PlaceDescriptionActivity::class.java)
            intent.putExtra("placeName", names[position])
            startActivity(intent)
        }
    }


    private class PlacesListAdapter constructor(
        context: Context?,
        val mushroomsNames: List<String>,
        val images: List<String>
    ) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        override fun getCount(): Int {
            return mushroomsNames.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val row = inflater.inflate(R.layout.list_item, parent, false)
            val image = images[position]
            val mushroomImageView = row.findViewById(R.id.image) as ImageView
            val drawable = Drawable.createFromPath(image)
            drawable?.let { mushroomImageView.setImageDrawable(it) }
            val mushroomNameView = row.findViewById(R.id.name) as TextView
            mushroomNameView.text = mushroomsNames[position]
            return row
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (resultCode) {
            ADD_PLACE_CODE -> {
                savePlaceInfoFromIntentData(this, data)
                loadItems()
            }
        }
    }
}