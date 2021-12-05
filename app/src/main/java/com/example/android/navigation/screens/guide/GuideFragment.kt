package com.example.android.navigation.screens.guide

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentGuideBinding


class GuideFragment : Fragment() {

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        val binding: FragmentGuideBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_guide, container, false
        )

       // Sets title for fragment
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.titleGuide)


       // BUTTONS ----------------------------------------------------------------------------------

       binding.importLayoutButton.setOnClickListener{

           binding.importInstructions.isVisible = true
           binding.importInstructions.isVisible = true
           // TODO kumpi?

       }

       binding.Ohje1.setOnClickListener{

           binding.importInstructions.isVisible = false

       }

       binding.guideButton.setOnClickListener{

           binding.importInstructions.isVisible = false

       }

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

