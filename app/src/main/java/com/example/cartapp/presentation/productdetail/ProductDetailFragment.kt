package com.example.cartapp.presentation.productdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cartapp.databinding.FragmentProductDetailBinding
import com.example.cartapp.presentation.ui_state.ProductDetailUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.cartapp.R

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomAppBar()
        setupListeners()
        setupObservers()
        setupWindowInsets()
        loadProduct()
    }

    private fun setupCustomAppBar() {
        binding.customAppBar.tvTitle.text = "Product Detail"
        
        binding.customAppBar.btnBack.visibility = View.VISIBLE
        binding.customAppBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.customAppBar.btnRightAction.visibility = View.GONE
        
        binding.customAppBar.customContentArea.visibility = View.GONE
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

    private fun setupListeners() {
        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart()
        }

        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                _binding?.let { binding ->
                    if (uiState.isLoading) {
                        binding.productImageCard.visibility = View.GONE
                        binding.lyProductInfo.visibility = View.GONE
                        binding.lyPriceAndAddToCart.visibility = View.GONE
                    } else if (uiState.error != null) {
                        binding.productImageCard.visibility = View.GONE
                        binding.lyProductInfo.visibility = View.GONE
                        binding.lyPriceAndAddToCart.visibility = View.GONE
                    } else {
                        uiState.product?.let { product ->
                            updateProductUI(product, binding)
                        }
                        
                        updateFavoriteUI(uiState.isFavorite, binding)
                    }
                }
            }
        }
    }

    private fun loadProduct() {
        viewModel.loadProduct(args.productId.toString())
    }

    private fun updateProductUI(product: com.example.cartapp.domain.model.Product, binding: FragmentProductDetailBinding) {
        binding.customAppBar.tvTitle.text = product.name
        
        binding.tvProductName.text = product.name
        binding.tvProductDescription.text = product.description
        
        binding.tvPrice.text = "${product.price} ₺"
        
        binding.productImageCard.visibility = View.VISIBLE
        binding.lyProductInfo.visibility = View.VISIBLE
        binding.lyPriceAndAddToCart.visibility = View.VISIBLE
    }

    private fun updateFavoriteUI(isFavorite: Boolean, binding: FragmentProductDetailBinding) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_star_filled)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_star_outline)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}