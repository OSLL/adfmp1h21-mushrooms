package ru.ifmo.mushrooms.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File


fun saveImage(finalBitmap: Bitmap, path: File?): String {
    val dir = File(path, "placesImages")
    if (!dir.exists()) {
        dir.mkdir()
    }
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val file = File(dir, fileName)
    if (file.exists()) file.delete()
    file.createNewFile()
    file.outputStream().use { out ->
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
    }
    return file.absolutePath
}

fun getImage(file: File): Bitmap? {
    val bmOptions = BitmapFactory.Options()
    return BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return null
}