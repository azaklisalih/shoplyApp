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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartapp.R
import com.example.cartapp.databinding.FragmentFavoriteBinding
import com.example.cartapp.presentation.ui_state.FavoriteUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteItemAdapter
    private lateinit var shimmerAdapter: FavoriteShimmerAdapter

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
        binding.customAppBar.tvTitle.text = getString(R.string.favorite_title)
        
        binding.customAppBar.btnBack.visibility = View.GONE
        
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        adapter = FavoriteItemAdapter(
            onItemClick = { favorite ->
                findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailFragment(
                        favorite.productId.toInt()
                    )
                )
            },
            onAddToCart = { favorite ->
                viewModel.addToCart(favorite)
            },
            onRemoveFavorite = { productId ->
                viewModel.removeFromFavorites(productId)
            }
        )
        
        shimmerAdapter = FavoriteShimmerAdapter()
        
        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    _binding?.let { binding ->
                        if (uiState.isLoading) {
                            binding.rvFavorites.adapter = shimmerAdapter
                            shimmerAdapter.setShimmerCount(6)
                            binding.rvFavorites.visibility = View.VISIBLE
                        } else if (uiState.error != null) {
                            binding.rvFavorites.visibility = View.GONE
                        } else {
                            updateFavoritesList(uiState.favorites, uiState, binding)
                        }
                    }
                }
            }
        }
    }

    private fun updateFavoritesList(favorites: List<com.example.cartapp.domain.model.Favorite>, uiState: FavoriteUIState, binding: FragmentFavoriteBinding) {
        try {
            if (favorites.isEmpty()) {
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.rvFavorites.visibility = View.VISIBLE
                binding.rvFavorites.adapter = adapter
                adapter.submitList(favorites)
                adapter.updateAnimationState(uiState.animatedCartProductId)
            }
        } catch (e: Exception) {
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