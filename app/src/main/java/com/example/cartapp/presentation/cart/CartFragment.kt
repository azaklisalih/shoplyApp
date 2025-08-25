package com.example.cartapp.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartapp.R
import com.example.cartapp.databinding.FragmentCartBinding
import com.example.cartapp.presentation.ui_state.CartUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartItemAdapter
    private lateinit var shimmerAdapter: CartShimmerAdapter

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
        binding.customAppBar.tvTitle.text = getString(R.string.cart_title)
        
        binding.customAppBar.btnBack.visibility = View.GONE
        
        binding.customAppBar.btnRightAction.apply {
            setImageResource(R.drawable.ic_remove)
            visibility = View.VISIBLE
            setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            
            setOnClickListener {
                showClearAllConfirmationDialog()
            }
        }
        
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
                val productIdInt = productId.toIntOrNull()
                if (productIdInt != null) {
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToProductDetailFragment(productIdInt)
                    )
                }
            }
        )
        
        shimmerAdapter = CartShimmerAdapter()
        
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
    
    private fun showClearAllConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.cart_clear_all))
            .setMessage(getString(R.string.cart_clear_all_confirmation))
            .setPositiveButton(getString(R.string.common_yes)) { _, _ ->
                viewModel.clearCart()
            }
            .setNegativeButton(getString(R.string.common_cancel), null)
            .show()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    _binding?.let { binding ->
                        if (uiState.isLoading) {
                            binding.rvCartItems.visibility = View.GONE
                        } else if (uiState.error != null) {
                            binding.rvCartItems.visibility = View.GONE
                        } else {
                            updateCartUI(uiState, binding)
                        }
                    }
                }
            }
        }
    }

    private fun updateCartUI(uiState: CartUIState, binding: FragmentCartBinding) {
        binding.tvTotal.text = getString(R.string.currency_format, uiState.totalPrice)
        
        if (uiState.isLoading) {
            binding.rvCartItems.adapter = shimmerAdapter
            shimmerAdapter.setShimmerCount(5)
        } else if (uiState.cartItems.isEmpty()) {
            binding.rvCartItems.visibility = View.GONE
            binding.btnComplete.isEnabled = false
        } else {
            binding.rvCartItems.visibility = View.VISIBLE
            binding.rvCartItems.adapter = adapter
            binding.btnComplete.isEnabled = true
            adapter.submitList(uiState.cartItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
