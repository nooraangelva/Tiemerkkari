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

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.MainActivity

import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignTypeBinding


class SignTypeFragment : Fragment() {

    private lateinit var viewModel: SignTypeViewModel
    private lateinit var viewModelFactory: SignTypeViewModelFactory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignTypeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_type, container, false)

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleSignType)


        // Get arguments
        val signTypeFragmentArgs by navArgs<SignTypeFragmentArgs>()
        viewModelFactory = SignTypeViewModelFactory(signTypeFragmentArgs.area)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SignTypeViewModel::class.java)


        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        val navController = findNavController();

        // Sets the listener for buttons

        viewModel.arrowsChosen.observe(viewLifecycleOwner, Observer { arrowsChosen ->
            if (arrowsChosen) {

                Log.v("Buttons","Sign type - arrow pressed")
                navController.navigate(SignTypeFragmentDirections.actionSignTypeFragmentToSignOptionsFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionArrowsChosenComplete()
            }
        })

        viewModel.speedLimitsChosen.observe(viewLifecycleOwner, Observer { speedLimitsChosen ->
            if (speedLimitsChosen) {
                Log.v("Buttons","Sign type - speed limit pressed")
                navController.navigate(SignTypeFragmentDirections.actionSignTypeFragmentToSignOptionsFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionSpeedLimitsChosenComplete()
            }
        })

        viewModel.othersChosen.observe(viewLifecycleOwner, Observer { othersChosen ->
            if (othersChosen) {
                Log.v("Buttons","Sign type - others pressed")

                navController.navigate(SignTypeFragmentDirections.actionSignTypeFragmentToSignOptionsFragment(viewModel.type.value!!,viewModel.area.value!!))
                viewModel.optionOthersChosenComplete()
            }
        })

        // ADDS MENU

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
