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

package com.example.android.navigation.screens.score

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
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
import com.example.android.navigation.databinding.FragmentGameWonBinding
import com.example.android.navigation.screens.score.GameWonFragmentArgs
import com.example.android.navigation.screens.score.GameWonFragmentDirections
import com.example.android.navigation.R



class GameWonFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false)

        val scoreFragmentArgs by navArgs<GameWonFragmentArgs>()
        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.numCorrect,
                scoreFragmentArgs.gameTime, scoreFragmentArgs.numQuestions, scoreFragmentArgs.gameType)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ScoreViewModel::class.java)

        //TODO mika moka?
        binding.vm = viewModel
        binding.setLifecycleOwner(this)

        //TODO oikessa paikkaa vai viewmodeliin?
        // Add observer for score

        viewModel.tripleMediatorLiveData.observe(viewLifecycleOwner, Observer { newData ->
            binding.scoreTextView.setText(getString(R.string.share_success_text, newData.first, newData.third, newData.second))
        })

        // Starts the game again when button is pressed
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { playAgain ->
            if (playAgain) {
                findNavController().navigate(
                        GameWonFragmentDirections.actionGameWonFragmentToGameFragment(viewModel.gameType.value!!))
                viewModel.onPlayAgainComplete()
            }
        })

        // Navigates back to title when button is pressed
        viewModel.eventPlayNewGame.observe(viewLifecycleOwner, Observer { playNewGame ->
            if (playNewGame) {
                findNavController().navigate(
                        GameWonFragmentDirections.actionGameWonFragmentToTitleFragment())
                viewModel.onPlayNewGameComplete()
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }



//TODO mita tehda naille? viewmodeliin?


    private fun getShareIntent() : Intent {

        return ShareCompat.IntentBuilder.from(activity!!)
                .setText(getString(R.string.share_success_text, viewModel.score.value!!, viewModel.numQuestions.value!!, viewModel.gameTime.value!!))
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
