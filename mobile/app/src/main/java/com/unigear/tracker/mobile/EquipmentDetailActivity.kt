package com.unigear.tracker.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EquipmentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_detail)

        val name = intent.getStringExtra("equipment_name") ?: "Equipment"
        val category = intent.getStringExtra("equipment_category") ?: "N/A"
        val status = intent.getStringExtra("equipment_status") ?: "Unknown"
        val location = intent.getStringExtra("equipment_location") ?: "N/A"
        val description = intent.getStringExtra("equipment_description") ?: "No description available."
        val specs = intent.getStringExtra("equipment_specs") ?: "N/A"

        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvDetailCategory).text = category
        findViewById<TextView>(R.id.tvDetailLocation).text = location
        findViewById<TextView>(R.id.tvDetailDescription).text = description
        findViewById<TextView>(R.id.tvDetailSpecs).text = specs

        val statusView = findViewById<TextView>(R.id.tvDetailStatus)
        statusView.text = status
        if (status.equals("Available", ignoreCase = true)) {
            statusView.setBackgroundResource(R.drawable.status_available_bg)
            statusView.setTextColor(getColor(R.color.status_available_text))
        } else {
            statusView.setBackgroundResource(R.drawable.status_inuse_bg)
            statusView.setTextColor(getColor(R.color.status_inuse_text))
        }

        findViewById<View>(R.id.btnRequestNow).setOnClickListener {
            val requestIntent = Intent(this, MyRequestsActivity::class.java)
            requestIntent.putExtra("prefill_equipment_name", name)
            requestIntent.putExtra("prefill_category", category)
            requestIntent.putExtra("open_form", true)
            startActivity(requestIntent)
        }

        findViewById<View>(R.id.btnBackToCatalog).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnNavCatalog).setOnClickListener {
            val homeIntent = Intent(this, HomeActivity::class.java)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }

        findViewById<View>(R.id.btnNavRequests).setOnClickListener {
            startActivity(Intent(this, MyRequestsActivity::class.java))
        }

        findViewById<View>(R.id.btnNavProfile).setOnClickListener {
            Toast.makeText(this, "Profile screen is coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}
