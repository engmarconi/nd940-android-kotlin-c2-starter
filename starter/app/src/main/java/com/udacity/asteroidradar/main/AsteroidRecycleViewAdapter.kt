package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemAstroidViewBinding


class AsteroidRecycleViewAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
        holder.itemView.setOnClickListener {
             onClickListener.onClick(asteroid)
        }
    }
}

class AsteroidViewHolder private constructor(private val binding: ItemAstroidViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(asteroid: Asteroid) {
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAstroidViewBinding.inflate(inflater, parent, false)
            return AsteroidViewHolder(binding)
        }
    }
}

class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}
