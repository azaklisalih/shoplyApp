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
import com.example.cartapp.presentation.ui_state.HomeUIState

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: ProductListAdapter

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
    }

    private fun setupCustomAppBar() {
        binding.customAppBar.tvTitle.text = "E-Market"
        
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

    private fun setAdapters() {
        adapter = ProductListAdapter(
            onItemClick = { product ->
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product.id.toInt()))
            },
            onAddToCart = { product ->
                viewModel.addToCart(product)
            },
            onToggleFavorite = { product ->
                viewModel.toggleFavorite(product)
            },
            onLoadMore = {
                viewModel.loadMoreProducts()
            }
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
        binding.etSearch.doAfterTextChanged {
            if (!binding.etSearch.hasFocus()) return@doAfterTextChanged
            viewModel.searchProducts(it.toString())
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

        binding.btnRetry.setOnClickListener {
            viewModel.fetchProducts()
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
                    } else {
                        adapter.setLoading(false)
                        adapter.setLoadingMore(state.isLoadingMore)
                        adapter.submitProducts(state.products)
                        adapter.updateFavoriteStates(state.favoriteStates)
                        adapter.updateAnimationStates(state.animatedCartProductId, state.animatedFavoriteProductId)
                        binding.errorLayout.visibility = View.GONE
                        binding.rvProducts.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}