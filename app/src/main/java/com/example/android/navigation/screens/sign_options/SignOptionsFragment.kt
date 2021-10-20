package com.example.android.navigation.screens.sign_options

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignOptionsBinding


class SignOptionsFragment : Fragment() {

    private lateinit var viewModel: SignOptionsViewModel
    private lateinit var viewModelFactory: SignOptionsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignOptionsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_options, container, false)





        // Get arguments
        val signOptionsFragmentArgs by navArgs<SignOptionsFragmentArgs>()
        viewModelFactory = SignOptionsViewModelFactory(signOptionsFragmentArgs.area, signOptionsFragmentArgs.type)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SignOptionsViewModel::class.java)

        //TODO mika moka?
        //binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        val navController = findNavController();

        // SetS the onClickListener for buttons

        /*
        binding.startMenuButton.setOnClickListener { view: View ->

            view.findNavController().navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("city"))
        }*/

        // ADDS SIDE MENU

        setHasOptionsMenu(true)

        return binding.root
    }



    // MENU FUNCTIONS


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        // check if the activity resolves

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}