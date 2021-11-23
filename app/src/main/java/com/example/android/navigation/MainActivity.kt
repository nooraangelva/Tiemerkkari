package com.example.android.navigation

import android.Manifest
import android.R.attr
import android.app.Activity
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
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.*
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.android.navigation.databinding.ActivityMainBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import timber.log.Timber
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController

    val REQUEST_CODE_PERMISSION = 100
    val RESULT_LOAD_IMAGE = 200
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.BLUETOOTH","android.permission.BLUETOOTH_SCAN","android.permission.BLUETOOTH_ADMIN",
        "android.permission.BLUETOOTH_CONNECT","android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.ACCESS_FINE_LOCATION")

    lateinit var bitmap : Bitmap
    private lateinit var imageName: String
    lateinit var  pathInString : String

    var SERVICE_UUID = UUID.fromString("4fa4c201-1fb5-459e-8fcc-c5c9c331914b")
    var CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")

    lateinit var receivedMessage : Receive
    lateinit var sendMessage : Send

    private lateinit var binding : ActivityMainBinding
    private val bluetoothAdapter : BluetoothAdapter by lazy{
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
        startBleScan()
        pathInString = ""


    }


    fun openBtActivity(){

        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        btRequestActivity.launch(intent)

    }

    fun startBleScan(){

        var filter = ScanFilter.Builder().setDeviceName("Tiemerkkari").build()
        var filters : MutableList<ScanFilter> = mutableListOf(filter)

        var setting = ScanSettings.Builder().build()
        bluetoothAdapter.bluetoothLeScanner.startScan(filters,setting,bleScanCallback)

    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice){

        bluetoothDevice.connectGatt(this,false,bleGattCallback)

    }

    private val bleScanCallback : ScanCallback by lazy{
        object : ScanCallback(){

            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                val bluetoothDevice = result.device
                connectToDevice(bluetoothDevice)

                Timber.tag("Buttons").v("laite: %s", bluetoothDevice.name)

                if (bluetoothDevice != null) {
                    connectToDevice(bluetoothDevice)
                }
            }
        }
    }
    private val bleGattCallback : BluetoothGattCallback by lazy {
        object : BluetoothGattCallback(){
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)

                if(newState == BluetoothProfile.STATE_CONNECTED){
                    gatt?.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)


                val service = gatt?.getService(SERVICE_UUID)
                val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)

                gatt?.setCharacteristicNotification(characteristic,true)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                super.onCharacteristicChanged(gatt, characteristic)
                Timber.v("laite: " + characteristic?.getStringValue(0))
                if (characteristic != null) {
                    receivedMessage = Json.decodeFromString<Receive>(characteristic.getStringValue(0))
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {                                 //See if the write was successful
                    Timber.e("**ACTION_DATA_WRITTEN**" + characteristic)

                }
            }
        }
    }

    fun write(gatt: BluetoothGatt){

        val service = gatt?.getService(SERVICE_UUID)
        gatt.writeCharacteristic(service?.getCharacteristic(CHARACTERISTIC_UUID))
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


