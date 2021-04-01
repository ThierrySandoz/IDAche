package com.example.iuam_idache.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R

class PickerAdapter : RecyclerView.Adapter<PickerAdapter.PickerItemViewHolder>() {

    private val data: ArrayList<String> = ArrayList()
    var callback: Callback? = null
    private val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }
    private var selectedItem: Int? = -1
    private var ctx: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PickerItemViewHolder {

        ctx = parent.context

        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_headache_numberpicker_unit, parent, false)

        itemView.setOnClickListener(clickListener)

        return PickerItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: PickerItemViewHolder,
        position: Int
    ) {
        holder.tvItem?.text = data[position]

        when (selectedItem) {
            position -> {
                holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.colorGreenLight))
                selectedItem = -1
            }
            else -> holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.colorDarkGray))
        }
    }

    fun setSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }

    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    class PickerItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tvItem: TextView? = itemView?.findViewById(R.id.tv_item)
    }
}