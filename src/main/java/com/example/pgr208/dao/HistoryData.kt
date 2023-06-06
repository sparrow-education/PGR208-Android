package com.example.pgr208.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pgr208.models.Asset

// Extending from Asset that contains all the information from AssetData
@Entity(tableName = "HistoryData")
class HistoryData: Asset() {
    @PrimaryKey(autoGenerate = true) var id = 0
}