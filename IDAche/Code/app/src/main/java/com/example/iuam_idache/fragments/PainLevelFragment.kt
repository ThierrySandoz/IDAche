package com.example.iuam_idache.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.NumberPickerSharedViewModel
import java.nio.channels.Selector

class PainLevelFragment : Fragment() {

    //-------------- TextViews
    private lateinit var painLevelDescription: TextView

    //-------------- ImageViews
    private lateinit var painLevelImage : ImageView

    //-------------- itemSelector
    private lateinit var itemSelector : Selector
    private lateinit var model : NumberPickerSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_pain_level, container, false)

        //----------------- ImageViews
        painLevelImage = view.findViewById(R.id.headache_painLevel_imageView)

        //----------------- TextViews
        painLevelDescription = view.findViewById(R.id.headache_painLevel_description_textView)

        //----------------- Shared view model
        model = ViewModelProviders.of(requireActivity()).get(NumberPickerSharedViewModel::class.java)

        //TODO create pain level animation
        //TODO modify pain level descriptions
        model.selectedItem.observe(viewLifecycleOwner, { t ->
            when (t) {
                1 -> {
                    painLevelDescription.text = "slight headache"
                }
                2 -> {
                    painLevelDescription.text = "slight headache"
                }
                3 -> {
                    painLevelDescription.text = "slight headache"
                }
                4 -> {
                    painLevelDescription.text = "slight headache"
                }
                5 -> {
                    painLevelDescription.text = "slight headache"
                }
                6 -> {
                    painLevelDescription.text = "slight headache"
                }
                7 -> {
                    painLevelDescription.text = "slight headache"
                }
                8 -> {
                    painLevelDescription.text = "slight headache"
                }
                9 -> {
                    painLevelDescription.text = "strong headache"
                }
            }
        })

        // Return the fragment view/layout
        return view
    }

    fun newInstance(): Fragment? {
        return PainLevelFragment()
    }

}