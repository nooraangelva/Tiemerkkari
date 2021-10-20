package com.example.android.navigation.screens.imports

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.navigation.R
import com.example.android.navigation.database.SignDatabase
import com.example.android.navigation.databinding.FragmentImportBinding
import com.example.android.navigation.screens.sign_options.SignOptionsViewModel
import com.example.android.navigation.screens.sign_options.SignOptionsViewModelFactory

class ImportFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentImportBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_import, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SignDatabase.getInstance(application).signDatabaseDao

        val viewModelFactory = ImportViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ImportViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel



        binding.importButton.setOnClickListener { view: View ->
            Log.v("Buttons", "ImportFragment - importButton pressed $view" )
        }
        setHasOptionsMenu(true)
        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }
}