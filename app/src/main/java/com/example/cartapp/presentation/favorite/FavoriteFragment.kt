package com.example.cartapp.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartapp.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomAppBar()
        setupRecyclerView()
        setupObservers()
        setupWindowInsets()
    }

    private fun setupCustomAppBar() {
        // Set title
        binding.customAppBar.tvTitle.text = "Favorites"
        
        // Hide back button
        binding.customAppBar.btnBack.visibility = View.GONE
        
        // Hide custom content area since we're not using it
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        adapter = FavoriteItemAdapter(
            onItemClick = { favorite ->
                // Navigate to product detail
                findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailFragment(
                        favorite.productId.toInt()
                    )
                )
            },
            onAddToCart = { favorite ->
                // Convert Favorite to Product and add to cart
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
                viewModel.addToCart(product)
            },
            onRemoveFavorite = { productId ->
                viewModel.removeFromFavorites(productId)
            }
        )
        
        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    _binding?.let { binding ->
                        if (uiState.isLoading) {
                            // Show loading state
                            binding.rvFavorites.visibility = View.GONE
                        } else if (uiState.error != null) {
                            // Show error state
                            binding.rvFavorites.visibility = View.GONE
                            println("❌ FavoriteFragment error: ${uiState.error}")
                        } else {
                            // Update favorites list
                            updateFavoritesList(uiState.favorites, binding)
                        }
                    }
                }
            }
        }
    }

    private fun updateFavoritesList(favorites: List<com.example.cartapp.domain.model.Favorite>, binding: FragmentFavoriteBinding) {
        try {
            if (favorites.isEmpty()) {
                // Show empty state
                binding.rvFavorites.visibility = View.GONE
            } else {
                // Show favorites list
                binding.rvFavorites.visibility = View.VISIBLE
                adapter.submitList(favorites)
                println("✅ Updated favorites list with ${favorites.size} items")
            }
        } catch (e: Exception) {
            println("❌ Error updating favorites list: ${e.message}")
            binding.rvFavorites.visibility = View.GONE
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            binding.root.setPadding(
                binding.root.paddingLeft,
                insets.top,
                binding.root.paddingRight,
                binding.root.paddingBottom
            )
            
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}