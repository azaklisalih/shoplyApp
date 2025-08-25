package com.example.cartapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartapp.R
import com.example.cartapp.MainActivity
import com.example.cartapp.databinding.FragmentHomeBinding
import com.example.cartapp.presentation.common.ReselectCallback
import com.example.cartapp.domain.model.ErrorType

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ReselectCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: ProductListAdapter
    
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = FragmentHomeBinding.inflate(inflater, container, false).also {
        _binding = it
        it.vm = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.navController = findNavController()
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomAppBar()
        setAdapters()
        setObservers()
        setListeners()
        setupWindowInsets()
        setupClickOutsideToHideKeyboard()
        
        (activity as? MainActivity)?.setReselectCallback(this)
    }
    
    override fun onReselect() {
        viewModel.refreshHome()
    }

    private fun setupCustomAppBar() {
        binding.customAppBar.tvTitle.text = getString(R.string.app_name)
        
        binding.customAppBar.btnBack.visibility = View.GONE
        
        binding.customAppBar.btnRightAction.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_clear_filter)
            setOnClickListener {
                viewModel.clearFilters()
            }
        }
        
        binding.customAppBar.customContentArea.visibility = View.GONE
    }


    private fun setupClickOutsideToHideKeyboard() {
        binding.root.setOnClickListener {
            hideKeyboard()
        }
        binding.rvProducts.setOnClickListener {
            hideKeyboard()
        }
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val currentFocus = requireActivity().currentFocus
        if (currentFocus != null) {
            val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            currentFocus.clearFocus()
        }
    }

    private fun setupWindowInsets() {
        val initialTop = binding.root.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                initialTop + insets.top,
                v.paddingRight,
                v.paddingBottom
            )
            windowInsets
        }
    }

    private fun setAdapters() {
        adapter = ProductListAdapter(
            onItemClick = { product ->
                val productId = product.id.toIntOrNull()
                if (productId != null) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(productId))
                }
            },
            onAddToCart = { product ->
                viewModel.addToCart(product)
            },
            onToggleFavorite = { product ->
                viewModel.toggleFavorite(product)
            },
        )
        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        
        binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0 &&
                    totalItemCount >= 4) {
                    viewModel.loadMoreProducts()
                }
            }
            
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        (activity as? MainActivity)?.showBottomNavigation()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        (activity as? MainActivity)?.hideBottomNavigation()
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        (activity as? MainActivity)?.hideBottomNavigation()
                    }
                }
            }
        })
    }

    private fun setListeners() {
        binding.etSearch.doAfterTextChanged { text ->
            if (!binding.etSearch.hasFocus()) return@doAfterTextChanged
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(300)
                viewModel.performSearch(text?.toString().orEmpty())
            }
        }

        binding.btnFilter.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_bottom)
                .setExitAnim(R.anim.slide_out_bottom)
                .setPopEnterAnim(R.anim.slide_in_bottom)
                .setPopExitAnim(R.anim.slide_out_bottom)
                .build()
            
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToFilterFragment(),
                navOptions
            )
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isLoading && state.products.isEmpty()) {
                        adapter.setLoading(true)
                        binding.errorLayout.visibility = View.GONE
                        binding.rvProducts.visibility = View.VISIBLE
                    } else if (state.error != null && state.products.isEmpty()) {
                        adapter.setLoading(false)
                        binding.errorLayout.visibility = View.VISIBLE
                        binding.rvProducts.visibility = View.GONE
                        binding.tvError.text = state.error
                        
                        // ErrorType'a göre UI kontrolü
                        when (state.errorType) {
                            is ErrorType.NetworkError, is ErrorType.ServerError -> {
                                binding.btnRetry.visibility = View.VISIBLE
                                binding.btnRetry.setOnClickListener {
                                    viewModel.fetchProducts()
                                }
                            }
                            is ErrorType.FailedAddCart, is ErrorType.FailedToggleFavorite -> {
                                binding.btnRetry.visibility = View.GONE
                                // Toast göster veya başka UI feedback
                            }
                            else -> {
                                binding.btnRetry.visibility = View.GONE
                            }
                        }
                    } else {
                        adapter.setLoading(false)
                        adapter.setLoadingMore(state.isLoadingMore)
                        adapter.submitProducts(state.products)
                        adapter.updateFavoriteStates(state.favoriteStates)
                        adapter.updateAnimationStates(state.animatedCartProductId, state.animatedFavoriteProductId)
                        binding.errorLayout.visibility = View.GONE
                        binding.rvProducts.visibility = View.VISIBLE
                        binding.btnRetry.visibility = View.GONE
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setReselectCallback(null)
        binding.rvProducts.clearOnScrollListeners()
        searchJob?.cancel()
        _binding = null
    }
}