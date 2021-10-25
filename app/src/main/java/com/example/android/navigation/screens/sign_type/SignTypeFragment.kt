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

package com.example.android.navigation.screens.sign_type

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.core.app.ShareCompat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI

import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignTypeBinding
import com.example.android.navigation.screens.speed_area.SpeedAreaFragmentDirections


class SignTypeFragment : Fragment() {

    private lateinit var viewModel: SignTypeViewModel
    private lateinit var viewModelFactory: SignTypeViewModelFactory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignTypeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_type, container, false)

        // Get arguments
        val signTypeFragmentArgs by navArgs<SignTypeFragmentArgs>()
        viewModelFactory = SignTypeViewModelFactory(signTypeFragmentArgs.area)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SignTypeViewModel::class.java)


        binding.vm = viewModel
        binding.setLifecycleOwner(this)

        val navController = findNavController();

        // Sets the listener for buttons

        viewModel.arrowsChosen.observe(viewLifecycleOwner, Observer { arrowsChosen ->
            if (arrowsChosen) {

                Log.v("Buttons","Sign type - arrow pressed")

                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionArrowsChosenComplete()
            }
        })

        viewModel.speedLimitsChosen.observe(viewLifecycleOwner, Observer { speedLimitsChosen ->
            if (speedLimitsChosen) {
                Log.v("Buttons","Sign type - speed limit pressed")

                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionSpeedLimitsChosenComplete()
            }
        })

        viewModel.othersChosen.observe(viewLifecycleOwner, Observer { othersChosen ->
            if (othersChosen) {
                Log.v("Buttons","Sign type - others pressed")

                navController.navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionOthersChosenComplete()
            }
        })

        // ADDS MENU

        setHasOptionsMenu(true)

        return binding.root
    }

    // MENU FUNCTIONS

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.iconless_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
