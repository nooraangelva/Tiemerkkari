package com.example.android.navigation.screens.printing

import android.os.Bundle
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
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentPrintingBinding
import kotlinx.coroutines.delay


class PrintingFragment : Fragment() {

    private lateinit var viewModel: PrintingViewModel
    private lateinit var viewModelFactory: PrintingViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentPrintingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Sets title for fragment
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titlePrinting)


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_printing, container, false)

        // Instance of the database
        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao
        (activity as MainActivity).applicationContext

        // Get arguments and creates viewModel with wanted values
        val printingFragmentArgs by navArgs<PrintingFragmentArgs>()
        viewModelFactory = PrintingViewModelFactory(printingFragmentArgs.signId, dataSource,
            application,(activity as MainActivity).bluetoothAdapter,
            (activity as MainActivity).applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[PrintingViewModel::class.java]
        binding.viewModel = viewModel

        // set lifecycle owner and ties navController to a variable
        binding.lifecycleOwner = this
        navController = findNavController()

        // Sets initial visibility to buttons
        binding.printingButtonPrinting.isVisible = false
        binding.stopPrintingButtonPrinting.isVisible = false

        // Observes printing status and controls button visibility based on it
        viewModel.isPrinting.observe(viewLifecycleOwner, Observer {

            if(viewModel.connected.value!!) {

                binding.printingButtonPrinting.isVisible = !it
                binding.stopPrintingButtonPrinting.isVisible = it
                binding.progressBarPrinting.isVisible = it
                binding.progressPrinting.isVisible = it

            }
            else{

                binding.printingButtonPrinting.isVisible = false
                binding.stopPrintingButtonPrinting.isVisible = false
            }



        })

        viewModel.connected.observe(viewLifecycleOwner, Observer {

            viewModel.connected()

        })




        //ViewModel adding image to imageview
        viewModel.getData.observe(viewLifecycleOwner, Observer {

            binding.signImagePrinting.setImageURI(viewModel.signSource.value?.toUri())

        })


        // Observer when printing is finished and then moves to startFragment
        viewModel.finished.observe(viewLifecycleOwner, Observer {

            if(it) {

                navController.navigate(PrintingFragmentDirections.actionPrintingFragmentToStartMenuFragment())

            }

        })



        // Observes connection status and controls printing button visibility based on it
        viewModel.connected.observe(viewLifecycleOwner, Observer {

            if(it == true) {

                Toast.makeText(
                    context,
                    "Connected to device.",
                    Toast.LENGTH_SHORT
                ).show()

                binding.printingButtonPrinting.isVisible =true

            }
            else{

                Toast.makeText(
                    context,
                    "Disconnected from device: ${viewModel.device.value}",
                    Toast.LENGTH_SHORT
                ).show()

                binding.printingButtonPrinting.isVisible = false

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