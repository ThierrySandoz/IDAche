package com.example.iuam_idache.fragments

import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.PickerAdapter
import com.example.iuam_idache.classes.PickerLayoutManager
import com.example.iuam_idache.classes.ScreenUtils

class CoffeeFragment : Fragment() {

    //-------------- TextViews
    private lateinit var coffeeDescription: TextView

    //-------------- ImageViews
    private lateinit var coffeeImage : ImageView

    //-------------- Bundle
    private lateinit var bundle : Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the view
        val view = inflater.inflate(R.layout.fragment_headache_coffee, container, false)

        //----------------- ImageViews
        coffeeImage = view.findViewById(R.id.headache_coffee_imageView)

        //----------------- TextViews
        coffeeDescription = view.findViewById(R.id.headache_coffee_description_textView)

        //----------------- Bundle
        bundle = Bundle()

        when (bundle.getInt("position_key")) {
            1 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_1)
            2 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_2)
            3 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_3)
            4 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_4)
            5 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_5)
            6 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_6)
            7 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_7)
            8 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_8)
            9 -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_9)
            else -> coffeeImage.setImageResource(R.drawable.ic_coffee_animation_1)
        }

        // Return the fragment view/layout
        return view
    }
}