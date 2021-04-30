package com.example.iuam_idache.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.activities.HeadacheActivity
import com.example.iuam_idache.adapters.PickerAdapter
import com.example.iuam_idache.classes.NumberPickerSharedViewModel
import com.example.iuam_idache.classes.PickerLayoutManager
import com.example.iuam_idache.classes.ScreenUtils

class NumberPickerFragment : Fragment() {

    //------------ Activities
    //private lateinit var headacheActivity: HeadacheActivity

    private val data = (1..9).toList().map { it.toString() } as ArrayList<String>
    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var sliderAdapter: PickerAdapter
    private val defaultPosition: Int = 5

    private lateinit var model : NumberPickerSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_numberpicker, container, false)

        // Get the mainActivity
        //headacheActivity = activity as HeadacheActivity

        // Get the shared view model
        model = ViewModelProviders.of(requireActivity()).get(NumberPickerSharedViewModel::class.java)

        // Get the recyclerView
        rvHorizontalPicker = view.findViewById(R.id.rv_horizontal_picker)

        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(requireContext()) / 2 - ScreenUtils.dpToPx(
            requireContext(), 40)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        rvHorizontalPicker.layoutManager = PickerLayoutManager(context).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {

                    //headacheActivity.fragmentCoffee(layoutPosition)

                    sliderAdapter.setSelectedItem(layoutPosition)
                    //Log.d("selected text", data[layoutPosition])
                    //Toast.makeText(context, data[layoutPosition], Toast.LENGTH_SHORT).show()

                    //set the message to share to another fragment
                    model.onItemSelected(layoutPosition+1)

                    val fragment = CoffeeFragment()
                    val fragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_headache_coffee, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
        }

        // Setting Adapter
        sliderAdapter = PickerAdapter()

        // Bind the adapter to the recyclerView
        rvHorizontalPicker.adapter = sliderAdapter.apply {
            setData(data)

            // Set the default number to 5
            setSelectedItem(defaultPosition-1)
            rvHorizontalPicker.scrollToPosition(defaultPosition-1)

            // Callback on item click to move to the right number
            callback = object : PickerAdapter.Callback {
                override fun onItemClicked(view: View) {
                    rvHorizontalPicker.smoothScrollToPosition(
                        rvHorizontalPicker.getChildLayoutPosition(
                            view
                        )
                    )
                }
            }
        }

        // Return the fragment view/layout
        return view
    }
}