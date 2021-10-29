package com.example.android.navigation.screens.imports

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentImportBinding
import com.example.android.navigation.databinding.FragmentPrintingBinding
import com.example.android.navigation.screens.printing.PrintingViewModel
import com.example.android.navigation.screens.printing.PrintingViewModelFactory

class ImportFragment : Fragment() {

    private lateinit var viewModel: ImportViewModel
    private lateinit var viewModelFactory: ImportViewModelFactory
    private lateinit var navController: NavController
    private lateinit var binding: FragmentImportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_import, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        viewModelFactory = ImportViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ImportViewModel::class.java)

        binding.setLifecycleOwner(this)
        navController = findNavController()

        binding.viewModel = viewModel

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