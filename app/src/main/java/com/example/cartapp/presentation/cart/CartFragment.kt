package com.example.cartapp.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartapp.databinding.FragmentCartBinding
import com.example.cartapp.presentation.cart.CartUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomAppBar()
        setupRecyclerView()
        setupListeners()
        setupObservers()
        setupWindowInsets()
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

    private fun setupCustomAppBar() {
        // Set title
        binding.customAppBar.tvTitle.text = "Cart"
        
        // Hide back button
        binding.customAppBar.btnBack.visibility = View.GONE
        
        // Hide custom content area since we're not using it
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        adapter = CartItemAdapter(
            onQuantityChanged = { productId, newQuantity ->
                viewModel.updateQuantity(productId, newQuantity)
            },
            onRemoveItem = { productId ->
                viewModel.removeFromCart(productId)
            },
            onItemClick = { productId ->
                // Navigate to product detail
                findNavController().navigate(
                    CartFragmentDirections.actionCartFragmentToProductDetailFragment(
                        productId.toInt()
                    )
                )
            }
        )
        
        binding.rvCartItems.adapter = adapter
        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        binding.btnComplete.setOnClickListener {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToCheckoutFragment()
            )
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                _binding?.let { binding ->
                    if (uiState.isLoading) {
                        // Show loading state
                        binding.rvCartItems.visibility = View.GONE
                    } else if (uiState.error != null) {
                        // Show error state
                        binding.rvCartItems.visibility = View.GONE
                    } else {
                        // Update cart items
                        updateCartUI(uiState, binding)
                    }
                }
            }
        }
    }

    private fun updateCartUI(uiState: CartUIState, binding: FragmentCartBinding) {
        binding.tvTotal.text = "${uiState.totalPrice} ₺"
        
        if (uiState.cartItems.isEmpty()) {
            // Show empty cart state
            binding.rvCartItems.visibility = View.GONE
            binding.btnComplete.isEnabled = false
        } else {
            // Show cart items
            binding.rvCartItems.visibility = View.VISIBLE
            binding.btnComplete.isEnabled = true
            adapter.submitList(uiState.cartItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
