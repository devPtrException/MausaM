@file:Suppress("DEPRECATION")
//suppressed deprecation warnings because I'm gonna be using AsyncTasks
//I haven't learnt coroutines yet

package com.rough.mausam

import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.clMain
import kotlinx.android.synthetic.main.activity_main.containerLocation
import kotlinx.android.synthetic.main.activity_main.containerRefresh
import kotlinx.android.synthetic.main.activity_main.tvAddress
import kotlinx.android.synthetic.main.activity_main.tvHumidity
import kotlinx.android.synthetic.main.activity_main.tvLastSync
import kotlinx.android.synthetic.main.activity_main.tvPressure
import kotlinx.android.synthetic.main.activity_main.tvStatus
import kotlinx.android.synthetic.main.activity_main.tvSunrise
import kotlinx.android.synthetic.main.activity_main.tvSunset
import kotlinx.android.synthetic.main.activity_main.tvTempMax
import kotlinx.android.synthetic.main.activity_main.tvTempMin
import kotlinx.android.synthetic.main.activity_main.tvTemperature
import kotlinx.android.synthetic.main.activity_main.tvWind
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity()
{
	
	val API : String = "125e2ff44e4c963cd27e3e1e2a81a27e"
	var ZIP = "110003"
//	this is the pin code of the capital of my country
	
	var doubleBackToExitPressedOnce = false
	
	var location : String = ""
	var lat : String = ""
	var lon : String = ""
	var country : String = ""
	var weather : String = ""
	var date : String = ""
	var sunrise : String = ""
	var pressure : String = ""
	var sunset : String = ""
	var wind : String = ""
	var humidity : String = ""
	var temp : String = ""
	var min : String = ""
	var max : String = ""


//	get lat lon
//	https://api.openweathermap.org/geo/1.0/zip?zip=743165,IN&appid=06c921750b9a82d8f5d1294e1586276
//	get weather
//	https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
	
	
	override public fun onCreate(savedInstanceState : Bundle?)
	{
		
		
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		fetchCoordinates().execute()
		fetchWeather().execute()
		
	
	
	}
	
	
	fun setValues()
	{
		tvAddress.text = location
		tvLastSync.text = "Updated:\n$date"
		tvStatus.text = weather
		tvTemperature.text = "$temp °C"
		tvTempMin.text = "Min:\n$min °C"
		tvTempMax.text = "Max:\n$max °C"
		tvSunrise.text = "Sunrise:\n$sunrise"
		tvPressure.text = "Pressure:\n$pressure mb/hPa"
		tvSunset.text = "Sunset:\n$sunset"
		tvWind.text = "Wind:\n$wind m/sec"
		tvHumidity.text = "Humidity:\n$humidity %"
	}
	
	
	
	
	inner class fetchCoordinates() : AsyncTask<String, Void, String>()
	{
		
		override fun onPreExecute()
		{
			super.onPreExecute()
		}
		
		
		override fun doInBackground(vararg params : String?) : String?
		{
			var response : String?
			
			try
			{
				response = URL("https://api.openweathermap.org/geo/1.0/zip?zip=$ZIP,IN&appid=$API").readText(Charsets.UTF_8)
			}
			catch (e : Exception)
			{
				Log.e("doInBG", e.toString())
				response = null
			}
			
			return response
		}
		
		
		override public fun onPostExecute(result : String?)
		{
			super.onPostExecute(result)
			
			try
			{
				val jsonObj = JSONObject(result)
				lat = jsonObj.getString("lat")
				lon = jsonObj.getString("lon")

				
			}
			catch (e : Exception)
			{
				Log.e("onPE", e.toString())
				clMain.visibility = View.INVISIBLE
			}
		}
	}
	
	
	inner class fetchWeather() : AsyncTask<String, Void, String>()
	{
		
		override fun onPreExecute()
		{
			super.onPreExecute()
		}
		
		
		override fun doInBackground(vararg params : String?) : String?
		{
			var response : String?
			
			try
			{
				response = URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$API").readText(Charsets.UTF_8)
			}
			catch (e : java.lang.Exception)
			{
				Log.e("doInBG", e.toString())
				response = null
			}
			
			return response
		}
		
		
		override fun onPostExecute(result : String?)
		{
			super.onPostExecute(result)
			
			try
			{
				var wind_ : JSONObject
				var main_ : JSONObject
				var sys_ : JSONObject
				var weather_ : JSONArray
				
				
				val jsonObj = JSONObject(result)
				main_ = jsonObj.getJSONObject("main")
				wind_ = jsonObj.getJSONObject("wind")
				sys_ = jsonObj.getJSONObject("sys")
				weather_ = jsonObj.getJSONArray("weather")
				
				location = jsonObj.getString("name")
				country = sys_.getString("country")
				date = SimpleDateFormat("dd/MM/yyyyy   HH:mm", Locale.ENGLISH).format(Date((jsonObj.getLong("dt") * 1000)))
				weather = weather_.getJSONObject(0).getString("main")
				temp = main_.getString("temp")
				min = main_.getString("temp_min")
				max = main_.getString("temp_max")
				sunrise = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date((sys_.getLong("sunrise") * 1000)))
				pressure = main_.getString("pressure")
				sunset = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date((sys_.getLong("sunset") * 1000)))
				wind = wind_.getString("speed")
				humidity = main_.getString("humidity")
				
				
				
				
				Log.v("uData", "$lat $lon $API\n$location $country $weather \n$temp $min $max\n$sunrise $pressure $sunset $wind $humidity")
				
				
				
				setValues()
			}
			catch (e : Exception)
			{
				Log.e("onPE", e.toString())
				
				clMain.visibility = View.INVISIBLE
				
			}
		}
		
	}
	
}
