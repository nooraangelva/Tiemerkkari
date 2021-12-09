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
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignTypeBinding
import timber.log.Timber


class SignTypeFragment : Fragment() {

    // ViewModel variables
    private lateinit var viewModel: SignTypeViewModel
    private lateinit var viewModelFactory: SignTypeViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflates the layout for this fragment
        val binding: FragmentSignTypeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_type, container, false)

        // Sets title to activity bar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleSignType)

        binding.lifecycleOwner = this

        // VIEWMODEL -------------------------------------------------------------------------------

        // Gets argument from navigation and gives it to viewModel when creating it
        val signTypeFragmentArgs by navArgs<SignTypeFragmentArgs>()
        viewModelFactory = SignTypeViewModelFactory(signTypeFragmentArgs.area)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignTypeViewModel::class.java]

        // Binds viewModel to fragment, sets lifecycleOwner
        // and gets navController for navigation
        binding.viewModel = viewModel

        // BUTTON OBSERVERS ------------------------------------------------------------------------

        // Gets navigation
        val navController = findNavController();

        // Observers if arrows is chosen
        viewModel.arrowsChosen.observe(viewLifecycleOwner, Observer { arrowsChosen ->

            if (arrowsChosen) {

                Timber.tag("SignTypeFragment").v("Arrows pressed")

                navController.navigate(SignTypeFragmentDirections.
                actionSignTypeFragmentToSignOptionsFragment
                    (0,viewModel.area.value!!))

                viewModel.optionArrowsChosenComplete()

            }

        })

        // Observers if speed limits is chosen
        viewModel.speedLimitsChosen.observe(viewLifecycleOwner, Observer { speedLimitsChosen ->

            if (speedLimitsChosen) {

                Timber.tag("SignTypeFragment").v("Speed limits pressed")

                navController.navigate(SignTypeFragmentDirections.
                actionSignTypeFragmentToSignOptionsFragment
                    (1,viewModel.area.value!!))

                viewModel.optionSpeedLimitsChosenComplete()

            }

        })

        // Observers if others is chosen
        viewModel.othersChosen.observe(viewLifecycleOwner, Observer { othersChosen ->

            if (othersChosen) {

                Timber.tag("SignTypeFragment").v("Others pressed")

                navController.navigate(SignTypeFragmentDirections.
                actionSignTypeFragmentToSignOptionsFragment
                    (2,viewModel.area.value!!))

                viewModel.optionOthersChosenComplete()

            }

        })

        // Shows back button
        setHasOptionsMenu(true)

        return binding.root

    }

    // MENU ----------------------------------------------------------------------------------------

    override fun onPrepareOptionsMenu(menu: Menu){

        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.languageOptionMenu)
        item.isVisible = false
        val item2 = menu.findItem(R.id.dayNightOptionMenu)
        item2.isVisible = false

    }

}
