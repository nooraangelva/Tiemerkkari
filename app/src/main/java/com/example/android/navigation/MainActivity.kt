package com.example.android.navigation

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import java.util.*
import android.bluetooth.*
import android.content.*
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.BitmapFactory
import android.os.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import timber.log.Timber
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.util.Log

// Thread constants to differentiate messages from each other
const val CONNECT = 1
const val SEND = 2
const val RECEIVE = 3
const val QUIT_MSG = 4
const val DISCONNECT = 5

// UUID's for BLE Gatt connection
val SERVICE_UUID = UUID.fromString("4fa4c201-1fb5-459e-8fcc-c5c9c331914b")!!
val CHARACTERISTIC_UUID_SEND = UUID.fromString("4ec4893c-6c9e-478e-9ad2-7a964946bc86")!!
val CHARACTERISTIC_UUID_RECEIVE = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")!!

// Permission request values for the app to work
const val REQUEST_CODE_PERMISSION = 100
const val RESULT_LOAD_IMAGE = 200
val REQUIRED_PERMISSIONS = arrayOf("android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.BLUETOOTH","android.permission.BLUETOOTH_SCAN","android.permission.BLUETOOTH_ADMIN",
    "android.permission.BLUETOOTH_CONNECT","android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.ACCESS_FINE_LOCATION")

class MainActivity : AppCompatActivity() {

    // To access user preferences
    lateinit var sharedPreferences: SharedPreferences

    // To create menu and access navigation between fragments
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var menu: Menu

    // To save and retrieve an image for creating a sign in import fragment
    lateinit var bitmap : Bitmap
    private lateinit var imageName: String
    lateinit var  pathInString : String

    // For using the devices bluetooth
    val bluetoothAdapter : BluetoothAdapter by lazy{

        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

    }

    // For activating user bluetooth if it's not activated
    private val btRequestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->

        if (result.resultCode == Activity.RESULT_OK){

            Toast.makeText(this, "Bluetooth is now enabled.", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if(BuildConfig.DEBUG){

            Timber.plant(Timber.DebugTree())

        }

        // Sets user preferences on theme and language
        sharedPreferences = getPreferences(MODE_PRIVATE)
        changeLanguage()
        Timber.tag("MainActivity").v("Language pref: %s", sharedPreferences.getString("language", "en")!!)
        setTheme()
        Timber.tag("MainActivity").v("Language pref: %s", sharedPreferences.getBoolean("SELECTED_THEME", false))

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Sets up navigation, for fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(this, navController)

        Timber.tag("MainActivity").v("nav graph%s", navController.graph.toString())

        // checks if all permissions required have been granted
        if (allPermissionsGranted()) {

            //permission ok
            Timber.tag("MainActivity").v("Permissions ok")

        } else {

            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
            Timber.tag("MainActivity").v("Ask permissions")

        }

        // Enables bluetooth if it is not On
        if(!bluetoothAdapter.isEnabled){

            openBtActivity()

        }

        // Initializes images path variable
        pathInString = ""

    }

    // Opens bluetooth if no active
    private fun openBtActivity(){

        Timber.tag("MainActivity").v("openBtActivity()")
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        btRequestActivity.launch(intent)

    }

    // Changes language through locale when called
    private fun changeLanguage() {

        // Gets selected user preference and the current language
        val newLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().language)
        val currentLanguage = Locale.getDefault().language

        Timber.tag("MainActivity").v("changeLanguage() - language new %s", newLanguage)
        Timber.tag("MainActivity").v("changeLanguage() - language current %s", currentLanguage)

        // If new language is different than the current, language will be changed
        if (currentLanguage !== newLanguage) {

            val locale = Locale(newLanguage!!)
            Locale.setDefault(locale)
            val config = Configuration()

            config.setLocale(locale)

            try {
                config.locale = locale
            }
            catch (e: Exception) {
            }

            val resources = resources

            resources.updateConfiguration(config, resources.displayMetrics)

        }

    }

    // Sets theme (dark or light) from user preference, when called
    private fun setTheme() {

        val darkMode = sharedPreferences.getBoolean("SELECTED_THEME", (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES))

        Timber.tag("MainActivity").v("setTheme() - darMode: $darkMode")

        if (darkMode) {

            setTheme(R.style.DarkTheme)

        }
        else {

            setTheme(R.style.LightTheme)

        }

    }

    // Checks if all the permission are granted, when called
    private fun allPermissionsGranted() : Boolean{

        Timber.tag("MainActivity").v("allPermissionsGranted()")
        for (permission in REQUIRED_PERMISSIONS){

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }

        }

        return true
    }

    // IMAGE RETRIEVE FUNCTIONS --------------------------------------------------------------------

    // Opens gallery to retrieve an image and names it
    fun openGalleryForImage() {

        Timber.tag("MainActivity").v("openGalleryForImage()")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")
        imageName = current.format(formatter)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)

    }

    // Gets the image that was retrieved, saves it as a file to a folder
    // and gives the path to Import fragment
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        Timber.tag("MainActivity").v("onActivityResult()")

        // Checks if the retrieval was successful and creates a bitmap
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            val image_uri = data.data
            bitmap = uriToBitmap(image_uri!!)!!

        }

        // Checks that data is not null
        if(data != null){

            // Gets the context wrapper instance
            val wrapper = ContextWrapper(applicationContext)

            // Initializes a new file
            // The bellow line returns a directory in internal storage
            val file = wrapper.getDir("sign_images", MODE_PRIVATE)

            // puts the bitmap in to a file in the designated path
            try {

                val path = File(file, "${imageName}.jpg")
                pathInString = path.path

                Timber.tag("MainActivity").v("Image path: $pathInString")

                val fos = FileOutputStream(path)

                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.close()


            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    // Creates a bitmap from the image
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {

        Timber.tag("MainActivity").v("uriToBitmap()")

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

    // MENU FUNCTIONS AND CONSTRUCTORS -------------------------------------------------------------

    // Menu construction
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
        return true
    }

    // When language or theme button in menu is pressed,
    // changes user preference and reload the activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.languageOptionMenu -> {
            Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu")

            when (sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().language)) {
                "fi" -> {

                    Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu - fi")

                    // Sets preference
                    with(sharedPreferences.edit()) {

                        putString("SELECTED_LANGUAGE", "en")
                        apply()

                    }

                    // Reloads the Activity
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)

                }
                "en" -> {

                    Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu - en")

                    // Sets preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "fi")
                        apply()
                    }

                    // Reloads the Activity
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)

                }
                else -> {

                    Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu - en")

                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }

                    // Reloads the Activity
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)

                }

            }

            true

        }

        R.id.dayNightOptionMenu -> {

            Timber.tag("MainActivity").v("onOptionsItemSelected() - dayNightOptionMenu")

            when (sharedPreferences.getBoolean("SELECTED_THEME", (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES))) {

                true -> {

                    Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu - light")

                    //  Sets preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", false)
                        apply()
                    }

                    // Reloads the Activity
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)

                }
                false -> {

                    Timber.tag("MainActivity").v("onOptionsItemSelected() - languageOptionMenu - dark")

                    // Sets preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", true)
                        apply()
                    }

                    // Reloads the Activity
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)

                }
            }
            true
        }

        else -> {

            // The user's action was not recognized.
            // Invokes the superclass to handle it.
            super.onOptionsItemSelected(item)

        }

    }

}


