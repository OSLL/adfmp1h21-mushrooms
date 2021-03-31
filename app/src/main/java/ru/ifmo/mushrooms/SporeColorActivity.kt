package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_spore_color.*
import ru.ifmo.mushrooms.util.configureColorsGridView
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.sporeColorFromId
import java.util.concurrent.atomic.AtomicInteger

class SporeColorActivity : AppCompatActivity() {
    private val colors = arrayOf(
        R.color.brown,
        R.color.black,
        R.color.chocolate,
        R.color.buff,
        R.color.white_mushroom,
        R.color.green,
        R.color.purple,
        R.color.yellow,
        R.color.orange
    )

    private val currentColor = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spore_color)
        configureMenu(this, sporeColorMenu)
        configureButtons()
        configureColorsGridView(this, sporeColors, colors, currentColor)
    }

    override fun onResume() {
        super.onResume()
        sporeColorMenu.setSelection(0)
    }

    private fun configureButtons() {
        sporeColorNextButton.setOnClickListener {
            val mushroomInfo =
                intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
            val idx = currentColor.get()
            mushroomInfo["spore-print-color"] =
                sporeColorFromId[colors[idx]] ?: return@setOnClickListener
            val intent = Intent(this, MushroomOtherCharacteristicsActivity::class.java)
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }

        sporeColorBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}