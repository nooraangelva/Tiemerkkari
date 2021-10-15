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

package com.example.android.navigation.screens.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentStartMenuBinding

class StartMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentStartMenuBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_start_menu, container, false)

        // Sets the onClickListener for buttons

        binding.startButton.setOnClickListener { view: View ->

            Log.v("Buttons","Star menu - start pressed")
            view.findNavController().navigate(R.id.action_startMenuFragment_to_speedAreaFragment)

        }

        binding.guideButton.setOnClickListener { view: View ->

            Log.v("Buttons","Star menu - guide pressed")
            view.findNavController().navigate(R.id.action_startMenuFragment_to_guideFragment)

        }

        binding.importLayoutButton.setOnClickListener { view: View ->

            Log.v("Buttons","Star menu - import pressed")
            view.findNavController().navigate(R.id.action_startMenuFragment_to_importFragment)

        }

        // ADDS SIDE MENU

        setHasOptionsMenu(true)
        return binding.root
    }

    // MENU FUNCTIONS

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
