package ru.ifmo.mushrooms.util

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import ru.ifmo.mushrooms.MainActivity
import ru.ifmo.mushrooms.MushroomsCatalogActivity


class MainActivityClasses {
    companion object {
        val activityForItemPosition = mapOf(
            1 to MainActivity::class.java,
            2 to MushroomsCatalogActivity::class.java,
            3 to MushroomsCatalogActivity::class.java
        )
    }
}

fun onClickMenu(activity: AppCompatActivity, position: Int) {
    if (position == 0) {
        return
    }
    val activityClass = (MainActivityClasses.activityForItemPosition[position]) as Class<*>?

    when (activityClass) {
        null -> return
        activity::class.java -> return
    }

    val intent = Intent(activity, activityClass)
    activity.startActivity(intent)
}


fun configureMenu(activity: AppCompatActivity, menu: Spinner) {
    menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onClickMenu(activity, position)
        }
    }
}