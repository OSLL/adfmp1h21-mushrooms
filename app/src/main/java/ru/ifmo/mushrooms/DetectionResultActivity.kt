package ru.ifmo.mushrooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detection_result.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.detectIsEdible

class DetectionResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection_result)
        configureMenu(this, detectionResultMenu)
        configureButtons()
        addDetectionResult()
    }

    override fun onResume() {
        super.onResume()
        detectionResultMenu.setSelection(0)
    }

    private fun addDetectionResult() {
        val mushroomInfo =
            intent.getSerializableExtra("mushroomInfo") as HashMap<String, String>
        val isEdible = detectIsEdible(this, mushroomInfo)
        if (isEdible) {
            resultBody.text = "Скорее всего этот гриб съедобный"
        } else {
            resultBody.text = "Скорее всего этот гриб ядовитый"
        }
    }

    private fun configureButtons() {
        resultToStartButton.setOnClickListener {
            val intent = Intent(this, DetectMushroomActivity::class.java)
            startActivity(intent)
        }

        resultBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}