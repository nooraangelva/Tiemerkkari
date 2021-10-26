package com.example.android.navigation.screens.start

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentStartMenuBinding

class StartMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentStartMenuBinding = inflate(
                inflater, R.layout.fragment_start_menu, container, false)


        // Sets the onClickListener for buttons

        binding.startButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Star menu - start pressed")
            view.findNavController().navigate(StartMenuFragmentDirections.actionStartMenuFragmentToSpeedAreaFragment(false))

        }

        binding.guideButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Star menu - guide pressed")
            view.findNavController().navigate(StartMenuFragmentDirections.actionStartMenuFragmentToGuideFragment())

        }

        binding.importLayoutButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Star menu - import pressed")
            view.findNavController().navigate(StartMenuFragmentDirections.actionStartMenuFragmentToImportFragment())

        }

        setHasOptionsMenu(true)
        return binding.root
    }

// MENU FUNCTIONS

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}