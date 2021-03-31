package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detect_mushroom.*
import ru.ifmo.mushrooms.util.capShapeFromId
import ru.ifmo.mushrooms.util.configureMenu

class DetectMushroomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detect_mushroom)

        configureMenu(this, detectMushroomMenu)
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        detectMushroomMenu.setSelection(0)
    }

    private fun configureButtons() {
        capShapeNextButton.setOnClickListener {
            val intent = Intent(this, CapColorActivity::class.java)
            val mushroomInfo = HashMap<String, String>()
            mushroomInfo["cap-shape"] =
                capShapeFromId[capShapeRadioGroup.checkedRadioButtonId] ?: return@setOnClickListener
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }
    }
}