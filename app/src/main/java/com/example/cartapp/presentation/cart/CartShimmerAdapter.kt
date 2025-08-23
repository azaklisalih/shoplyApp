package com.example.cartapp.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.databinding.ItemCartShimmerBinding

class CartShimmerAdapter : RecyclerView.Adapter<CartShimmerAdapter.ShimmerViewHolder>() {

    private var shimmerCount = 5

    fun setShimmerCount(count: Int) {
        shimmerCount = count
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val binding = ItemCartShimmerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // Shimmer effect is handled by the drawable
    }

    override fun getItemCount(): Int = shimmerCount

    class ShimmerViewHolder(
        val binding: ItemCartShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)
} 