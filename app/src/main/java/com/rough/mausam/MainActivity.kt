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
		
		
	}
	
}
