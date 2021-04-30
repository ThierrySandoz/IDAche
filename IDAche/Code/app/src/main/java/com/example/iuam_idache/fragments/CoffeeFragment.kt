package com.example.iuam_idache.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.NumberPickerSharedViewModel
import java.nio.channels.Selector

class CoffeeFragment : Fragment() {

    //-------------- TextViews
    private lateinit var coffeeDescription: TextView

    //-------------- ImageViews
    private lateinit var coffeeImage : ImageView

    //-------------- itemSelector
    private lateinit var itemSelector : Selector
    private lateinit var model : NumberPickerSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_coffee, container, false)

        //----------------- ImageViews
        coffeeImage = view.findViewById(R.id.headache_coffee_imageView)

        //----------------- TextViews
        coffeeDescription = view.findViewById(R.id.headache_coffee_description_textView)

        //----------------- Shared view model
        model = ViewModelProviders.of(requireActivity()).get(NumberPickerSharedViewModel::class.java)

        model.selectedItem.observe(viewLifecycleOwner, { t ->
            when(t) {
                1 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_1)
                    coffeeDescription.text = "Few : 1 coffee"
                }
                2 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_2)
                    coffeeDescription.text = "Few : 2 coffees"
                }
                3 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_3)
                    coffeeDescription.text = "Medium : 3 coffees"
                }
                4 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_4)
                    coffeeDescription.text = "Medium : 4 coffees"
                }
                5 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_5)
                    coffeeDescription.text = "Medium : 5 coffees"
                }
                6 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_6)
                    coffeeDescription.text = "A lot : 6 coffees"
                }
                7 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_7)
                    coffeeDescription.text = "A lot : 7 coffees"
                }
                8 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_8)
                    coffeeDescription.text = "A lot : 8 coffees"
                }
                9 -> {
                    coffeeImage.setImageResource(R.drawable.ic_coffee_animation_9)
                    coffeeDescription.text = "A lot : More than 9 coffees"
                }
                else -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_5)
            }
        })

        // Return the fragment view/layout
        return view
    }
}