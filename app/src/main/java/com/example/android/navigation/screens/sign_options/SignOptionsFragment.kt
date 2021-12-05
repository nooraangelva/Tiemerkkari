package com.example.android.navigation.screens.sign_options

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.navigation.R
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentSignOptionsBinding


class SignOptionsFragment : Fragment() {

    private lateinit var viewModel: SignOptionsViewModel
    private lateinit var viewModelFactory: SignOptionsViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflates the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_options, container, false)

        // Sets title to activity bar
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleSignOptions)

        binding.lifecycleOwner = this

        // VIEWMODEL -------------------------------------------------------------------------------

        // Gets arguments
        val signOptionsFragmentArgs by navArgs<SignOptionsFragmentArgs>()

        val application = requireNotNull(this.activity).application
        // Database
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        // Creates viewModel for the fragment with parameters
        viewModelFactory = SignOptionsViewModelFactory(
            signOptionsFragmentArgs.area, signOptionsFragmentArgs.typeInt, dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignOptionsViewModel::class.java]

        // Binds viewModel to fragment
        binding.viewModel = viewModel

        // RECYCLERVIEW ----------------------------------------------------------------------------

        // Gets navigation
        navController = findNavController()

        // Creates recycler with GridLayout, every line has three signs
        val manager = GridLayoutManager(activity, 3)
        binding.signListRecycleView.layoutManager = manager

        // Sets an adapter for the views to be recycled (sets new values) when scrolled
        val adapter = SignOptionsAdapter(SignListener { signId ->

            // When a sign is clicked navigates to printing fragment
            navController.navigate(
                SignOptionsFragmentDirections.actionSignOptionsFragmentToPrintingFragment(signId))

        })

        // Ties the adapter to Layouts recyclerview
        binding.signListRecycleView.adapter = adapter

        // Submits the list of signs when it has been retrieved
        viewModel.sign.observe(viewLifecycleOwner, Observer {

            it?.let {

                adapter.submitList(it)

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