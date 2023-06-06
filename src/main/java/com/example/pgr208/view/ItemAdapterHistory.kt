package com.example.pgr208.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208.R
import com.example.pgr208.dao.HistoryData

class ItemAdapterHistory(private val result: ArrayList<HistoryData>, private val context: Context)
    : RecyclerView.Adapter<ItemAdapterHistory.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var historyLabel: TextView? = itemView.findViewById(R.id.tvHistoryLabel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterHistory.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_history, parent, false)
        return ViewHolder( view )
    }

    override fun onBindViewHolder(holder: ItemAdapterHistory.ViewHolder, position: Int) {
        val item = result[position]
        val labelHolder = holder.historyLabel
        labelHolder?.text = item.label.toString()
    }

    override fun getItemCount(): Int {
        return result.size
    }
}
