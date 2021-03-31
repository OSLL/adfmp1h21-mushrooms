package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stalk_color.*
import ru.ifmo.mushrooms.util.configureColorsGridView
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.stalkColorFromId
import java.util.concurrent.atomic.AtomicInteger

class StalkColorActivity : AppCompatActivity() {
    private val colors = arrayOf(
        R.color.brown,
        R.color.buff,
        R.color.cinnamon,
        R.color.gray_mushroom,
        R.color.pink,
        R.color.white_mushroom,
        R.color.red,
        R.color.yellow,
        R.color.orange
    )

    private val currentColor = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stalk_color)
        configureMenu(this, stalkColorMenu)
        configureButtons()
        configureColorsGridView(this, stalkColors, colors, currentColor)
    }

    override fun onResume() {
        super.onResume()
        stalkColorMenu.setSelection(0)
    }

    private fun configureButtons() {
        stalkColorNextButton.setOnClickListener {
            val mushroomInfo =
                intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
            val idx = currentColor.get()
            mushroomInfo["stalk-color-below-ring"] =
                stalkColorFromId[colors[idx]] ?: return@setOnClickListener
            val intent = Intent(this, SporeColorActivity::class.java)
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }

        stalkColorBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}