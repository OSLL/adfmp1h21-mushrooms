package ru.ifmo.mushrooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.android.synthetic.main.activity_mushroom_info.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.getResourceId

class MushroomInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mushroom_info)

        configureMenu(this, mushroomInfoMenu)
        addInfo()
    }

    private fun setInfo(
        i: Int,
        header: Map<String, Int>,
        row: List<String>,
        images: MutableList<SlideModel>
    ) {
        when (i) {
            header["описание"] -> descriptionBody.text = row[i]
            header["шляпка"] -> mushroomCapBody.text = row[i]
            header["ножка"] -> stipeBody.text = row[i]
            header["спороносный слой"] -> sporeLayerBody.text = row[i]
            header["споровый порошок"] -> sporeBody.text = row[i]
            header["съедобный"] -> edibleBody.text = row[i]
            header["распространение"] -> habitatBody.text = row[i]
            header["источник"] -> srcBody.text = row[i]
            header["фото"] -> {
                for (photoName in row[i].split("\n").filter { it != "" }) {
                    val id = getResourceId(photoName, R.drawable::class.java)
                    images.add(SlideModel(id, ScaleTypes.CENTER_CROP))
                }
            }
        }
    }


    private fun addInfo() {
        val mushroomName = intent.getStringExtra("mushroomName")
        val inputStream = resources.openRawResource(R.raw.mushrooms_catalog)
        var rows: List<List<String>> = csvReader().readAll(inputStream)
        val header = rows[0].indices.map { rows[0][it] to it }.toMap()

        rows = rows.subList(1, rows.size)

        val images = mutableListOf<SlideModel>()
        for (row in rows) {
            val nameIdx = header["название"] ?: continue
            if (row[nameIdx] != mushroomName) continue
            for (i in row.indices) {
                setInfo(i, header, row, images)
            }
        }

        if (images.size == 0) {
            images.add(SlideModel(R.drawable.nophoto, ScaleTypes.CENTER_CROP))
        }
        imageSlider.setImageList(images)
        mushroomNameView.text = mushroomName
    }

    override fun onResume() {
        super.onResume()
        mushroomInfoMenu.setSelection(0)
    }
}