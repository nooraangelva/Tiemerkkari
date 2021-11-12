package com.example.android.navigation

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import java.util.*
import android.R.attr.data
import android.content.Context
import android.content.ContextWrapper
import java.io.File
import java.io.IOException

import java.io.FileOutputStream

import java.io.OutputStream

import java.io.InputStream



class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController

    val REQUEST_CODE_PERMISSION = 100
    val REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    private lateinit var imageUri : Uri
    private lateinit var bitmap : Bitmap
    private lateinit var imageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "OnCreate")

        super.onCreate(savedInstanceState)
        sharedPreferences = getPreferences(MODE_PRIVATE)

        changeLanguage()
        Log.i("MainActivity", "Langugae pref: " + sharedPreferences.getString("language", "en")!!)
        changeTheme()
        Log.i("MainActivity", "Langugae pref: " + sharedPreferences.getBoolean("SELECTED_THEME", false))

        setContentView(R.layout.activity_main)
/*
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController)

        appBarConfiguration = AppBarConfiguration(navController.graph)


        Log.i("MainActivity", "nav graph" + navController.graph.toString())*/

        if(allPermissionsGranted()){
            //permission ok
            Log.v("CameraApp","Permission ok")
        }
        else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
            Log.v("CameraApp","Ask permissions")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    fun changeLanguage()
    {
        val currentLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", "fi")

        Log.i("MainActivity", "setApplocale()" + sharedPreferences.getString("SELECTED_LANGUAGE", "fi"))

        val locale = Locale(currentLanguage!!)

        Locale.setDefault(locale)

        val config = Configuration()



        config.setLocale(locale)

        try {
            config.locale = locale
        } catch (e: Exception) {
        }



        val currentLocale = locale

        val resources = resources

        resources.updateConfiguration(config, resources.displayMetrics)


    }

    fun allPermissionsGranted() : Boolean{
        //Tsekkaa onko kaikki permissionit annettu
        for (permission in REQUIRED_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    fun openGalleryForImage(name: String) {
        imageName = name
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            var imageUriString : String = data?.data?.path!!
            imageUri = Uri.parse(imageUriString)
            bitmap =   MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            val storageDir = filesDir


            //TODO save image and name dynamically file with data from import

            // Get the context wrapper instance
            val wrapper = ContextWrapper(applicationContext)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("sign_images", Context.MODE_PRIVATE)


            // Create a file to save the image
            file = File(file, "${imageName}.jpg")

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
            } catch (e: IOException){ // Catch the exception
                e.printStackTrace()
            }

            Log.v("Import", "saved images path ${Uri.parse(file.absolutePath)}")

            // Return the saved image uri
            //return Uri.parse(file.absolutePath)


        }
    }



    private fun changeTheme() {

        Log.i("MainActivity", "checkTheme()" + sharedPreferences.getBoolean("SELECTED_THEME", false))

        val darkMode = sharedPreferences.getBoolean("SELECTED_THEME", false)
        if(!darkMode)
        {
            setTheme(R.style.DarkTheme)
        }
        else
        {
            setTheme(R.style.LightTheme)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {


        R.id.languageOptionMenu -> {


            when (sharedPreferences.getString("SELECTED_LANGUAGE", "en")) {
                "fi" -> {

                    Log.i("MainActivity", "languageOptionMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }
                    Log.i("MainActivity", "setApplocale() now" + sharedPreferences.getString("SELECTED_LANGUAGE", "fi"))
                    this.recreate()

                }
                "en" -> {

                    Log.i("MainActivity", "languageOptionMenu pressed to fi")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "fi")
                        apply()
                    }
                    Log.i("MainActivity", "setApplocale() now" + sharedPreferences.getString("SELECTED_LANGUAGE", "fi"))
                    this.recreate()

                }
                else -> {

                    Log.i("MainActivity", "languageOptionMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }
                    Log.i("MainActivity", "setApplocale() now" + sharedPreferences.getString("SELECTED_LANGUAGE", "fi"))
                    this.recreate()
                }
            }

            true
        }

        R.id.dayNightOptionMenu -> {

            when (sharedPreferences.getBoolean("SELECTED_THEME", false)) {
                true -> {

                    Log.i("MainActivity", "dayNightOptionMenu pressed to day")
                    Log.i("MainActivity", "checkTheme() now " + sharedPreferences.getBoolean("SELECTED_THEME", false))

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", false)
                        apply()
                    }
                    Log.i("MainActivity", "checkTheme() now " + sharedPreferences.getBoolean("SELECTED_THEME", false))
                    this.recreate()

                }
                false -> {

                    Log.i("MainActivity", "dayNightOptionMenu pressed to night")
                    Log.i("MainActivity", "checkTheme() now " + sharedPreferences.getBoolean("SELECTED_THEME", false))

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", true)
                        apply()
                    }
                    Log.i("MainActivity", "checkTheme() now" + sharedPreferences.getBoolean("SELECTED_THEME", false))
                    this.recreate()

                }
            }
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}


