package ru.ifmo.mushrooms.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import ru.ifmo.mushrooms.R
import java.util.concurrent.atomic.AtomicInteger


fun setMark(context: AppCompatActivity, imageView: ImageView) {
    val checkId = if (imageView.isSelected) {
        R.drawable.baseline_check_white_24
    } else {
        R.drawable.baseline_check_black_24
    }
    imageView.setImageDrawable(ContextCompat.getDrawable(context, checkId))
}

fun configureColorsGridView(
    context: AppCompatActivity,
    gridView: GridView,
    colors: Array<Int>,
    currentColor: AtomicInteger
) {
    val adapter = ColorsGridAdapter(context, colors)
    gridView.adapter = adapter

    gridView.setOnItemClickListener { _, view, position, _ ->
        gridView.children.forEach {
            val color = it.findViewById(R.id.colorButton) as ImageView
            color.setImageDrawable(null)
        }
        val color = view.findViewById(R.id.colorButton) as ImageView
        setMark(context, color)
        currentColor.set(position)
    }
}

class ColorsGridAdapter constructor(
    private val context: Context,
    private val colors: Array<Int>
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return colors.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val row = inflater.inflate(R.layout.color_item, parent, false)
        val color = row.findViewById(R.id.colorButton) as ImageView
        color.setBackgroundColor(ContextCompat.getColor(context, colors[position]))
        if (position != 0) color.setImageDrawable(null)
        if (colors[position] == R.color.black) color.isSelected = true
        return row
    }
}