
package com.example.android.navigation.screens.speed_area

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSpeedAreaBinding
import com.example.android.navigation.screens.sign_type.SignTypeViewModelFactory

class SpeedAreaFragment : Fragment() {

    private lateinit var viewModel: SpeedAreaViewModel
    private lateinit var viewModelFactory: SpeedAreaViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSpeedAreaBinding>(
                inflater, R.layout.fragment_speed_area, container, false)

        viewModelFactory = SpeedAreaViewModelFactory()

        // Get the viewmodel
        viewModel =ViewModelProvider(this, viewModelFactory)
                .get(SpeedAreaViewModel::class.java)



        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.vm = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        val navController = findNavController();


        // Sets the listener for buttons

        viewModel.completeChosen.observe(viewLifecycleOwner, Observer { chosen ->
            if (viewModel.cityChosen.value!!) {
                Log.v("Buttons", "Speed area - city pressed")

                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(0,viewModel.cityChosen.value!!))
                viewModel.cityChosenComplete()
            }
            else{
                Log.v("Buttons", "Speed area - outsideCity pressed")
                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(0,viewModel.cityChosen.value!!))
                viewModel.countyChosenComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU FUNCTIONS

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.iconless_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}



