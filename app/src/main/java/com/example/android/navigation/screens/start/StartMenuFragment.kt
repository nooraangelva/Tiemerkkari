package com.example.android.navigation.screens.start

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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

        // Sets title to activity bar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleStart)

        // BUTTONS ---------------------------------------------------------------------------------

        // Sets the onClickListener for button start, navigates to selecting a sign to print
        binding.startButton.setOnClickListener { view: View ->

            Timber.tag("StartMenuFragment").v("Start pressed")

            view.findNavController().navigate(
                StartMenuFragmentDirections.actionStartMenuFragmentToSpeedAreaFragment(false)
            )

        }

        // Sets the onClickListener for button start, navigates to guide
        binding.guideButton.setOnClickListener { view: View ->

            Timber.tag("StartMenuFragment").v("Star menu - guide pressed")

            view.findNavController()
                .navigate(StartMenuFragmentDirections.actionStartMenuFragmentToGuideFragment())

        }

        // Sets the onClickListener for button start, navigates to importing a new sign
        binding.importLayoutButton.setOnClickListener { view: View ->

            Timber.tag("StartMenuFragment").v("Star menu - import pressed")

            view.findNavController()
                .navigate(StartMenuFragmentDirections.actionStartMenuFragmentToImportFragment())

        }

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU ----------------------------------------------------------------------------------------

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