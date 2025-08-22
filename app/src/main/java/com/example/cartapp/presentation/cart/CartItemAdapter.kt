package com.example.cartapp.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.databinding.ItemCartBinding
import com.example.cartapp.domain.model.CartItem

class CartItemAdapter(
    private val onQuantityChanged: (String, Int) -> Unit,
    private val onRemoveItem: (String) -> Unit,
    private val onItemClick: (String) -> Unit
) : ListAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartItemViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.cartItem = cartItem
            binding.executePendingBindings()

            // Set up quantity controls
            binding.btnMinus.setOnClickListener {
                val newQuantity = cartItem.quantity - 1
                onQuantityChanged(cartItem.productId, newQuantity)
            }

            binding.btnPlus.setOnClickListener {
                val newQuantity = cartItem.quantity + 1
                onQuantityChanged(cartItem.productId, newQuantity)
            }

            // Set up item click for navigation
            binding.root.setOnClickListener {
                onItemClick(cartItem.productId)
            }

            // Set up remove button (long press to remove)
            binding.root.setOnLongClickListener {
                onRemoveItem(cartItem.productId)
                true
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }
} 