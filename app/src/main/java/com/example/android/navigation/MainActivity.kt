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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.android.navigation.databinding.ActivityMainBinding
import timber.log.Timber
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.util.Log
import androidx.annotation.UiThread


const val CONNECT = 1
const val SEND = 2
const val RECEIVE = 3
const val QUIT_MSG = 4

val SERVICE_UUID = UUID.fromString("4fa4c201-1fb5-459e-8fcc-c5c9c331914b")!!
val CHARACTERISTIC_UUID_SEND = UUID.fromString("4ec4893c-6c9e-478e-9ad2-7a964946bc86")!!
val CHARACTERISTIC_UUID_RECEIVE = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")!!

const val REQUEST_CODE_PERMISSION = 100
const val RESULT_LOAD_IMAGE = 200
val REQUIRED_PERMISSIONS = arrayOf("android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.BLUETOOTH","android.permission.BLUETOOTH_SCAN","android.permission.BLUETOOTH_ADMIN",
    "android.permission.BLUETOOTH_CONNECT","android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.ACCESS_FINE_LOCATION")

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController
    var mainThreadHandler : Handler? = null
    lateinit var runnable: ThreadHandler


    lateinit var bitmap : Bitmap
    private lateinit var imageName: String
    lateinit var  pathInString : String
    private lateinit var menu: Menu

    lateinit var thread : Thread


    private val _receivedMessage = MutableLiveData<String>()
    val receivedMessage: LiveData<String>
        get() = _receivedMessage

    lateinit var sendMessage : String

    private lateinit var binding : ActivityMainBinding

    private val bluetoothAdapter : BluetoothAdapter by lazy{
        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private val btRequestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Bluetooth is now enabled.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("TONIW", "onCreate")
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }


        super.onCreate(savedInstanceState)
        sharedPreferences = getPreferences(MODE_PRIVATE)

        changeLanguage()
        Timber.i("Language pref: " + sharedPreferences.getString("language", "en")!!)
        setTheme()
        Timber.i("Language pref: " + sharedPreferences.getBoolean("SELECTED_THEME", false))

        /*
        when(sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().displayLanguage)){
            "en" -> menu?.getItem(1)?.setIcon(R.drawable.en)
            "fi" -> menu?.getItem(1)?.setIcon(R.drawable.fi)
        }
        when(sharedPreferences.getBoolean("SELECTED_THEME", (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES))){
            true -> menu?.getItem(0)?.setIcon(R.drawable.icon_day_night)
            false -> menu?.getItem(0)?.setIcon(R.drawable.icon_night)

        }*/

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
/*
        if(!bluetoothAdapter.isEnabled){
            openBtActivity()
        }

        mainThreadHandler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message){
                when (msg.what) {
                    SEND -> {
                        //super.handleMessage(msg)
                        Timber.v(""+msg.obj)
                        Toast.makeText(applicationContext, ""+msg.obj, Toast.LENGTH_SHORT).show()
                    }
                    RECEIVE -> {
                        Timber.v(""+msg.obj)
                        _receivedMessage.value = msg.obj.toString() //as LiveData<JsonArray>
                    }
                    CONNECT -> {
                        Timber.v(""+msg.obj)
                        val text = "Connected to device: "+msg.obj
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
        runnable = ThreadHandler(mainThreadHandler, this, bluetoothAdapter)
        thread = Thread(runnable)


        Timber.v(""+Thread.activeCount())


        thread.start()
        //connect()

 */
        Log.v("TONIW", "${Thread.activeCount()}")


        pathInString = ""
        _receivedMessage.value = ""

        //lifecycleOwner = this



    }
fun connect() {
    val msg = Message()
    msg.what = CONNECT

    Timber.v("NULL  " + msg.what)
    runnable.workerThreadHandler!!.sendMessage(msg)

    //Thread.State.TERMINATED
}

   override fun recreate() {
        Log.v("TONIW", "recreate")
       super.recreate()
         /*val msg = Message()
         msg.what = QUIT_MSG

         Timber.v("NULL  " + msg.what)
         runnable.workerThreadHandler!!.sendMessage(msg)
        super.recreate()*/
    }

    override fun onPause() {

        Log.v("TONIW", "onPause")
        super.onPause()
        /*val msg = Message()

        msg.what = QUIT_MSG

        Timber.v("NULL  " + msg.what)
        runnable.workerThreadHandler!!.sendMessage(msg)*/

    }

    override fun onPostResume() {
        super.onPostResume()

        Log.v("TONIW", "onPostResume")
    }

    override fun onDestroy() {
        Log.v("TONIW", "onDestroy")

        super.onDestroy()

/*
        val msg = Message()

        msg.what = QUIT_MSG

        Timber.v("NULL  " + msg.what)
        runnable.workerThreadHandler!!.sendMessage(msg)
*/

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.v("TONIW", "onNewIntent")
    }


    private fun openBtActivity(){

        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        btRequestActivity.launch(intent)

    }


    fun write(data : String){

        val msg = Message()
        msg.what = SEND
        msg.obj = data
        runnable.workerThreadHandler!!.sendMessage(msg)


    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
        return true
    }

    private fun changeLanguage() {
        val newLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().language)
        val currentLanguage = Locale.getDefault().language
        Timber.i("setApplocale()" + sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().language))

        Log.v("TONIW", "language new ${newLanguage}")
        Log.v("TONIW", "language current ${currentLanguage}")

        if(currentLanguage !== newLanguage) {
            val locale = Locale(newLanguage!!)

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


    }

    private fun allPermissionsGranted() : Boolean{
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
            val file = wrapper.getDir("sign_images", MODE_PRIVATE)

            Timber.i("Langugae pref:"+file)

            try {

                val mypath = File(file, "${imageName}.jpg")
                pathInString = mypath.path
                val fos = FileOutputStream(mypath)
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

    private fun setTheme() {

        val darkMode = sharedPreferences.getBoolean("SELECTED_THEME", (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES))
        if (darkMode) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {


        R.id.languageOptionMenu -> {
            Log.v("TONIW", "languageOptionMenu")

            when (sharedPreferences.getString("SELECTED_LANGUAGE", Locale.getDefault().language)) {
                "fi" -> {

                    Timber.i("languageOptionMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }

                    // FIXME JOS ON JO OIKEA KIELI NIIN ALA VAIHTA
                    this.recreate()



                }
                "en" -> {


                    Timber.i("languageOptionMenu pressed to fi")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "fi")
                        apply()
                    }

                    this.recreate()

                }
                else -> {
                    Log.v("TONIW", "languageOptionMenu else ")
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
            Log.v("TONIW", "dayNightOptionMen ")
            when (sharedPreferences.getBoolean("SELECTED_THEME", (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES))) {
                true -> {


                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", false)
                        apply()
                    }

                    this.recreate()

                }
                false -> {
                    Log.v("TONIW", "dayNightOptionMen else ")

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
            Log.v("TONIW", "onOptionsItemSelected")
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}


