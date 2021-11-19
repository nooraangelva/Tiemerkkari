package com.example.android.navigation.screens.start

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentStartMenuBinding
import timber.log.Timber

class StartMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: FragmentStartMenuBinding = inflate(
                inflater, R.layout.fragment_start_menu, container, false)


        // Sets the onClickListener for buttons

        binding.startButton.setOnClickListener { view: View ->

            Timber.tag("Buttons").v("Star menu - start pressed")
            view.findNavController().navigate(
                StartMenuFragmentDirections.actionStartMenuFragmentToSpeedAreaFragment(false)
            )

        }

        binding.guideButton.setOnClickListener { view: View ->

            Timber.tag("Buttons").v("Star menu - guide pressed")
            view.findNavController()
                .navigate(StartMenuFragmentDirections.actionStartMenuFragmentToGuideFragment())

        }

        binding.importLayoutButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Star menu - import pressed")
            view.findNavController().navigate(StartMenuFragmentDirections.actionStartMenuFragmentToImportFragment())

        }

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU FUNCTIONS

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        val item2 = menu.findItem(R.id.dayNightOptionMenu)
        val item = menu.findItem(R.id.languageOptionMenu)

        if(!item.isVisible) {

            item.isVisible = true
            item2.isVisible = true
        }
    }

}