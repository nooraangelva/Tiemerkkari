

package com.example.android.navigation

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    val MyPREFERENCES = "MyPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "OnCreate")

        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)

        languageChange()
        Log.i("MainActivity", "Langugae pref: " + sharedPreferences.getString("language", "en")!!)
        checkTheme()
        Log.i("MainActivity", "Langugae pref: " + sharedPreferences.getBoolean("SELECTED_THEME", false))

        setContentView(R.layout.activity_main)

        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        Log.i("MainActivity", "nav graph" + navController.graph.toString())
        findViewById<Toolbar>(R.id.toolBar)
                .setupWithNavController(navController, appBarConfiguration)

    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        invalidateOptionsMenu()
        menu.findItem(R.id.languageModeMenu).isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    fun languageChange()
    {
        val currentLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", "fi")

        Log.i("MainActivity", "setApplocale()" + currentLanguage)

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

    private fun checkTheme() {

        Log.i("MainActivity", "checkTheme()" + sharedPreferences.getBoolean("SELECTED_THEME", false))

        val darkMode = sharedPreferences.getBoolean("SELECTED_THEME", false)
        if(!darkMode)
        {
            setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
        }
        else
        {
            setTheme(R.style.ThemeOverlay_AppCompat_Light)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        languageChange()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {


        R.id.languageModeMenu -> {


            when (sharedPreferences.getString("language", "en")) {
                "fi" -> {

                    Log.i("MainActivity", "languageModeMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }

                    this.recreate()

                }
                "en" -> {

                    Log.i("MainActivity", "languageModeMenu pressed to fi")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "fi")
                        apply()
                    }

                    this.recreate()

                }
                else -> {

                    Log.i("MainActivity", "languageModeMenu pressed to en")
                    // set preference
                    with(sharedPreferences.edit()) {
                        putString("SELECTED_LANGUAGE", "en")
                        apply()
                    }

                    this.recreate()
                }
            }

            true
        }

        R.id.colorTheme -> {

            when (sharedPreferences.getBoolean("colorMode", false)) {
                true -> {

                    Log.i("MainActivity", "colorTheme pressed to day")

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", false)
                        apply()
                    }

                    this.recreate()

                }
                false -> {

                    Log.i("MainActivity", "colorTheme pressed to night")

                    // set preference
                    with(sharedPreferences.edit()) {
                        putBoolean("SELECTED_THEME", true)
                        apply()
                    }

                    this.recreate()

                }
            }
            true
        }

        R.id.aboutMenu -> {
            //TODO Fragment to about
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}


