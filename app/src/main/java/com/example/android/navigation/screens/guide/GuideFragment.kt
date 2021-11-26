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

package com.example.android.navigation.screens.guide

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.MainActivity
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentGuideBinding
import com.example.android.navigation.databinding.FragmentStartMenuBinding
import com.example.android.navigation.screens.start.StartMenuFragmentDirections
import timber.log.Timber

class GuideFragment : Fragment() {

    private lateinit var binding: FragmentGuideBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_guide, container, false
        )

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleGuide)

        // Inflate the layout for this fragment


        // ADDS SIDE MENU
        binding.startButton.setOnClickListener { view: View ->
            binding.

        }
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

