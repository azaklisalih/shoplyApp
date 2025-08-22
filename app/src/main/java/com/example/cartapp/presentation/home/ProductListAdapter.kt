package com.example.cartapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.databinding.ItemProductBinding
import com.example.cartapp.databinding.ItemProductShimmerBinding
import com.example.cartapp.domain.model.Product

class ProductListAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit,
    private val onToggleFavorite: (Product) -> Unit,
    private val onLoadMore: () -> Unit = {}
) : ListAdapter<Any, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var isLoading = false
    private var isLoadingMore = false
    private var favoriteStates: Map<String, Boolean> = emptyMap()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return if (item is Product) item.id.toLong() else position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Product) VIEW_TYPE_PRODUCT else VIEW_TYPE_SHIMMER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PRODUCT -> {
                val binding = ItemProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ProductViewHolder(binding)
            }
            VIEW_TYPE_SHIMMER -> {
                val binding = ItemProductShimmerBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ShimmerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                val product = getItem(position) as Product
                val isFavorite = favoriteStates[product.id] ?: false
                holder.bind(product, isFavorite)
            }
            is ShimmerViewHolder -> {
                // Shimmer effect is handled by the layout
            }
        }
    }

    fun setLoading(loading: Boolean) {
        if (isLoading != loading) {
            isLoading = loading
            if (loading) {
                val shimmerItems = List(10) { ShimmerItem() }
                submitList(shimmerItems)
            }
        }
    }
    
    fun setLoadingMore(loadingMore: Boolean) {
        isLoadingMore = loadingMore
    }

    fun submitProducts(products: List<Product>) {
        if (!isLoading) {
            submitList(products)
        }
    }
    
    fun updateFavoriteStates(states: Map<String, Boolean>) {
        favoriteStates = states
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, isFavorite: Boolean = false) {
            binding.product = product
            binding.root.setOnClickListener { onItemClick(product) }
            
            // Add to Cart button click listener
            binding.btnAddToCart.setOnClickListener { 
                onAddToCart(product)
            }
            
            // Favorite button click listener
            binding.ivFavorite.setOnClickListener {
                onToggleFavorite(product)
            }
            
            // Update favorite icon based on status
            if (isFavorite) {
                binding.ivFavorite.setImageResource(com.example.cartapp.R.drawable.ic_star_filled)
            } else {
                binding.ivFavorite.setImageResource(com.example.cartapp.R.drawable.ic_star_outline)
            }
            
            binding.executePendingBindings()
        }
    }

    inner class ShimmerViewHolder(
        private val binding: ItemProductShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private data class ShimmerItem(val id: Int = 0)

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_SHIMMER = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(old: Any, new: Any): Boolean {
                return when {
                    old is Product && new is Product -> old.id == new.id
                    old is ShimmerItem && new is ShimmerItem -> old.id == new.id
                    else -> false
                }
            }

            override fun areContentsTheSame(old: Any, new: Any): Boolean {
                return when {
                    old is Product && new is Product -> old.id == new.id
                    old is ShimmerItem && new is ShimmerItem -> old.id == new.id
                    else -> false
                }
            }
        }
    }
}
