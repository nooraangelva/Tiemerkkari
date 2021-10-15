package com.example.android.navigation.screens.sign_options

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.R
import com.example.android.navigation.databinding.FragmentSignOptionsBinding

class SignOptionsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignOptionsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_options, container, false)


        // Get data

        val args = SignOptionsFragmentArgs.fromBundle(requireArguments())
        val area = args.area
        val type = args.type

        // SetS the onClickListener for buttons

        binding.optionOneButton.setOnClickListener { view: View ->

            Log.v("Buttons","Sign options - option 1 pressed")

        }

        // ADDS SIDE MENU

        setHasOptionsMenu(true)

        return binding.root
    }

    // MENU FUNCTIONS

    private fun getShareIntent() : Intent {
        //val args = Si.fromBundle(requireArguments())
        return ShareCompat.IntentBuilder.from(activity!!)
                .setText("")
                .setType("text/plain")
                .intent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}