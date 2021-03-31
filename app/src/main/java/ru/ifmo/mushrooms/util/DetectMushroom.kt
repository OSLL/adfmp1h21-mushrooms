package ru.ifmo.mushrooms.util

import androidx.appcompat.app.AppCompatActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.ifmo.mushrooms.R


fun detectIsEdible(activity: AppCompatActivity, mushroomInfo: Map<String, String>): Boolean {
    val inputStream = activity.resources.openRawResource(R.raw.mushrooms_dataset)
    var rows: List<List<String>> = csvReader().readAll(inputStream)
    val header = rows[0].indices.map { rows[0][it] to it }.toMap()
    rows = rows.subList(1, rows.size)

    val classIdx = header["class"] ?: return false

    val counters = Array(rows.size) { 0 }
    for ((key, value) in mushroomInfo) {
        val propertyIdx = header[key] ?: continue
        rows.indices.forEach { i ->
            if (rows[i][propertyIdx] == value) {
                counters[i] = counters[i] + 1
            }
        }
    }
    counters.sort()
    val max = counters[counters.size - 1]
    val edibleCount = counters.indices.count { counters[it] == max && rows[it][classIdx] == "e" }
    val poisonousCount = counters.indices.count { counters[it] == max && rows[it][classIdx] == "p" }

    return edibleCount > poisonousCount
}