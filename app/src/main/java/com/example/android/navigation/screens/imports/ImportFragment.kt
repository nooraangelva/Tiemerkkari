package com.example.android.navigation.screens.imports

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.navigation.MainActivity
import com.example.android.navigation.R
import com.example.android.navigation.Step
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentImportBinding
import com.example.android.navigation.screens.speed_area.SpeedAreaFragmentDirections
import timber.log.Timber

class ImportFragment : Fragment(){

    private lateinit var viewModel: ImportViewModel
    private lateinit var viewModelFactory: ImportViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentImportBinding

    val stepList : ArrayList<Step> = ArrayList()
    private lateinit var stepAdapter : StepAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleImport)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_import, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        viewModelFactory = ImportViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ImportViewModel::class.java)

        binding.lifecycleOwner = this
        navController = findNavController()

        binding.viewModel = viewModel

        //Recycleview stuff

        val manager = LinearLayoutManager(activity)

        binding.importRecycleView.layoutManager = manager

        binding.importButton.isVisible = false
        binding.cancelButtonImport.isVisible = false

        /*
        val adapter = ImportAdapter(SignListener { signId ->
            Toast.makeText(context, "$signId", Toast.LENGTH_LONG).show()
            Log.v("Buttons","SignId - $signId chosen")
            //TODO call initialize sign
            viewModel.initializeStep()

        })

        binding.importListRecycleView.adapter = adapter*/


        binding.lifecycleOwner = this
        navController = findNavController()
        setHasOptionsMenu(true)

        setAdapter()

        //TYPE SPINNER
        val spinner: Spinner = binding.typeInput
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity().applicationContext,
            R.array.type_options_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // set on-click listener
        binding.newSignImageImportButton.setOnClickListener {

            (activity as MainActivity).openGalleryForImage()
        }

        binding.newStepButton.setOnClickListener{
            stepList.add(Step())
            stepAdapter.notifyItemInserted(stepList.size)
        }

        binding.createSignButtonImport.setOnClickListener{

            if((activity as MainActivity).pathInString != "") {
                val imagePath = (activity as MainActivity).pathInString
                viewModel.savedImagepath(imagePath)
                viewModel.createSign()
                stepList.add(Step())
                stepAdapter.notifyItemInserted(stepList.size)
                Toast.makeText(context,"Sign saved.",Toast.LENGTH_SHORT)
            }
            else{
                Toast.makeText(context, "Get Picture first", Toast.LENGTH_LONG).show()
            }
        }

        binding.cancelButtonImport.setOnClickListener{
            viewModel.delete()
            Toast.makeText(context,"Created sign and steps deleted",Toast.LENGTH_SHORT)
        }

        binding.newStepButton.setOnClickListener{
            stepList.add(Step())
            stepAdapter.notifyItemInserted(stepList.size)
        }

        binding.importButton.setOnClickListener{
            stepList.forEachIndexed { index, step ->
                Timber.i("Import O: " + step.order)
                viewModel.saveSteps(step, index)
            }
            Toast.makeText(context,"Steps saved",Toast.LENGTH_SHORT)
        }

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })

        viewModel.signCreated.observe(viewLifecycleOwner, Observer { created ->
            binding.createSignButtonImport.isVisible = !viewModel.signCreated.value!!
            binding.importButton.isVisible = viewModel.signCreated.value!!
            binding.newStepButton.isVisible = viewModel.signCreated.value!!
            binding.cancelButtonImport.isVisible = viewModel.signCreated.value!!

        })


        return binding.root
    }



    private fun setAdapter(){
        stepAdapter = StepAdapter(stepList, RadioButtonListener { choice,order ->
            //Toast.makeText(context,choice.toString(),Toast.LENGTH_SHORT)
            if(choice >4){
                stepList[order-1].paint = choice
            }
            else{
                stepList[order-1].order = choice
            }

        })
        binding.importRecycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = stepAdapter
        }
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


