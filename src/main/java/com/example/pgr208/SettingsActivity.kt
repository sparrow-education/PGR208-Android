package com.example.pgr208

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.pgr208.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner1: Spinner = binding.spinnerDietType
        val spinner2: Spinner = binding.spinnerPriority

        /*
        *   BINDING VIEWS
        * */

        // Binding TextViews
        val tvSettingsHeader = binding.tvSettingsHeader
        val tvDesiredDailyCalorieIntake = binding.tvDesiredDailyCalorieIntake
        val tvMaxSearchHistoryItems = binding.tvMaxSearchHistoryItems
        val tvDesiredDietTypeMaxAmount = binding.tvDesiredDietTypeMaxAmount
        val tvPriority = binding.tvPriority

        // Binding EditTexts
        val etDesiredDailyCalorieIntake = binding.etDesiredDailyCalorieIntake
        val etMaxSearchHistoryItems = binding.etMaxSearchHistoryItems
        val etDesiredDietTypeMaxAmount = binding.etDesiredDietTypeMaxAmount

        // Binding spinners
        val spinnerDietType = binding.spinnerDietType
        val spinnerPriority = binding.spinnerPriority

        // Binding buttons
        val btnReset = binding.btnReset
        val btnSave = binding.btnSave
        val btnReturn = binding.btnBackThirdActivity


        // Creating lists of options for the spinners
        val dietTypes = listOf("Low Carb", "Low  Calorie", "Low Fat")
        val mealCategories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Night Snack")

        // Creating Array adapters
        val spinnerDietTypeAdapter: ArrayAdapter<Any> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dietTypes)
        val spinnerPriorityAdapter: ArrayAdapter<Any> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mealCategories)

        // Specify the layout to use when the list of choices appear
        spinnerDietTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = spinnerDietTypeAdapter
        spinner2.adapter = spinnerPriorityAdapter


        val settings = getSharedPreferences("mySettings", MODE_PRIVATE)
        val settingsEditor = settings.edit()
        val intent = Intent(this, MainActivity::class.java)

        btnSave.setOnClickListener {
            if (etDesiredDailyCalorieIntake.text.isNotEmpty()) {
                val calorieValue = etDesiredDailyCalorieIntake.text.toString().toInt()
                settingsEditor.putInt("setKcal", calorieValue).apply()
                Toast.makeText(this, "$calorieValue saved", Toast.LENGTH_SHORT).show()
            }
            if (etMaxSearchHistoryItems.text.isNotEmpty()) {
                val searchHistoryItemValue = etMaxSearchHistoryItems.text.toString().toInt()
                settingsEditor.putInt("setSearchItem", searchHistoryItemValue).apply()
                Toast.makeText(this, "$searchHistoryItemValue saved", Toast.LENGTH_SHORT).show()
            }
            if (etDesiredDietTypeMaxAmount.text.isNotEmpty()) {
                val nutritionValue = etDesiredDietTypeMaxAmount.text.toString().toInt()
                settingsEditor.putInt("setNutrition", nutritionValue).apply()
                Toast.makeText(this, "$nutritionValue saved", Toast.LENGTH_SHORT).show()
            }
            startActivity( intent )
        }

        btnReset.setOnClickListener {
            settings.edit().clear().apply()
            startActivity( intent )
            Toast.makeText(this, "Cleared settings", Toast.LENGTH_SHORT).show()
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }
}
