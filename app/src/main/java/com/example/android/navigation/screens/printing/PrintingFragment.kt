package com.example.android.navigation.screens.printing

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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
        (activity as MainActivity).applicationContext
        // Get arguments
        val printingFragmentArgs by navArgs<PrintingFragmentArgs>()
        viewModelFactory = PrintingViewModelFactory(printingFragmentArgs.signId, dataSource, application,(activity as MainActivity).bluetoothAdapter, (activity as MainActivity).applicationContext  )

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(PrintingViewModel::class.java)

        binding.viewModel = viewModel




        viewModel.isPrinting.observe(viewLifecycleOwner, Observer {
            binding.printingButton.isVisible = !it
            binding.printingStopButton.isVisible = it
        })


        //ViewModel adding image to imageview
        viewModel.getData.observe(viewLifecycleOwner, Observer {

            binding.printingImageView.setImageURI(viewModel.signSource.value?.toUri())

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

            viewModel.energencyStop()
            var array = """{"Commands":["STOP"]}"""

            viewModel.write(array)
        }

        binding.printingButton.setOnClickListener {

            viewModel.startPrinting()
            viewModel.steps.value?.forEachIndexed { index, step ->

                var array = """{"Commands":["${step.order}","${step.parY}","${step.parX}","${step.paint}","${step.step}" ]}"""

                viewModel.write(array)
            }

            binding.progressBarPrinting.isVisible = true
            binding.printingprogress.isVisible = true


        }
        viewModel.connected.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                Toast.makeText(
                    context,
                    "Connected to device: ${viewModel.device.value}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(
                    context,
                    "Disconnected from device: ${viewModel.device.value}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


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