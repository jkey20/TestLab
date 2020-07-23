package org.first.mnkotlin

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import org.first.mnkotlin.databinding.ActivityRestaurantListBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class RestaurantListActivity : AppCompatActivity() {
    private lateinit var finalAddress : String
    private lateinit var mlResult : String
    private lateinit var br : BufferedReader
    private lateinit var inputLine : String
    private var address = ArrayList<String>()
    private var title = ArrayList<String>()
    private var telephone = ArrayList<String>()
    private lateinit var binding : ActivityRestaurantListBinding
    private lateinit var viewmodel : RestaurantListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_list)
        viewmodel = ViewModelProviders.of(this).get(RestaurantListViewModel::class.java)

        finalAddress = "금천구"
        mlResult = "치킨"

        var customAsyncTask = CustomAsyncTask()
        customAsyncTask.execute()

    }





}
