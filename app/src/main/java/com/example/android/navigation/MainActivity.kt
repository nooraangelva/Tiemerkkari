

package com.example.android.navigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.android.navigation.databinding.ActivityMainBinding
import com.example.android.navigation.screens.speed_area.SpeedAreaFragmentDirections
import com.example.android.navigation.screens.start.StartMenuFragmentDirections
import com.google.android.material.navigation.NavigationView
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setAppLocale(sharedPreferences.getString("language", "en"), this)
        setContentView(R.layout.activity_main)

        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolBar)
                .setupWithNavController(navController, appBarConfiguration)

       // checkTheme()
    }

/*
    fun setAppLocale(languageFromPreference: String?, context: Context)
    {

        if (languageFromPreference != null) {

            val resources: Resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.toLowerCase(Locale.ROOT)))
            resources.updateConfiguration(config, dm)

            // set preference
            with (sharedPreferences.edit()) {
                putString("language", languageFromPreference)
                apply()
            }
        }
    }

    private fun checkTheme() {

        when (sharedPreferences.getInt("colorMode", 1)) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
        }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.languageModeMenu -> {

            when (sharedPreferences.getString("language", "en")) {
                "fi" -> {
                    setAppLocale( "en",this)

                }
                "en" -> {
                    setAppLocale("fi", this)

                }
                else -> {
                    setAppLocale("en", this)
                }
            }

            true
        }

        R.id.colorTheme -> {

            when (sharedPreferences.getInt("colorMode", 1)) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    // set preference
                    with (sharedPreferences.edit()) {
                        putInt("colorMode", 1)
                        apply()
                    }
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                    with (sharedPreferences.edit()) {
                        putInt("colorMode", 0)
                        apply()
                    }
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    with (sharedPreferences.edit()) {
                        putInt("colorMode", 1)
                        apply()
                    }
                }
            }
            true
        }

        R.id.aboutMenu -> {

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }*/

}


