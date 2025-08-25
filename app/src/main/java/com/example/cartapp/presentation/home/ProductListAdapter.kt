package com.example.cartapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.R
import com.example.cartapp.databinding.ItemProductBinding
import com.example.cartapp.databinding.ItemProductShimmerBinding
import com.example.cartapp.domain.model.Product

class ProductListAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit,
    private val onToggleFavorite: (Product) -> Unit,
) : ListAdapter<Any, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var isLoading = false
    private var isLoadingMore = false
    private var favoriteStates: Map<String, Boolean> = emptyMap()
    private var animatedCartProductId: String? = null
    private var animatedFavoriteProductId: String? = null

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
    
    fun updateAnimationStates(cartProductId: String?, favoriteProductId: String?) {
        animatedCartProductId = cartProductId
        animatedFavoriteProductId = favoriteProductId
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, isFavorite: Boolean = false) {
            binding.product = product
            binding.root.setOnClickListener { onItemClick(product) }
            
            binding.btnAddToCart.setOnClickListener {
                onAddToCart(product)
            }
            
            binding.ivFavorite.setOnClickListener {
                onToggleFavorite(product)
            }
            
            if (isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_star_outline)
            }
            
            updateCartButtonAnimation(product.id)
            updateFavoriteButtonAnimation(product.id)
            
            binding.executePendingBindings()
        }
        
        private fun updateCartButtonAnimation(productId: String) {
            if (animatedCartProductId == productId) {
                binding.btnAddToCart.text = binding.root.context.getString(R.string.common_added_to_cart)
                binding.btnAddToCart.setBackgroundColor(android.graphics.Color.GREEN)
                binding.btnAddToCart.setTextColor(android.graphics.Color.WHITE)
                
                binding.btnAddToCart.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(300)
                    .withEndAction {
                        binding.btnAddToCart.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(300)
                            .start()
                    }
                    .start()
            } else {
                binding.btnAddToCart.text = binding.root.context.getString(R.string.common_add_to_cart)
                binding.btnAddToCart.setBackgroundResource(R.drawable.add_to_cart_button)
                binding.btnAddToCart.setTextColor(android.graphics.Color.WHITE)
            }
        }
        
        private fun updateFavoriteButtonAnimation(productId: String) {
            if (animatedFavoriteProductId == productId) {
                binding.ivFavorite.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(300)
                    .withEndAction {
                        binding.ivFavorite.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(300)
                            .start()
                    }
                    .start()
            }
        }
    }

    inner class ShimmerViewHolder(
        binding: ItemProductShimmerBinding
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
