

package com.example.android.navigation

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "OnCreate")

        super.onCreate(savedInstanceState)

        setAppLocale( this)
        Log.i("MainActivity", "Langugae pref: " + sharedPreferences.getString("language", "en")!!)

        setContentView(R.layout.activity_main)

        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        Log.i("MainActivity", "nav graph" + navController.graph.toString())
        findViewById<Toolbar>(R.id.toolBar)
                .setupWithNavController(navController, appBarConfiguration)


        //setSupportActionBar(findViewById<Toolbar>(R.id.toolBar))

       checkTheme()
    }


    fun setAppLocale(context: Context)
    {
        val currentLanguage = sharedPreferences.getString("SELECTED_LANGUAGE", "fi")

        Log.i("MainActivity", "setApplocale()" + currentLanguage + "," + context)

        val locale = Locale(currentLanguage)

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

        Log.i("MainActivity", "checkTheme()" + sharedPreferences.getInt("colorMode", 1))

        when (sharedPreferences.getInt("colorMode", 0)) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        /*
        R.id.languageModeMenu -> {


            when (sharedPreferences.getString("language", "en")) {
                "fi" -> {

                    Log.i("MainActivity", "languageModeMenu pressed to en")
                    setAppLocale( "en",this)

                }
                "en" -> {

                    Log.i("MainActivity", "languageModeMenu pressed to fi")
                    setAppLocale("fi", this)

                }
                else -> {

                    Log.i("MainActivity", "languageModeMenu pressed to en")
                    setAppLocale("en", this)
                }
            }

            true
        }*/

        R.id.colorTheme -> {

            when (sharedPreferences.getInt("colorMode", 0)) {
                1 -> {

                    Log.i("MainActivity", "colorTheme pressed to day")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()

                    // set preference
                    with(sharedPreferences.edit()) {
                        putInt("colorMode", 0)
                        apply()
                    }

                }
                0 -> {

                    Log.i("MainActivity", "colorTheme pressed to night")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()

                    // set preference
                    with(sharedPreferences.edit()) {
                        putInt("colorMode", 1)
                        apply()
                    }

                }
                else -> {

                    Log.i("MainActivity", "languageModeMenu pressed to day")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()

                    // set preference
                    with(sharedPreferences.edit()) {
                        putInt("colorMode", 0)
                        apply()
                    }
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


