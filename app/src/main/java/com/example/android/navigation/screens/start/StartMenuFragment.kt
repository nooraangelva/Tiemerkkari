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

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.*
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
                inflater, R.layout.fragment_start_menu, container, true)


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

        return binding.root
    }


}
