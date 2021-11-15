package com.example.android.navigation.screens.sign_options

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.navigation.R
import com.example.android.navigation.database.SignApplication
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.database.SignRepository
import com.example.android.navigation.databinding.FragmentSignOptionsBinding


class SignOptionsFragment : Fragment() {

    private lateinit var viewModel: SignOptionsViewModel
    private lateinit var viewModelFactory: SignOptionsViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_options, container, false)


        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao


        // Get arguments
        val signOptionsFragmentArgs by navArgs<SignOptionsFragmentArgs>()
        viewModelFactory = SignOptionsViewModelFactory(signOptionsFragmentArgs.area, signOptionsFragmentArgs.typeInt, dataSource, (application as SignApplication)
            )

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SignOptionsViewModel::class.java)


        binding.viewModel = viewModel

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.signListRecycleView.layoutManager = manager

        val adapter = SignOptionsAdapter(SignListener { signId ->

            Toast.makeText(context, "$signId", Toast.LENGTH_LONG).show()
            Log.v("Buttons","SignId - $signId chosen")

            navController.navigate(SignOptionsFragmentDirections.actionSignOptionsFragmentToPrintingFragment(signId))

        })

        binding.signListRecycleView.adapter = adapter

        viewModel.signs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        navController = findNavController();

        // Sets the onClickListener for buttons

        /*
        binding.startMenuButton.setOnClickListener { view: View ->

            view.findNavController().navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("city"))
        }*/

        setHasOptionsMenu(true)
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