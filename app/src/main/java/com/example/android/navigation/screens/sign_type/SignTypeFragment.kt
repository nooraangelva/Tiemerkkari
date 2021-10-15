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
import androidx.navigation.findNavController

import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignTypeBinding


class SignTypeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignTypeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_type, container, false)

        // SetS the onClickListener for buttons

        binding.arrowsButton.setOnClickListener { view: View ->

            Log.v("Buttons","Sign type - arrow pressed")
            view.findNavController().navigate(R.id.action_signTypeFragment_to_signOptionsFragment)

        }

        binding.speedLimitButton.setOnClickListener { view: View ->

            Log.v("Buttons","Sign type - speed limit pressed")
            view.findNavController().navigate(R.id.action_signTypeFragment_to_signOptionsFragment)

        }

        binding.othersButton.setOnClickListener { view: View ->

            Log.v("Buttons","Sign type - others pressed")
            view.findNavController().navigate(R.id.action_signTypeFragment_to_signOptionsFragment)

        }

        // ADDS SIDE MENU

        setHasOptionsMenu(true)

        return binding.root
    }

    // MENU FUNCTIONS

    private fun getShareIntent() : Intent {
        //val args = Si.fromBundle(requireArguments())
        return ShareCompat.IntentBuilder.from(activity!!)
                .setText("")
                .setType("text/plain")
                .intent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
