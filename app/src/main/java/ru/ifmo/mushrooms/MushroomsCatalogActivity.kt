package ru.ifmo.mushrooms

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.android.synthetic.main.activity_mushrooms_catalog.*
import ru.ifmo.mushrooms.util.configureMenu
import ru.ifmo.mushrooms.util.getResourceId
import java.util.*


class MushroomsCatalogActivity : AppCompatActivity() {

    var edible = "да"
    var mutex = "mutex"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mushrooms_catalog)

        loadItems("")
        configureButtons()
        configureMenu(this, mushroomsCatalogMenu)
        configureSearch()
        configureEditTextSearch()
    }

    override fun onResume() {
        super.onResume()
        mushroomsCatalogMenu.setSelection(0)
    }


    private fun configureEditTextSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadItems(p0.toString())
            }
        })

    }


    private fun loadItems(nameSubstring: String) {
        val inputStream = resources.openRawResource(R.raw.mushrooms_catalog)
        var rows: List<List<String>> = csvReader().readAll(inputStream)
        val header = rows[0].indices.map { rows[0][it] to it }.toMap()

        rows = rows.subList(1, rows.size)

        val namesList = mutableListOf<String>()
        val images = mutableListOf<List<String>>()
        for (row in rows) {
            val nameIdx = header["название"] ?: continue
            val imagesIdx = header["фото"] ?: continue
            val statusIdx = header["съедобный"] ?: continue
            if (!row[nameIdx].toLowerCase(Locale.ROOT)
                    .contains(nameSubstring.toLowerCase(Locale.ROOT))
            ) continue
            val filterValue: String
            synchronized(mutex) { filterValue = edible }
            if (row[statusIdx] == filterValue) {
                namesList.add(row[nameIdx])
                images.add(row[imagesIdx].split("\n"))
            }
        }

        val adapter = MushroomsListAdapter(this, namesList, images)
        mushroomsList.adapter = adapter

        mushroomsList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MushroomInfoActivity::class.java)
            intent.putExtra("mushroomName", namesList[position])
            startActivity(intent)
        }
    }


    private fun configureButtons() {
        toEdibleCatalog.setOnClickListener {
            synchronized(mutex) { edible = "да" }
            toEdibleCatalog.setBackgroundResource(R.drawable.rounded_btn_gray)
            toNotEdibleCatalog.setBackgroundResource(R.drawable.rounded_btn_blue)
            toEdibleCatalog.setTextColor(ContextCompat.getColor(this, R.color.black))
            toNotEdibleCatalog.setTextColor(ContextCompat.getColor(this, R.color.white))
            loadItems(searchEditText.text.toString())
        }

        toNotEdibleCatalog.setOnClickListener {
            synchronized(mutex) { edible = "нет" }
            toEdibleCatalog.setBackgroundResource(R.drawable.rounded_btn_blue)
            toNotEdibleCatalog.setBackgroundResource(R.drawable.rounded_btn_gray)
            toEdibleCatalog.setTextColor(ContextCompat.getColor(this, R.color.white))
            toNotEdibleCatalog.setTextColor(ContextCompat.getColor(this, R.color.black))
            loadItems(searchEditText.text.toString())
        }
    }


    private fun configureSearch() {
        search.setOnClickListener {
            if (searchEditText.isCursorVisible) {
                searchEditText.setText("")
                search.setBackgroundResource(R.drawable.baseline_search_white_20)
                hideKeyboard()
            } else {
                search.setBackgroundResource(R.drawable.baseline_clear_white_20)
                openKeyboard()
            }
            searchEditText.isCursorVisible = !searchEditText.isCursorVisible
        }

        searchEditText.setOnEditorActionListener(OnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                search.setBackgroundResource(R.drawable.baseline_search_white_20)
                searchEditText.isCursorVisible = false
            }
            false
        })
    }


    private fun openKeyboard() {
        searchEditText.requestFocus()
        searchEditText.isFocusableInTouchMode = !searchEditText.isCursorVisible
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_FORCED)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    private class MushroomsListAdapter constructor(
        context: Context?,
        val mushroomsNames: List<String>,
        val images: List<List<String>>
    ) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        override fun getCount(): Int {
            return mushroomsNames.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val row = inflater.inflate(R.layout.list_item, parent, false)

            if (images[position].isNotEmpty()) {
                val image = images[position][0]
                val mushroomImageView = row.findViewById(R.id.image) as ImageView
                val id = getResourceId(image, R.drawable::class.java)
                id?.let { mushroomImageView.setImageResource(it) }
            }
            val mushroomNameView = row.findViewById(R.id.name) as TextView
            mushroomNameView.text = mushroomsNames[position]
            return row
        }
    }
}