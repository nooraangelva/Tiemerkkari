package com.example.android.navigation

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.*
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.android.navigation.databinding.ActivityMainBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import org.json.JSONArray
import timber.log.Timber
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val CONNECT = 1
const val SEND = 2
const val RECEIVE = 3

var SERVICE_UUID_SEND = UUID.fromString("4fa4c201-1fb5-459e-8fcc-c5c9c331914b")
var CHARACTERISTIC_UUID_SEND = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")

var SERVICE_UUID_RECEIVE = UUID.fromString("4fa4c201-1fb5-459e-8fcc-c5c9c331914b")
var CHARACTERISTIC_UUID_RECEIVE = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController
    var mainThreadHandler : Handler? = null



    val REQUEST_CODE_PERMISSION = 100
    val RESULT_LOAD_IMAGE = 200
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.BLUETOOTH","android.permission.BLUETOOTH_SCAN","android.permission.BLUETOOTH_ADMIN",
        "android.permission.BLUETOOTH_CONNECT","android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION")

    lateinit var bitmap : Bitmap
    private lateinit var imageName: String
    lateinit var  pathInString : String


    lateinit var receivedMessage : Receive
    lateinit var sendMessage : Send

    private lateinit var binding : ActivityMainBinding

    val bluetoothAdapter : BluetoothAdapter by lazy{
        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    val btRequestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Bluetooth is now enabled.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("OnCreate")

        super.onCreate(savedInstanceState)
        sharedPreferences = getPreferences(MODE_PRIVATE)

        changeLanguage()
        Timber.i("Langugae pref: " + sharedPreferences.getString("language", "en")!!)
        changeTheme()
        Timber.i("Langugae pref: " + sharedPreferences.getBoolean("SELECTED_THEME", false))

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(this, navController)


        //appBarConfiguration = AppBarConfiguration(navController.graph)


        Timber.i("nav graph" + navController.graph.toString())

        if (allPermissionsGranted()) {
            //permission ok
            Timber.tag("CameraApp").v("Permission ok")
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
            Timber.tag("CameraApp").v("Ask permissions")
        }

        if(!bluetoothAdapter.isEnabled){
            openBtActivity()
        }

        mainThreadHandler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message){
                if(SEND == msg.what) {
                    //super.handleMessage(msg)
                    Timber.v(""+msg.obj)
                    //binding.helloText.setText(msg.obj as String)
                }
                else if(RECEIVE == msg.what) {
                    Timber.v(""+msg.obj)
                    //binding.helloText.setText(msg.obj as String)
                }
            }
        }
        var runnable = ThreadHandler(mainThreadHandler)
        var thread = Thread(runnable)
        thread.start()
        runnable.setBluetoothAdapter(bluetoothAdapter)


        //runnable.workerThreadHandler!!.startBleScan(bluetoothAdapter)
        var msg = Message()
        msg.what = CONNECT
        runnable.workerThreadHandler!!.sendMessage(msg)

        pathInString = ""


    }


    fun openBtActivity(){

        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        btRequestActivity.launch(intent)

    }


    fun write(data : JsonArray){

        var runnable = ThreadHandler(mainThreadHandler)
        var msg = Message()
        msg.what = SEND
        msg.obj = data
        runnable.workerThreadHandler!!.sendMessage(msg)

    }
    fun read(){

    }










    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    fun changeLanguage() {
        val currentLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", "fi")

        Timber.i("setApplocale()" + sharedPreferences.getString("SELECTED_LANGUAGE", "fi"))

        val locale = Locale(currentLanguage!!)

        Locale.setDefault(locale)

        val config = Configuration()

        config.setLocale(locale)

        try {
            config.locale = locale
        } catch (e: Exception) {
        }

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

    fun openGalleryForImage() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")
        imageName = current.format(formatter)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)

    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {

        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val image_uri = data.data
            //frame!!.setImageURI(image_uri)
            bitmap = uriToBitmap(image_uri!!)!!
            Timber.i("Langugae pref:"+image_uri)
        }
        if(data != null){

            // Get the context wrapper instance
            val wrapper = ContextWrapper(applicationContext)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("sign_images", MODE_PRIVATE)

            Timber.i("Langugae pref:"+file)

            try {

                val mypath = File(file, "${imageName}.jpg")
                pathInString = mypath.path
                val fos = FileOutputStream(mypath);
                //val fos = openFileOutput(mypath, MODE_PRIVATE)
                Timber.i("Langugae pref:"+fos)

                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.close()


            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun changeTheme() {

        Timber.i("checkTheme()" + sharedPreferences.getBoolean("SELECTED_THEME", false))

        val darkMode = sharedPreferences.getBoolean("SELECTED_THEME", false)
        if (!darkMode) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {


        R.id.languageOptionMenu -> {


            when (sharedPreferences.getString("SELECTED_LANGUAGE", "en")) {
                "fi" -> {

                    Timber.i("languageOptionMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }
                    Timber.i(
                        "setApplocale() now" + sharedPreferences.getString(
                            "SELECTED_LANGUAGE",
                            "fi"
                        )
                    )
                    this.recreate()

                }
                "en" -> {

                    Timber.i("languageOptionMenu pressed to fi")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "fi")
                        apply()
                    }
                    Timber.i(
                        "setApplocale() now" + sharedPreferences.getString(
                            "SELECTED_LANGUAGE",
                            "fi"
                        )
                    )
                    this.recreate()

                }
                else -> {

                    Timber.i("languageOptionMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }
                    Timber.i(
                        "setApplocale() now" + sharedPreferences.getString(
                            "SELECTED_LANGUAGE",
                            "fi"
                        )
                    )
                    this.recreate()
                }
            }

            true
        }

        R.id.dayNightOptionMenu -> {

            when (sharedPreferences.getBoolean("SELECTED_THEME", false)) {
                true -> {

                    Timber.i("dayNightOptionMenu pressed to day")
                    Timber.i(
                        "checkTheme() now " + sharedPreferences.getBoolean(
                            "SELECTED_THEME",
                            false
                        )
                    )

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", false)
                        apply()
                    }
                    Timber.i(
                        "checkTheme() now " + sharedPreferences.getBoolean(
                            "SELECTED_THEME",
                            false
                        )
                    )
                    this.recreate()

                }
                false -> {

                    Timber.i("dayNightOptionMenu pressed to night")
                    Timber.i(
                        "checkTheme() now " + sharedPreferences.getBoolean(
                            "SELECTED_THEME",
                            false
                        )
                    )

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", true)
                        apply()
                    }
                    Timber.i(
                        "checkTheme() now" + sharedPreferences.getBoolean(
                            "SELECTED_THEME",
                            false
                        )
                    )
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


