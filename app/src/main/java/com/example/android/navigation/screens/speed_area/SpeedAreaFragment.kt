
package com.example.android.navigation.screens.speed_area

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSpeedAreaBinding
import com.example.android.navigation.databinding.FragmentStartMenuBinding
import com.example.android.navigation.screens.start.StartMenuFragmentDirections
import timber.log.Timber

class SpeedAreaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentSpeedAreaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_speed_area, container, false
        )


        // Sets the onClickListener for buttons

        binding.cityButton.setOnClickListener { view: View ->

            Timber.tag("Buttons").v("Star menu - start pressed")
            view.findNavController().navigate(
                SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(true)
            )

        }

        binding.outsideCityButton.setOnClickListener { view: View ->

            Timber.tag("Buttons").v("Star menu - guide pressed")
            view.findNavController()
                .navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(false))

        }

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU FUNCTIONS

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.languageOptionMenu)
        item.isVisible = false
        val item2 = menu.findItem(R.id.dayNightOptionMenu)
        item2.isVisible = false
    }
}



