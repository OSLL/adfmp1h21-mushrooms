package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stalk_shape.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.stalkShapeFromId

class StalkShapeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stalk_shape)

        configureMenu(this, stalkShapeMenu)
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        stalkShapeMenu.setSelection(0)
    }

    private fun configureButtons() {
        stalkShapeNextButton.setOnClickListener {
            val mushroomInfo =
                intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
            mushroomInfo["stalk-shape"] =
                stalkShapeFromId[stalkShapeRadioGroup.checkedRadioButtonId]
                    ?: return@setOnClickListener
            val intent = Intent(this, StalkColorActivity::class.java)
            intent.putExtra("mushroomInfo", mushroomInfo)
            startActivity(intent)
        }

        stalkShapeBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}