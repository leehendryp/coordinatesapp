package com.leehendryp.coordinatesapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leehendryp.coordinatesapp.domain.Coordinates
import com.leehendryp.coordinatesapp.databinding.CoordinatesListItemBinding

class CoordinatesListAdapter :
    ListAdapter<Coordinates, CoordinatesListAdapter.ViewHolder>(CoordinateListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CoordinatesListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: CoordinatesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Coordinates) {
            binding.coordinates = item
            binding.executePendingBindings()
        }
    }

    class CoordinateListDiffCallback : DiffUtil.ItemCallback<Coordinates>() {
        override fun areItemsTheSame(oldItem: Coordinates, newItem: Coordinates): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Coordinates, newItem: Coordinates): Boolean {
            return oldItem == newItem
        }
    }
}