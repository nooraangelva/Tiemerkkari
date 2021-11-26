package com.example.android.navigation.screens.printing

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.MainActivity
import com.example.android.navigation.R
import com.example.android.navigation.SEND
import com.example.android.navigation.Step
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentPrintingBinding
import kotlinx.serialization.json.*
import org.json.JSONArray
import timber.log.Timber


class PrintingFragment : Fragment() {

    private lateinit var viewModel: PrintingViewModel
    private lateinit var viewModelFactory: PrintingViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentPrintingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titlePrinting)


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

        viewModel.isPrinting.observe(viewLifecycleOwner, Observer {
            binding.printingButton.isVisible = !it
            binding.printingStopButton.isVisible = it
        })

        //TODO laita kuva
        //ViewModel adding image to imageview
        viewModel.bitmap.observe(viewLifecycleOwner, Observer {
            binding.printingImageView.setImageBitmap(it)

        })

        binding.setLifecycleOwner(this)

        navController = findNavController()

        //TODO Sets the onClickListener for finished

        /*
        binding.startMenuButton.setOnClickListener { view: View ->

            view.findNavController().navigate(SpeedAreaFragmentDirections.actionSpeedAreaFragmentToSignTypeFragment("city"))
        }*/

        binding.printingButton.isVisible = viewModel.isPrinting.value!!
        binding.printingStopButton.isVisible = !viewModel.isPrinting.value!!

        //(activity as MainActivity).connectBle()

        binding.printingStopButton.setOnClickListener {
            Log.v("ThreadHandler", "check prime number pressed")

            //(activity as MainActivity).write(JSONArray("stop"))
        }

        binding.printingButton.setOnClickListener {
            Log.v("ThreadHandler", "check prime number pressed")
/*
            viewModel.steps.value?.forEachIndexed { index, step ->

                var array = JSONArray(step)
                Timber.i("Import O: " + step.order)
                (activity as MainActivity).write(array)
            }*/
            (activity as MainActivity).write("Testi lahetys")
            binding.progressBarPrinting.isVisible = true
            binding.printingprogress.isVisible = true

        }


        (activity as MainActivity).receivedMessage.observe(viewLifecycleOwner, Observer {
            viewModel.update(it)
            Timber.v(it)
        })

        //(activity as MainActivity).connect()
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