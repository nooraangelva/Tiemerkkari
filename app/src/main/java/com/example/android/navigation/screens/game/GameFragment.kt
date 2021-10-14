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

package com.example.android.navigation.screens.game

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.navigation.screens.game.GameFragmentArgs
import com.example.android.navigation.screens.game.GameFragmentDirections
import com.example.android.navigation.screens.score.GameWonFragmentDirections
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentGameBinding
import timber.log.Timber
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings


const val KEY_RIGHT_ANSWERED = "timer_seconds_key"
const val KEY_NUM_QUESTIONS_ASKED = "timer_seconds_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"

class GameFragment : Fragment() {


    private lateinit var gameTimer: GameTimer
    val args = GameFragmentArgs.fromBundle(requireArguments())

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_game,
                container,
                false
        )

        val viewModelFactory = GameViewModelFactory(args.gameType)


        // Get the viewmodel
        val viewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(GameViewModel::class.java)



        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        //TODO mika vikana?
        binding.vm = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        // Sets up event listening to navigate the player when the game is finished
            viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
                if (isFinished) {
                    // We've survived!  Navigate to the gameWonFragment.
                    val currentScore = viewModel.score.value ?: 0
                    val numQuestions = viewModel.numQuestions.value ?: 0
                    val gameType = viewModel.gameType.value ?: 0
                    //TODO linkattu navigaatiossa oikein. miksi error?
                    val action = GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions,currentScore,gameType,gameTimer.secondsCount )

                    viewModel.onGameFinishComplete()
                }
            })

        // Set the listener for the submitButton
        viewModel.eventSubmit.observe(viewLifecycleOwner, Observer { submit ->

            if(submit){
                val checkedId  = binding.questionRadioGroup.checkedRadioButtonId
                viewModel.eventSubmit(checkedId)
                viewModel.submitComplete()
            }

        })

        // Set the listener for the actionbar changes
        viewModel.eventChangeTitle.observe(viewLifecycleOwner, Observer { change ->

            if(change){
                (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, viewModel.questionIndex.value!!.plus(1), viewModel.numQuestions.value)
                viewModel.changeActionBarTitleComplete()
            }

        })



        // Buzzes when triggered with different buzz events
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })


       /*     //TODO fixxaa ja viewModeliin? Tarpeeton?
        if (savedInstanceState != null) {
            // Get all the game state information from the bundle, set it
            result = savedInstanceState.getInt(KEY_RIGHT_ANSWERED, 0)
            questionIndex = savedInstanceState.getInt(KEY_NUM_QUESTIONS_ASKED, 0)
            gameTimer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)

        }*/


        return binding.root

    }
    //TODO getSystemService oikein?
    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }


    /**
     * Called when the user navigates away from the app but might come back

    //TODO fixxaa ja viewModeliin? Tarpeeton?
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_NUM_QUESTIONS_ASKED, questionIndex)
        outState.putInt(KEY_RIGHT_ANSWERED, result)
        outState.putInt(KEY_TIMER_SECONDS, gameTimer.secondsCount)
        Timber.i("onSaveInstanceState Called")
        super.onSaveInstanceState(outState)
    }*/
}
