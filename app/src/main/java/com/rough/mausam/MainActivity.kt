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
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity()
{
	
	val API : String = "125e2ff44e4c963cd27e3e1e2a81a27e"
	var ZIP = "743165"
	
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
