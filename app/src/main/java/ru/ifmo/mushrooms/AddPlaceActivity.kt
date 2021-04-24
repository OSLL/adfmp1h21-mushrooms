package ru.ifmo.mushrooms

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_add_place.*
import ru.ifmo.mushrooms.util.checkPlaceNameIsCorrect
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.saveImage
import java.util.*


class AddPlaceActivity : AppCompatActivity() {
    private val CAMERA_REQ_CODE = 1
    private val RANDOM_STRING_LEN = 8
    private val SAVE_RETURN_CODE = 1
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        configureEditText()
        configureMenu(this, addPlaceMenu)
        configureButtons()
    }

    override fun onResume() {
        super.onResume()
        addPlaceMenu.setSelection(0)
    }

    private fun configureEditText() {
        val randomString = UUID.randomUUID().toString().substring(0, RANDOM_STRING_LEN)
        placeName.setText(randomString)
        placeName.setSelection(randomString.length)
    }

    private fun showPlaceNameError(msg: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Ошибка!")
            .setMessage(msg)
            .setPositiveButton("ОК") { dialog, _ ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun configureButtons() {
        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQ_CODE
                )
            } else {
                takePhoto()
            }
        }

        saveButton.setOnClickListener {
            val name = placeName.text.toString()
            val maybeError = checkPlaceNameIsCorrect(name)
            if (maybeError != null) {
                showPlaceNameError(maybeError.msg)
                return@setOnClickListener
            }
            val comment = comment.text.toString()
            val imageFileName = saveImage(cameraButton.drawable.toBitmap(), cacheDir)
            val intent = Intent()
            intent.putExtra("name", name)
            intent.putExtra("comment", comment)
            intent.putExtra("image", imageFileName)
            setResult(SAVE_RETURN_CODE, intent)
            finish()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                }
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                val photo = data?.extras?.get("data") as Bitmap? ?: return
                cameraButton.setImageBitmap(photo)
            }
        }
    }
}