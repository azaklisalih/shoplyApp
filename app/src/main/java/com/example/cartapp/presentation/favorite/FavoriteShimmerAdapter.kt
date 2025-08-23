package com.example.cartapp.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.databinding.ItemFavoriteShimmerBinding

class FavoriteShimmerAdapter : RecyclerView.Adapter<FavoriteShimmerAdapter.ShimmerViewHolder>() {

    private var shimmerCount = 5

    fun setShimmerCount(count: Int) {
        shimmerCount = count
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val binding = ItemFavoriteShimmerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = shimmerCount

    class ShimmerViewHolder(
        val binding: ItemFavoriteShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)
} 