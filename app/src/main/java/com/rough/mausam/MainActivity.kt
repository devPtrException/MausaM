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
import kotlinx.android.synthetic.main.activity_main.containerZipUpdate
import kotlinx.android.synthetic.main.activity_main.etZip
import kotlinx.android.synthetic.main.activity_main.ivInfo
import kotlinx.android.synthetic.main.activity_main.ivReload
import kotlinx.android.synthetic.main.activity_main.pbLoading
import kotlinx.android.synthetic.main.activity_main.tvAddress
import kotlinx.android.synthetic.main.activity_main.tvErrorText
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
		
		
		containerRefresh.setOnClickListener {
			
			fetchCoordinates().execute()
			fetchWeather().execute()
			
			Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show()
		}
		
		
		
		containerLocation.setOnClickListener {
			
			clMain.visibility = View.INVISIBLE
			containerZipUpdate.visibility = View.VISIBLE
			
			
			
			etZip.requestFocus()
			val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
			imm.showSoftInput(etZip, InputMethodManager.SHOW_IMPLICIT)
			
			
			ivReload.setOnClickListener {
				
				if (zipValidator(etZip.text.toString()))
				{
					Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show()
					ZIP = etZip.text.toString()
					etZip.text.clear()
					
					fetchCoordinates().execute()
					fetchWeather().execute()
					
					clMain.visibility = View.VISIBLE
					containerZipUpdate.visibility = View.INVISIBLE
				}
				else
				{
					Toast.makeText(this, "ZIP code invalid.\nPlease enter a valid zip code.", Toast.LENGTH_SHORT).show()
				}
			}
		}
		
		
		
		ivInfo.setOnClickListener {
			Toast.makeText(this, "Long press INFO for more details.\nClick on LOCATION to update zip code.\n", Toast.LENGTH_SHORT).show()
		}
		
		ivInfo.setOnLongClickListener {
			
			val infoDialogue = AlertDialog.Builder(this@MainActivity)
					.setTitle("About")
					.setMessage("\nA simple and lightweight weather app.\nMore features to be added.\n\n\n\n- devRaj\n- Techno India")
					.setPositiveButton("Ok", DialogInterface.OnClickListener
					{ dialogInterface, i ->
						Toast.makeText(this, "Thank You for trying out my app.", Toast.LENGTH_SHORT).show()
					})
					.setCancelable(true)
					.setOnCancelListener {
						Toast.makeText(this, "Thank You for trying out my app.", Toast.LENGTH_SHORT).show()
					}
					.show()
			
			true
		}
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
	
	
	fun zipValidator(zip : String) : Boolean
	{
//		val pattern = Regex("^[1-9]{1}[0-9]{2}\\\\s{0,1}[0-9]{3}\$")
		val pattern = Regex("^[1-9][0-9]{5}\$")
		
		return pattern.matches(zip)
	}
	
	
	inner class fetchCoordinates() : AsyncTask<String, Void, String>()
	{
		
		override fun onPreExecute()
		{
			super.onPreExecute()
			pbLoading.visibility = View.VISIBLE
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

//				Log.v("dx2", "$zip $location $lat $lon $country")
				
				pbLoading.visibility = View.GONE
			}
			catch (e : Exception)
			{
				Log.e("onPE", e.toString())
				pbLoading.visibility = View.INVISIBLE
				clMain.visibility = View.INVISIBLE
				tvErrorText.visibility = View.VISIBLE
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
				pbLoading.visibility = View.GONE
			}
			catch (e : Exception)
			{
				Log.e("onPE", e.toString())
				
				pbLoading.visibility = View.INVISIBLE
				clMain.visibility = View.INVISIBLE
				tvErrorText.visibility = View.VISIBLE
				tvErrorText.visibility = View.VISIBLE
				
			}
		}
		
	}
	
	
	override fun onBackPressed()
	{
		if (doubleBackToExitPressedOnce)
		{
			super.onBackPressed()
			return
		}
		
		this.doubleBackToExitPressedOnce = true
		tvErrorText.visibility = View.GONE
		
		Toast.makeText(this, "Press BACK again to exit.", Toast.LENGTH_SHORT).show()
		clMain.visibility = View.VISIBLE
		
		Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
	}
	
}
