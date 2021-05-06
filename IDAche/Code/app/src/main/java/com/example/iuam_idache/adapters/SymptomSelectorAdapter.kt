package com.example.iuam_idache.adapters

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.SymptomSelectorUI
import com.mikhaellopez.circularimageview.CircularImageView

class SymptomSelectorAdapter(
    private val context: Context?,
    private val selectorList: MutableList<SymptomSelectorUI>,
    private val listener: (SymptomSelectorUI) -> Unit
) : RecyclerView.Adapter<SymptomSelectorAdapter.SelectorViewHolder>() {

    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_headache_select_symptoms_unit,
            parent,
            false
        )

        return SelectorViewHolder(view)
    }

    //------------------------------------------
    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {

        // Get the actual selector
        val selector = selectorList[position]

        // Set description
        holder.textViewSelectorDescription.text = selector.description

        // Set image
        if(selector.selected) {
            holder.imageViewSelectorImage.borderWidth = 300f
            holder.imageViewSelectorImage.foreground = ContextCompat.getDrawable(context!!, selector.imagePressedID)
        }
        else {
            holder.imageViewSelectorImage.borderWidth = 8f
            holder.imageViewSelectorImage.foreground = null
            holder.imageViewSelectorImage.setImageResource(selector.imageID)
        }

        // Set on item click listener
        holder.imageViewSelectorImage.setOnClickListener {
            listener(selector)
        }
    }

    //----------------------------------------------------------------------------------------------
    override fun getItemCount(): Int = selectorList.size

    //----------------------------------------------------------------------------------------------
    inner class SelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewSelectorDescription = view.findViewById<TextView>(R.id.fragment_headache_select_symptom_description)!!
        val imageViewSelectorImage = view.findViewById<CircularImageView>(R.id.fragment_headache_select_symptom_image)!!
    }
}