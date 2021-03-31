package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mushroom_other_characteristics.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.ringsNumberFromId

class MushroomOtherCharacteristicsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mushroom_other_characteristics)
        configureMenu(this, otherCharacteristicsMenu)
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        otherCharacteristicsMenu.setSelection(0)
    }

    private fun configureButtons() {
        otherCharacteristicsNextButton.setOnClickListener {
            val mushroomInfo =
                intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
            mushroomInfo["ring-number"] =
                ringsNumberFromId[ringsNumber.checkedRadioButtonId] ?: return@setOnClickListener
            if (bruises.isChecked) {
                mushroomInfo["bruises"] = "t"
            } else {
                mushroomInfo["bruises"] = "f"
            }
            val intent = Intent(this, DetectionResultActivity::class.java)
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }

        otherCharacteristicsBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}