package com.example.iuam_idache.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.SymptomSelectorAdapter
import com.example.iuam_idache.classes.HeadachePages
import com.example.iuam_idache.classes.SymptomSelectorUI
import java.util.*


class SelectSymptomsFragment : Fragment() {

    //-------------- Adapters
    private lateinit var symptomSelectorAdapter: SymptomSelectorAdapter
    private lateinit var symptomSelectorRecyclerView: RecyclerView

    //-------------- SelectorList
    private lateinit var selectorList : MutableList<SymptomSelectorUI>

    //-------------- Variables
    private val nbColumns : Int = 3

    //-------------- SymptomList
    private lateinit var symptomList: MutableList<HeadachePages>

    //-------------- SharedPreferences
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var lastSelectedPages : String
    private val headachePagesKey : String = "headache_pages"
    private lateinit var stringTokenizer: StringTokenizer
    private var stringBuilder = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //-------------------------- SharedPreferences ---------------------------
        sharedPreferences = this.requireActivity().getSharedPreferences(headachePagesKey, Context.MODE_PRIVATE)

        // Get back the last selected values
        lastSelectedPages = sharedPreferences.getString("headachePages", "").toString()
        stringTokenizer = StringTokenizer(lastSelectedPages, ",")

        symptomList = mutableListOf()

        val nbTokens = stringTokenizer.countTokens()
        if (nbTokens != 0) {
            for (i in 1..nbTokens) {
                symptomList.add(HeadachePages.values()[stringTokenizer.nextToken().toInt()])
            }
        }

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_select_symptoms, container, false)

        // Populate the mutable list
        selectorList = mutableListOf(
            SymptomSelectorUI(
                getString(R.string.coffee),
                R.drawable.ic_coffee,
                R.drawable.ic_coffee_blank,
                symptomList.contains(HeadachePages.COFFEE),
                HeadachePages.COFFEE
            ),
            SymptomSelectorUI(
                getString(R.string.cigarettes),
                R.drawable.ic_cigarette,
                R.drawable.ic_cigarette_blank,
                symptomList.contains(HeadachePages.CIGARETTE),
                HeadachePages.CIGARETTE
            ),
            SymptomSelectorUI(
                getString(R.string.alcohol),
                R.drawable.ic_bier,
                R.drawable.ic_bier_blank,
                symptomList.contains(HeadachePages.ALCOHOL),
                HeadachePages.ALCOHOL
            ),
            SymptomSelectorUI(
                getString(R.string.dizziness),
                R.drawable.ic_dizziness,
                R.drawable.ic_dizziness_blank,
                symptomList.contains(HeadachePages.DIZZINESS),
                HeadachePages.DIZZINESS
            ),
            SymptomSelectorUI(
                getString(R.string.temperature),
                R.drawable.ic_high_temperature,
                R.drawable.ic_high_temperature_blank,
                symptomList.contains(HeadachePages.TEMPERATURE),
                HeadachePages.TEMPERATURE
            ),
            SymptomSelectorUI(
                getString(R.string.toothache),
                R.drawable.ic_toothache,
                R.drawable.ic_toothache_blank,
                symptomList.contains(HeadachePages.TOOTHACHE),
                HeadachePages.TOOTHACHE
            )
        )

        // Set up the recyclerView
        symptomSelectorRecyclerView = view.findViewById(R.id.fragment_headache_select_symptom_recyclerView)
        symptomSelectorRecyclerView.setHasFixedSize(true)
        symptomSelectorRecyclerView.layoutManager = GridLayoutManager(
            context,
            nbColumns
        )

        // Set up the adapter
        symptomSelectorAdapter = SymptomSelectorAdapter(context, selectorList) { selector ->
            // Inverse selector state
            selector.selected = !selector.selected
            symptomSelectorAdapter.notifyDataSetChanged()

            if (selector.selected) {
                symptomList.add(selector.page)
            }
            else {
                symptomList.remove(selector.page)
            }
        }
        symptomSelectorRecyclerView.adapter = symptomSelectorAdapter

        // Return the fragment view/layout
        return view
    }

    override fun onPause() {
        super.onPause()

        // Convert the list of headachePages (int) into list of string
        for (i in 0 until symptomList.size) {
            stringBuilder.append(symptomList[i].ordinal).append(",")
        }

        // Store the headachePages into sharedPreferences
        editor = sharedPreferences.edit()
        editor.putString("headachePages", stringBuilder.toString())

        // Apply the changes
        editor.apply()

    }
}