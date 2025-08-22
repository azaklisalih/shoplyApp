package com.example.cartapp.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cartapp.databinding.FragmentFilterBinding
import com.example.cartapp.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import com.example.cartapp.R
import androidx.core.view.WindowCompat
import com.example.cartapp.MainActivity

@AndroidEntryPoint
class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        binding.vm = filterViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        setupCustomAppBar()
        setupListeners()
        setupObservers()
        setupWindowInsets()
        
        // Hide bottom navigation when filter modal opens
        (requireActivity() as MainActivity).hideBottomNavigation()
    }

    private fun setupWindowInsets() {
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            
            binding.root.setPadding(
                binding.root.paddingLeft,
                insets.top,
                binding.root.paddingRight,
                binding.root.paddingBottom
            )
            
            androidx.core.view.WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupStatusBar() {
        // Set status bar color for Filter fragment
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary_blue)
        
        // Set status bar icons to light (white) for dark background
        val windowInsetsController = WindowCompat.getInsetsController(
            requireActivity().window, 
            requireActivity().window.decorView
        )
        windowInsetsController.isAppearanceLightStatusBars = false
    }

    private fun setupCustomAppBar() {
        // Set title
        binding.customAppBar.tvTitle.text = "Filter"
        
        // Show close button (X) instead of back button
        binding.customAppBar.btnBack.visibility = View.VISIBLE
        binding.customAppBar.btnBack.setImageResource(R.drawable.ic_close)
        binding.customAppBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        // Hide custom content area since we're not using it
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupListeners() {
        // Apply button
        binding.btnApply.setOnClickListener {
            applyFilters()
        }

        // Brand search
        binding.etBrandSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                filterViewModel.updateBrandSearch(s?.toString() ?: "")
            }
        })

        // Model search
        binding.etModelSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                filterViewModel.updateModelSearch(s?.toString() ?: "")
            }
        })

        // Sort options
        binding.rbOldToNew.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filterViewModel.setSorting("createdAt", "asc")
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbNewToOld.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filterViewModel.setSorting("createdAt", "desc")
                binding.rbOldToNew.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbPriceHighToLow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filterViewModel.setSorting("price", "desc")
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbPriceLowToHigh.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filterViewModel.setSorting("price", "asc")
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            filterViewModel.uiState.collect { uiState ->
                _binding?.let { binding ->
                    if (uiState.isLoading) {
                        // Show loading state
                        binding.progressBar.visibility = View.VISIBLE
                    } else if (uiState.error != null) {
                        // Show error state
                        binding.progressBar.visibility = View.GONE
                        // TODO: Show error message
                    } else {
                        // Update UI with brands and models
                        binding.progressBar.visibility = View.GONE
                        updateBrandsUI(uiState.filteredBrands, uiState.selectedBrands)
                        updateModelsUI(uiState.filteredModels, uiState.selectedModels)
                    }
                }
            }
        }
    }

    private fun updateBrandsUI(brands: List<String>, selectedBrands: Set<String>) {
        // Clear existing brand checkboxes
        binding.brandContainer.removeAllViews()
        
        // Add dynamic brand checkboxes
        brands.forEach { brand ->
            val checkBox = android.widget.CheckBox(requireContext()).apply {
                text = brand
                isChecked = selectedBrands.contains(brand)
                setOnCheckedChangeListener { _, isChecked ->
                    filterViewModel.toggleBrand(brand)
                }
            }
            binding.brandContainer.addView(checkBox)
        }
    }

    private fun updateModelsUI(models: List<String>, selectedModels: Set<String>) {
        // Clear existing model checkboxes
        binding.modelContainer.removeAllViews()
        
        // Add dynamic model checkboxes
        models.forEach { model ->
            val checkBox = android.widget.CheckBox(requireContext()).apply {
                text = model
                isChecked = selectedModels.contains(model)
                setOnCheckedChangeListener { _, isChecked ->
                    filterViewModel.toggleModel(model)
                }
            }
            binding.modelContainer.addView(checkBox)
        }
    }

    private fun applyFilters() {
        val uiState = filterViewModel.uiState.value
        
        // Apply filters to HomeViewModel using API filtering
        homeViewModel.applyFilters(uiState.selectedSortBy, uiState.selectedOrder)
        homeViewModel.applyBrandFilter(uiState.selectedBrands)
        homeViewModel.applyModelFilter(uiState.selectedModels)
        
        // Navigate back
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        // Show bottom navigation when filter modal closes
        (requireActivity() as MainActivity).showBottomNavigation()
        
        _binding = null
    }
} 