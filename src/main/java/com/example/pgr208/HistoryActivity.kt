package com.example.pgr208

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.*
import com.example.pgr208.dao.HistoryData
import com.example.pgr208.databinding.ActivityHistoryBinding
import com.example.pgr208.view.ItemAdapterHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbInstance = Room.databaseBuilder( this, AppDatabase::class.java, "historyData").build()
        val adapter = ItemAdapterHistory(result = ArrayList(), this)
        val recyclerView = binding.recyclerViewHistory
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        GlobalScope.launch( Dispatchers.IO ) {
            val result = dbInstance.historyDAO().getAll()
            recyclerView.adapter = ItemAdapterHistory(result as ArrayList<HistoryData>, this@HistoryActivity)
        }

        val btnReturn = binding.btnBackSecondActivity
        btnReturn.setOnClickListener {
            finish()
        }
    }
}