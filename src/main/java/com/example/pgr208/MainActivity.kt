package com.example.pgr208

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.*
import androidx.room.Database
import com.example.pgr208.dao.HistoryData
import com.example.pgr208.databinding.ActivityMainBinding
import com.example.pgr208.db.DatabaseHelper
import com.example.pgr208.models.AssetData
import com.example.pgr208.view.ItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

@Dao
interface HistoryDAO {
    @Insert fun addSearch(historyData: HistoryData)
    @Query("SELECT * FROM HistoryData") fun getAll(): List<HistoryData>
    @Query("UPDATE `sqlite_sequence` SET `seq` = 0 WHERE `name` = 'historyData'") fun resetAutoInc()
}

@Database(entities = [HistoryData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDAO(): HistoryDAO
}

class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Binding
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Adapter for recycler view with ItemAdapter
            val adapter = ItemAdapter(allData = ArrayList(), this)

            // RecyclerView
            val recyclerView = binding.rvMainActivity
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            // Launching coroutine on main thread or running in background(IO)
            // Since downloadAssetList is async, it is running in the BG, we don't need it on the IO thread
            GlobalScope.launch( Dispatchers.Main ) {
                val allData = downloadAssetList("recipes")
                // A Recyclerview needs an adapter to understand whats inside each row
                recyclerView.adapter = ItemAdapter( allData, this@MainActivity )
            }

            // Room database connection
            val dbInstance = Room.databaseBuilder( this, AppDatabase::class.java, "historyData").build()

            // SQLite database connection
            val sqliteInstance = DatabaseHelper(this,null).writableDatabase
            val query = "SELECT * FROM recipes WHERE label = ?"
            val cursor = sqliteInstance.rawQuery(query, arrayOf("Tester from SQLite"))
            if (cursor.moveToFirst()) {
                val label = cursor.getString(1)
                println(" ----> $label")
            }
            cursor.close()

            // Getting the components with binding
            val btnSettings = binding.btnSettings
            val btnSearchHistory = binding.btnSearchHistory
            val btnSearch = binding.btnSearch
            val etSearchField = binding.etSearchField


            // Search button to dispatch function downloadAssetList to send in user input from an editText
            btnSearch.setOnClickListener {
                if (etSearchField.text.isEmpty()) {
                    Toast.makeText(this, "Search Field Is Empty", Toast.LENGTH_LONG).show()
                } else {
                    GlobalScope.launch ( Dispatchers.Main ){
                        val allData = downloadAssetList(etSearchField.text.toString())
                        recyclerView.adapter = ItemAdapter( allData, this@MainActivity )
                        val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                        intent.putExtra("searchFieldData", "test string value")
                    }
                    GlobalScope.launch( Dispatchers.IO ) {
                        val searchFieldData = HistoryData()

                        // Room insert
                        searchFieldData.label = etSearchField.text.toString()
                        dbInstance.historyDAO().addSearch( searchFieldData )

                        // SQLite insert
                        // ContentValues is a class to store the prepared values into the table
                        // The put method is used to specify the column name and values for the new row
                        val values = ContentValues()
                        values.put("label", etSearchField.text.toString())
                        sqliteInstance.insert("recipes", null, values)
                    }
                }
            }

            // Search History button starting HistoryActivity with the help of intent
            btnSearchHistory.setOnClickListener {
                Intent( this, HistoryActivity::class.java ).also {
                    startActivity( it )
                }
            }

            btnSettings.setOnClickListener {
                Intent( this, SettingsActivity::class.java ).also {
                    startActivity( it )
                }
            }
        }

    // Suspend coroutine function
    private suspend fun downloadAssetList(userInput: String?): ArrayList<AssetData>  {
        val allData = ArrayList<AssetData>()
        GlobalScope.async {
            val assetData = URL("https://api.edamam.com/api/recipes/v2?type=public&q=${ userInput }&app_id=f278e16b&app_key=0d47f01d3b60fd574eaa7bc10bedfb43").readText().toString()
            Log.i("Testing REST API", assetData)

            // We extract the Array "hits" from response body from our response body
            // Since assetData is a big chunk of Object we cast is as a JSONObject and then extract "hits" from the sub-layer
            // then cast hits as an Array
            val assetDataArray = (JSONObject( assetData ).get("hits") as JSONArray)
            // For each item inside "hits" we get the position of that object we pass it by reference as "assetItem"
            (0 until assetDataArray.length()).forEach{ itemNr ->
                // AssetData is a DTO class which is helpful to map the response data
                val dataItem = AssetData()
                val assetItem = assetDataArray.get( itemNr )

                // Then finally we cast assetItem as JSONObject and extract recipe
                val recipe = ( assetItem as JSONObject ).getJSONObject("recipe")
                // We can also use our custom made DTO for referencing. Since "recipe" is an object, we can access its fields like label
                dataItem.label = recipe.getString("label")
                //println(dataItem.label)

                // Grabbing the image object from API
                val image = ( recipe as JSONObject ).getJSONObject("images")
                val small = ( image as JSONObject ).getJSONObject("SMALL")
                // Opens a connection to this URL and returns an InputStream for reading from that connection.
                val url = URL( (small as JSONObject).getString("url").toString() ).openStream()
                // Decode an input stream into a bitmap. If the input stream is null, or cannot be used to decode a bitmap, the function returns null.
                dataItem.image = BitmapFactory.decodeStream(url)

                dataItem.calories = recipe.getInt("calories")
                //println(dataItem.calories)

                dataItem.url = recipe.getString("url")
                //println(dataItem.url)

                dataItem.mealType = ( recipe ).getJSONArray("mealType")[0].toString()
                //println(dataItem.mealType)

                dataItem.yield = ( recipe ).getInt("yield")

                // Fixing for single serving only
                /*val totalKcal = dataItem.calories
                val totalServing = dataItem.yield
                val singleServing = (totalKcal as Int).div(totalServing as Int)
                dataItem.calories = singleServing*/

                dataItem.dietLabels = ( recipe ).getJSONArray("dietLabels").toString()
                //println(dataItem.dietLabels)
                arrayOf(dataItem.dietLabels).forEach { dl ->
                    val diet = mutableListOf<String>()
                    diet.add(dl.toString())
                    for(d in diet){
                        dataItem.dietLabels = d.replace(",",", ").replace("["," ").replace("\"", " ").replace("]", " ")
                    }
                }

                allData.add( dataItem )
            }
        }.await()
        return allData

    }
}
