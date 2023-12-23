package com.zambo.locationdiary

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zambo.locationdiary.databinding.ItemLayoutLocationBinding

class HistoryAdapter(private val originalList: List<AddLocationData>, private val activity: Activity) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var filteredList: List<AddLocationData> = originalList


    class ViewHolder(val binding: ItemLayoutLocationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.binding.titleTextView.text = currentItem.locationName
        holder.binding.subtitleTextView.text = currentItem.address
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}