
package com.example.android.navigation.screens.speed_area

import android.os.Bundle

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSpeedAreaBinding
import timber.log.Timber

class SpeedAreaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflates the layout for this fragment
        val binding: FragmentSpeedAreaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_speed_area, container, false
        )

        // Sets title to activity bar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleSpeedArea)

        // BUTTONS ---------------------------------------------------------------------------------

        // Sets the onClickListener for button city, navigates to speed area with a parameter
        binding.cityButtonSpeedArea.setOnClickListener { view: View ->

            Timber.tag("SpeedAreaFragment").v("City pressed")

            view.findNavController().navigate(
                SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(true)
            )

        }

        // Sets the onClickListener for button city, navigates to speed area with a parameter
        binding.outsideCityButtonSpeedArea.setOnClickListener { view: View ->

            Timber.tag("SpeedAreaFragment").v("Outside city pressed")

            view.findNavController().navigate(
                SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(false))

        }

        // Shows back button
        setHasOptionsMenu(true)

        return binding.root

    }

    // MENU ----------------------------------------------------------------------------------------

    override fun onPrepareOptionsMenu(menu: Menu){

        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.languageOptionMenu)
        item.isVisible = false
        val item2 = menu.findItem(R.id.dayNightOptionMenu)
        item2.isVisible = false

    }

}



