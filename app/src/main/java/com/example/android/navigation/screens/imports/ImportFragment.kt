package com.example.android.navigation.screens.imports

import android.os.Bundle
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
import com.example.android.navigation.dataForm.Step
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentImportBinding
import timber.log.Timber


class ImportFragment : Fragment(){

    private lateinit var viewModel: ImportViewModel
    private lateinit var viewModelFactory: ImportViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentImportBinding

    // RecycleView variables
    private val stepList : ArrayList<Step> = ArrayList()
    private lateinit var stepAdapter : StepAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Sets title for fragment
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleImport)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_import, container, false)

        // Instance of the database
        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        // Get arguments and creates viewModel with wanted values
        viewModelFactory = ImportViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ImportViewModel::class.java]
        binding.viewModel = viewModel

        // Set lifecycle owner and ties navController to a variable
        binding.lifecycleOwner = this
        navController = findNavController()

        // Set import and cancel button invisible before
        binding.importButton.isVisible = false
        binding.cancelButtonImport.isVisible = false

        // Showcasing errors
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->

            Toast.makeText(context, error, Toast.LENGTH_LONG).show()

        })

        // Deletes steps and sign
        binding.cancelButtonImport.setOnClickListener{

            viewModel.delete()
            Toast.makeText(context,"Created sign and steps deleted",Toast.LENGTH_SHORT).show()

        }

        // TYPE SPINNER ----------------------------------------------------------------------------

        val spinner: Spinner = binding.typeInput
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity().applicationContext,
            R.array.type_options_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Adds a new step
        binding.newStepButton.setOnClickListener{

            stepList.add(Step())
            stepAdapter.notifyItemInserted(stepList.size)

        }

        binding.newStepButton.setOnClickListener{
            stepList.add(Step())
            stepAdapter.notifyItemInserted(stepList.size)
        }

        // SIGN CREATING ---------------------------------------------------------------------------

        // Sets onclick listener for getting image
        binding.newSignImageImportButton.setOnClickListener {

            (activity as MainActivity).openGalleryForImage()

        }

        binding.createSignButtonImport.setOnClickListener{

            if((activity as MainActivity).pathInString != "") {

                val imagePath = (activity as MainActivity).pathInString
                viewModel.savedImagePath(imagePath)
                viewModel.createSign()
                stepList.add(Step())
                stepAdapter.notifyItemInserted(stepList.size)

                Toast.makeText(context,"Sign saved.",Toast.LENGTH_SHORT).show()

            }
            else{

                Toast.makeText(context, "Get Picture first", Toast.LENGTH_LONG).show()

            }

        }

        viewModel.signCreated.observe(viewLifecycleOwner, Observer { created ->

            Toast.makeText(context, "Sign Created",Toast.LENGTH_SHORT).show()

            binding.createSignButtonImport.isVisible = !viewModel.signCreated.value!!
            binding.importButton.isVisible = viewModel.signCreated.value!!
            binding.newStepButton.isVisible = viewModel.signCreated.value!!
            binding.cancelButtonImport.isVisible = viewModel.signCreated.value!!

        })

        // CREATING STEPS --------------------------------------------------------------------------

        binding.importButton.setOnClickListener{
            stepList.forEachIndexed { index, step ->
                Timber.i("Import O: " + step.order)
                viewModel.saveSteps(step, index)
            }
            Toast.makeText(context,"Steps saved",Toast.LENGTH_SHORT).show()
            binding.importButton.isVisible = false
            binding.newStepButton.isVisible = false
        }

        // RECYCLERVIEW FOR STEPS ------------------------------------------------------------------

        // Sets linearLayout
        val manager = LinearLayoutManager(activity)
        binding.importRecycleView.layoutManager = manager
        setAdapter()

        // Shows back button
        setHasOptionsMenu(true)

        return binding.root

    }



    private fun setAdapter(){
        stepAdapter = StepAdapter(stepList, RadioButtonListener { choice,order ->
            //Toast.makeText(context,choice.toString(),Toast.LENGTH_SHORT)
            if(choice > 4){
                stepList[order].paint = choice
            }
            else{
                Timber.v(""+stepList.size)
                stepList[order].order = choice
            }

        })
        binding.importRecycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = stepAdapter
        }
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


