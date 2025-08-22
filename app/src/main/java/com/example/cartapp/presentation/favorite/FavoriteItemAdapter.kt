package com.example.cartapp.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.databinding.ItemProductBinding
import com.example.cartapp.domain.model.Favorite

class FavoriteItemAdapter(
    private val onItemClick: (Favorite) -> Unit,
    private val onAddToCart: (Favorite) -> Unit,
    private val onRemoveFavorite: (String) -> Unit
) : ListAdapter<Favorite, FavoriteItemAdapter.FavoriteItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteItemViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteItemViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: Favorite) {
            // Convert Favorite to Product for data binding
            val product = com.example.cartapp.domain.model.Product(
                id = favorite.productId,
                name = favorite.name,
                price = favorite.price,
                image = favorite.image,
                description = favorite.description,
                model = favorite.model,
                brand = favorite.brand,
                createdAt = favorite.createdAt
            )
            
            binding.product = product
            binding.executePendingBindings()

            // Set up click listeners
            binding.root.setOnClickListener {
                onItemClick(favorite)
            }

            // Add to Cart button
            binding.btnAddToCart.setOnClickListener {
                onAddToCart(favorite)
            }

            // Favorite button - show filled star and handle remove
            binding.ivFavorite.setImageResource(com.example.cartapp.R.drawable.ic_star_filled)
            binding.ivFavorite.setOnClickListener {
                onRemoveFavorite(favorite.productId)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
} 