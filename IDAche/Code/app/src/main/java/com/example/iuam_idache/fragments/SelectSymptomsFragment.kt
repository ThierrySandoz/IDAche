package com.example.iuam_idache.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.SymptomSelectorAdapter
import com.example.iuam_idache.classes.SymptomSelectorUI


class SelectSymptomsFragment : Fragment() {

    //-------------- Adapters
    private lateinit var symptomSelectorAdapter: SymptomSelectorAdapter
    private lateinit var symptomSelectorRecyclerView: RecyclerView

    //-------------- SelectorList
    private lateinit var selectorList : MutableList<SymptomSelectorUI>

    //-------------- Variables
    private val nbColumns : Int = 3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_select_symptoms,container,false)

        // Populate the mutable list
        selectorList = mutableListOf(
            SymptomSelectorUI("coffee", R.drawable.ic_coffee, R.drawable.ic_coffee_blank, false),
            SymptomSelectorUI("cigarettes", R.drawable.ic_cigarette, R.drawable.ic_cigarette_blank,false),
            SymptomSelectorUI("alcohol",  R.drawable.ic_bier, R.drawable.ic_bier_blank,false),
            SymptomSelectorUI("dizziness", R.drawable.ic_dizziness, R.drawable.ic_dizziness_blank,false),
            SymptomSelectorUI("temperature", R.drawable.ic_high_temperature, R.drawable.ic_high_temperature_blank, false),
            SymptomSelectorUI("toothache", R.drawable.ic_toothache, R.drawable.ic_toothache_blank, false)
        )

        // Set up the recyclerView
        symptomSelectorRecyclerView = view.findViewById(R.id.fragment_headache_select_symptom_recyclerView)
        symptomSelectorRecyclerView.setHasFixedSize(true )
        symptomSelectorRecyclerView.layoutManager = GridLayoutManager(
            context,
            nbColumns
        )

        // Set up the adapter
        symptomSelectorAdapter = SymptomSelectorAdapter(context, selectorList) { selector ->
            // Inverse selector state
            selector.selected = !selector.selected
            symptomSelectorAdapter.notifyDataSetChanged()
        }
        symptomSelectorRecyclerView.adapter = symptomSelectorAdapter

        // Return the fragment view/layout
        return view
    }
}