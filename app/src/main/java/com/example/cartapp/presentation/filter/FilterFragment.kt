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
import com.example.cartapp.presentation.ui_state.HomeUIState
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        binding.vm = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomAppBar()
        setupListeners()
        setupObservers()
        setupWindowInsets()
        
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



    private fun setupCustomAppBar() {
        binding.customAppBar.tvTitle.text = getString(R.string.common_filter)
        
        binding.customAppBar.btnBack.visibility = View.VISIBLE
        binding.customAppBar.btnBack.setImageResource(R.drawable.ic_close)
        binding.customAppBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.btnApply.setOnClickListener {
            applyFilters()
        }

        binding.etBrandSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                homeViewModel.updateBrandSearch(s?.toString() ?: "")
            }
        })

        binding.etModelSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                homeViewModel.updateModelSearch(s?.toString() ?: "")
            }
        })

        setupSortingListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            homeViewModel.uiState.collect { uiState ->
                _binding?.let { binding ->
                    if (uiState.isFilterDataLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else if (uiState.filterError != null) {
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        updateBrandsUI(uiState.filteredBrands, uiState.selectedBrands)
                        updateModelsUI(uiState.filteredModels, uiState.selectedModels)
                        updateSortingUI(uiState.selectedSortBy, uiState.selectedSortOrder)
                    }
                }
            }
        }
    }

    private fun updateBrandsUI(brands: List<String>, selectedBrands: Set<String>) {
        binding.brandContainer.removeAllViews()
        
        brands.forEach { brand ->
            val checkBox = android.widget.CheckBox(requireContext()).apply {
                text = brand
                isChecked = selectedBrands.contains(brand)
                setOnCheckedChangeListener { _, isChecked ->
                    homeViewModel.toggleBrand(brand)
                }
            }
            binding.brandContainer.addView(checkBox)
        }
    }

    private fun updateModelsUI(models: List<String>, selectedModels: Set<String>) {
        binding.modelContainer.removeAllViews()
        
        models.forEach { model ->
            val checkBox = android.widget.CheckBox(requireContext()).apply {
                text = model
                isChecked = selectedModels.contains(model)
                setOnCheckedChangeListener { _, isChecked ->
                    homeViewModel.toggleModel(model)
                }
            }
            binding.modelContainer.addView(checkBox)
        }
    }
    
    private fun updateSortingUI(selectedSortBy: String?, selectedOrder: String?) {
        binding.rbOldToNew.setOnCheckedChangeListener(null)
        binding.rbNewToOld.setOnCheckedChangeListener(null)
        binding.rbPriceHighToLow.setOnCheckedChangeListener(null)
        binding.rbPriceLowToHigh.setOnCheckedChangeListener(null)
        
        when {
            selectedSortBy == "createdAt" && selectedOrder == "asc" -> {
                binding.rbOldToNew.isChecked = true
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
            selectedSortBy == "createdAt" && selectedOrder == "desc" -> {
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = true
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
            selectedSortBy == "price" && selectedOrder == "desc" -> {
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = true
                binding.rbPriceLowToHigh.isChecked = false
            }
            selectedSortBy == "price" && selectedOrder == "asc" -> {
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = true
            }
            else -> {
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }
        
        setupSortingListeners()
    }
    
    private fun setupSortingListeners() {
        binding.rbOldToNew.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                homeViewModel.setSorting("createdAt", "asc")
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbNewToOld.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                homeViewModel.setSorting("createdAt", "desc")
                binding.rbOldToNew.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbPriceHighToLow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                homeViewModel.setSorting("price", "desc")
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceLowToHigh.isChecked = false
            }
        }

        binding.rbPriceLowToHigh.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                homeViewModel.setSorting("price", "asc")
                binding.rbOldToNew.isChecked = false
                binding.rbNewToOld.isChecked = false
                binding.rbPriceHighToLow.isChecked = false
            }
        }
    }

    private fun applyFilters() {
        homeViewModel.applyFilters()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        (requireActivity() as MainActivity).showBottomNavigation()
        
        _binding = null
    }
} 