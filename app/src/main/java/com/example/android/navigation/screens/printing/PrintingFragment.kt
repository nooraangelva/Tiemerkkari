package com.example.android.navigation.screens.printing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.R
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentPrintingBinding
import com.example.android.navigation.screens.printing.*

class PrintingFragment : Fragment() {

    private lateinit var viewModel: PrintingViewModel
    private lateinit var viewModelFactory: PrintingViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentPrintingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_printing, container, false)


        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        // Get arguments
        val printingFragmentArgs by navArgs<PrintingFragmentArgs>()
        viewModelFactory = PrintingViewModelFactory(printingFragmentArgs.signId, dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(PrintingViewModel::class.java)

        binding.viewModel = viewModel

        /*
        val adapter = SignOptionsAdapter(SignListener { signId ->
            Toast.makeText(context, "$signId", Toast.LENGTH_LONG).show()

            Log.v("Buttons", "SignId - $signId chosen")
            navController.navigate(SignOptionsFragmentDirections.actionSignOptionsFragmentToPrintingFragmnet(signId))

        })


        viewModel.sign.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })*/

        binding.setLifecycleOwner(this)

        navController = findNavController()

        //TODO Sets the onClickListener for finished

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