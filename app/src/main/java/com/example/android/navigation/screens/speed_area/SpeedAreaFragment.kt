/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        viewModelFactory = SpeedAreaViewModelFactory("")

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

        viewModel.cityChosen.observe(viewLifecycleOwner, Observer { cityChosen ->
            if (cityChosen) {
                Log.v("Buttons", "Speed area - city pressed")

                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("",viewModel.area.value!!))
                viewModel.cityChosenComplete()
            }
        })

        viewModel.countyChosen.observe(viewLifecycleOwner, Observer { countyChosen ->
            if (countyChosen) {
                Log.v("Buttons", "Speed area - outsideCity pressed")
                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("",viewModel.area.value!!))
                viewModel.countyChosenComplete()
            }
        })

        // ADDS SIDE MENU

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU FUNCTIONS

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}



