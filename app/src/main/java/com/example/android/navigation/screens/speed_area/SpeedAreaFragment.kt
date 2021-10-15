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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSpeedAreaBinding

class SpeedAreaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSpeedAreaBinding>(
                inflater, R.layout.fragment_speed_area, container, false)


        // Sets the onClickListener for buttons

        binding.cityButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Speed area - city pressed")

            view.findNavController().navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("city"))

        }

        binding.outsideCityButton.setOnClickListener { view: View ->

            Log.v("Buttons", "Speed area - outsideCity pressed")

            view.findNavController().navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("country"))
        }

        return binding.root
    }


}
