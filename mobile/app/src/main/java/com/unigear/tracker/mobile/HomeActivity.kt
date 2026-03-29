package com.unigear.tracker.mobile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private data class EquipmentItem(
        val id: Int,
        val name: String,
        val category: String,
        val status: String,
        val location: String,
        val description: String,
        val specs: String
    )

    private val equipment = listOf(
        EquipmentItem(
            1,
            "Microscope",
            "Microscopes",
            "Available",
            "UniGear Office - Main Lobby",
            "High-quality compound microscope for biology and lab sessions.",
            "Magnification: 40x-1000x, LED illumination"
        ),
        EquipmentItem(
            2,
            "Beaker Set",
            "Glassware",
            "Available",
            "Chemistry Lab - Storage Room",
            "Borosilicate beaker set for chemistry experiments.",
            "50ml, 100ml, 250ml, 500ml, 1000ml"
        ),
        EquipmentItem(
            3,
            "Oscilloscope",
            "Electronics",
            "In Use",
            "Electronics Lab - Workbench 3",
            "Digital storage oscilloscope for signal analysis.",
            "Bandwidth: 100MHz, 4 channels"
        ),
        EquipmentItem(
            4,
            "Lab Coat",
            "Safety Equipment",
            "Available",
            "Safety Equipment Storage",
            "Standard laboratory coat for protection.",
            "Size: L, Cotton"
        ),
        EquipmentItem(
            5,
            "Test Tubes",
            "Glassware",
            "Available",
            "Chemistry Lab - Storage Room",
            "Set of borosilicate glass test tubes.",
            "50 pieces, 18mm x 150mm"
        ),
        EquipmentItem(
            6,
            "Digital Multimeter",
            "Electronics",
            "Available",
            "Electronics Lab - Tool Cabinet",
            "Professional multimeter for electrical measurements.",
            "AC/DC Voltage, Auto-ranging"
        ),
        EquipmentItem(
            7,
            "Safety Goggles",
            "Safety Equipment",
            "Available",
            "Safety Equipment Storage",
            "Impact-resistant goggles with anti-fog coating.",
            "Polycarbonate lens, UV protection"
        ),
        EquipmentItem(
            8,
            "Compound Microscope",
            "Microscopes",
            "In Use",
            "Biology Lab - Station 5",
            "Advanced microscope for academic research.",
            "40x-2000x, phase contrast"
        ),
        EquipmentItem(
            9,
            "Hydrochloric Acid",
            "Chemicals",
            "Available",
            "Chemistry Lab - Chemical Cabinet",
            "Laboratory reagent for controlled chemistry procedures.",
            "Concentration: 1M, volume: 500ml"
        )
    )

    private var selectedCategory = "all"
    private lateinit var searchInput: EditText
    private lateinit var listContainer: LinearLayout
    private lateinit var noResultsText: TextView

    private lateinit var btnCatAll: Button
    private lateinit var btnCatMicroscopes: Button
    private lateinit var btnCatGlassware: Button
    private lateinit var btnCatElectronics: Button
    private lateinit var btnCatSafety: Button
    private lateinit var btnCatChemicals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        searchInput = findViewById(R.id.etSearch)
        listContainer = findViewById(R.id.llEquipmentList)
        noResultsText = findViewById(R.id.tvNoResults)

        btnCatAll = findViewById(R.id.btnCatAll)
        btnCatMicroscopes = findViewById(R.id.btnCatMicroscopes)
        btnCatGlassware = findViewById(R.id.btnCatGlassware)
        btnCatElectronics = findViewById(R.id.btnCatElectronics)
        btnCatSafety = findViewById(R.id.btnCatSafety)
        btnCatChemicals = findViewById(R.id.btnCatChemicals)

        findViewById<View>(R.id.btnNavCatalog).setOnClickListener { }
        findViewById<View>(R.id.btnNavRequests).setOnClickListener {
            startActivity(Intent(this, MyRequestsActivity::class.java))
        }
        findViewById<View>(R.id.btnNavProfile).setOnClickListener {
            Toast.makeText(this, "Profile screen is coming soon", Toast.LENGTH_SHORT).show()
        }

        setupCategoryFilters()
        setupSearch()
        updateCategoryStyles()
        renderEquipment()
    }

    private fun setupSearch() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                renderEquipment()
            }
        })
    }

    private fun setupCategoryFilters() {
        btnCatAll.setOnClickListener { setCategory("all") }
        btnCatMicroscopes.setOnClickListener { setCategory("microscopes") }
        btnCatGlassware.setOnClickListener { setCategory("glassware") }
        btnCatElectronics.setOnClickListener { setCategory("electronics") }
        btnCatSafety.setOnClickListener { setCategory("safety equipment") }
        btnCatChemicals.setOnClickListener { setCategory("chemicals") }
    }

    private fun setCategory(category: String) {
        selectedCategory = category
        updateCategoryStyles()
        renderEquipment()
    }

    private fun updateCategoryStyles() {
        val allButtons = listOf(
            btnCatAll to "all",
            btnCatMicroscopes to "microscopes",
            btnCatGlassware to "glassware",
            btnCatElectronics to "electronics",
            btnCatSafety to "safety equipment",
            btnCatChemicals to "chemicals"
        )

        allButtons.forEach { (button, category) ->
            if (category == selectedCategory) {
                button.setBackgroundResource(R.drawable.home_chip_active)
                button.setTextColor(getColor(R.color.ug_white))
            } else {
                button.setBackgroundResource(R.drawable.home_chip_inactive)
                button.setTextColor(getColor(R.color.ug_maroon))
            }
        }
    }

    private fun renderEquipment() {
        val searchText = searchInput.text.toString().trim().lowercase()
        val filtered = equipment.filter { item ->
            val matchesSearch = item.name.lowercase().contains(searchText) ||
                item.category.lowercase().contains(searchText)
            val matchesCategory = selectedCategory == "all" ||
                item.category.lowercase() == selectedCategory
            matchesSearch && matchesCategory
        }

        listContainer.removeAllViews()

        if (filtered.isEmpty()) {
            noResultsText.text = "No equipment found matching your search criteria."
            noResultsText.visibility = TextView.VISIBLE
            return
        }

        noResultsText.visibility = TextView.GONE

        filtered.forEach { item ->
            val card = layoutInflater.inflate(R.layout.item_equipment_card, listContainer, false)
            card.findViewById<TextView>(R.id.tvEquipmentInitial).text = item.name.take(1)
            card.findViewById<TextView>(R.id.tvEquipmentName).text = item.name
            card.findViewById<TextView>(R.id.tvEquipmentCategory).text = item.category
            card.findViewById<TextView>(R.id.tvEquipmentLocation).text = item.location

            val statusText = card.findViewById<TextView>(R.id.tvEquipmentStatus)
            statusText.text = item.status
            if (item.status == "Available") {
                statusText.setBackgroundResource(R.drawable.status_available_bg)
                statusText.setTextColor(getColor(R.color.status_available_text))
            } else {
                statusText.setBackgroundResource(R.drawable.status_inuse_bg)
                statusText.setTextColor(getColor(R.color.status_inuse_text))
            }

            card.setOnClickListener {
                val intent = Intent(this, EquipmentDetailActivity::class.java)
                intent.putExtra("equipment_id", item.id)
                intent.putExtra("equipment_name", item.name)
                intent.putExtra("equipment_category", item.category)
                intent.putExtra("equipment_status", item.status)
                intent.putExtra("equipment_location", item.location)
                intent.putExtra("equipment_description", item.description)
                intent.putExtra("equipment_specs", item.specs)
                startActivity(intent)
            }

            listContainer.addView(card)
        }
    }
}
