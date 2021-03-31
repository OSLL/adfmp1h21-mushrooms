package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cap_color.*
import ru.ifmo.mushrooms.util.capColorFromId
import ru.ifmo.mushrooms.util.configureColorsGridView
import ru.ifmo.mushrooms.util.configureMenu
import java.util.concurrent.atomic.AtomicInteger

class CapColorActivity : AppCompatActivity() {
    private val colors = arrayOf(
        R.color.brown,
        R.color.buff,
        R.color.cinnamon,
        R.color.gray_mushroom,
        R.color.white_mushroom,
        R.color.green,
        R.color.pink,
        R.color.purple,
        R.color.red,
        R.color.yellow
    )

    private val currentColor = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cap_color)

        configureMenu(this, capColorMenu)
        configureButtons()
        configureColorsGridView(this, capColors, colors, currentColor)
    }

    override fun onResume() {
        super.onResume()
        capColorMenu.setSelection(0)
    }

    private fun configureButtons() {
        capColorNextButton.setOnClickListener {
            val mushroomInfo =
                intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
            val idx = currentColor.get()
            mushroomInfo["cap-color"] = capColorFromId[colors[idx]] ?: return@setOnClickListener
            val intent = Intent(this, StalkShapeActivity::class.java)
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }

        capColorBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}