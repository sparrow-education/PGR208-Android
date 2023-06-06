package com.example.pgr208.models

import android.graphics.Bitmap

// AssetData is a reusable DTO mapping response data from a REST API
class AssetData: Asset() {
    var calories: Int? = null
    var image: Bitmap? = null
    var url: String? = null
    var mealType: String? = null
    var dietLabels: String? = null
    var yield: Int? = null
}