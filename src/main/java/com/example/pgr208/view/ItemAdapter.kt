package com.example.pgr208.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208.R
import com.example.pgr208.models.AssetData
import com.example.pgr208.models.SettingDTO

class ItemAdapter(private val allData: ArrayList<AssetData>, private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var cover: ImageView? = itemView.findViewById(R.id.cover)
        var title: TextView? = itemView.findViewById(R.id.title)
        var calories: TextView? = itemView.findViewById(R.id.calories)
        var recipeBtn: Button? = itemView.findViewById(R.id.recipeBtn)
        var mealType: TextView? = itemView.findViewById(R.id.mealType)
        var dietLabels: TextView? = itemView.findViewById(R.id.dietLabels)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cell, parent, false)
        return ViewHolder( view )
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item = allData[position]
        val recipeBtn = holder.recipeBtn
        val coverHolder = holder.cover
        val labelHolder = holder.title
        val caloriesHolder = holder.calories
        val mealTypeHolder = holder.mealType
        val dietHolder = holder.dietLabels

        val settingsKcalValue = SettingDTO(context).myPreference.getInt("setKcal", 0).toString()

        coverHolder?.setImageBitmap(item.image)
        labelHolder?.text = item.label
        caloriesHolder?.text = item.calories.toString().plus(" kcal per serving")
        mealTypeHolder?.text = item.mealType.toString()
        dietHolder?.text = item.dietLabels.toString()

        // Button for showing recipe to external site
        recipeBtn?.setOnClickListener {
            val recipeURL = item.url
            val intent = Intent( Intent.ACTION_VIEW, Uri.parse(recipeURL) )
            holder.itemView.context.startActivity(intent)
        }

        if ( settingsKcalValue.toInt() == 0 ) {
            mealTypeHolder?.setBackgroundColor(Color.alpha(5))
        }
        println(item.calories!!.toInt())
        if ( settingsKcalValue.toInt().toString() <= item.calories.toString() ) {
            mealTypeHolder?.setBackgroundColor(Color.parseColor("red"))
        }
        if ( settingsKcalValue.toInt().toString() >= item.calories.toString() ) {
            mealTypeHolder?.setBackgroundColor(Color.parseColor("lime"))
        }
    }

    override fun getItemCount(): Int {
        return allData.size
    }

}
