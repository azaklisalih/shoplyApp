package com.example.cartapp.presentation.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cartapp.R
import com.example.cartapp.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
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
    }

    private fun setupCustomAppBar() {
        binding.customAppBar.tvTitle.text = getString(R.string.checkout_title)
        
        binding.customAppBar.btnBack.visibility = View.VISIBLE
        binding.customAppBar.btnBack.setImageResource(R.drawable.ic_arrow_back)
        binding.customAppBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.customAppBar.btnRightAction.visibility = View.GONE
        binding.customAppBar.customContentArea.visibility = View.GONE
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

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            
            if (validateInputs(name, email, phone)) {
                viewModel.placeOrder(name, email, phone)
            }
        }
        
        binding.etName.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val name = s.toString()
                if (name.isNotBlank()) {
                    binding.etName.error = null
                }
            }
        })
        
        binding.etEmail.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val email = s.toString()
                if (email.isNotBlank() && isValidEmail(email)) {
                    binding.etEmail.error = null
                }
            }
        })
        
        binding.etPhone.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val phone = s.toString()
                if (phone.isNotBlank() && isValidPhone(phone)) {
                    binding.etPhone.error = null
                }
            }
        })
    }

    private fun validateInputs(name: String, email: String, phone: String): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            binding.etName.error = getString(R.string.checkout_name_required)
            isValid = false
        } else {
            binding.etName.error = null
        }
        
        if (email.isBlank()) {
            binding.etEmail.error = getString(R.string.checkout_email_required)
            isValid = false
        } else if (!isValidEmail(email)) {
            binding.etEmail.error = getString(R.string.checkout_email_invalid)
            isValid = false
        } else {
            binding.etEmail.error = null
        }
        
        if (phone.isBlank()) {
            binding.etPhone.error = getString(R.string.checkout_phone_required)
            isValid = false
        } else if (!isValidPhone(phone)) {
            binding.etPhone.error = getString(R.string.checkout_phone_invalid)
            isValid = false
        } else {
            binding.etPhone.error = null
        }
        
        return isValid
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }
    
    private fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        
        return when {
            cleanPhone.length == 10 && cleanPhone.startsWith("5") -> true
            cleanPhone.length == 10 && cleanPhone.startsWith("2") -> true
            cleanPhone.length == 11 && cleanPhone.startsWith("05") -> true
            cleanPhone.length == 11 && cleanPhone.startsWith("02") -> true
            else -> false
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.tvTotal.text = getString(R.string.currency_format, uiState.cartTotal)
                    
                    if (uiState.isLoading) {
                        binding.btnConfirm.isEnabled = false
                        binding.btnConfirm.text = getString(R.string.common_loading)
                    } else if (uiState.error != null) {
                        binding.btnConfirm.isEnabled = true
                        binding.btnConfirm.text = getString(R.string.checkout_confirm_order_button)
                    } else if (uiState.isOrderPlaced) {
                        findNavController().navigate(
                            CheckoutFragmentDirections.actionCheckoutFragmentToOrderSuccessFragment(
                                orderNumber = uiState.orderNumber
                            )
                        )
                    } else {
                        binding.btnConfirm.isEnabled = true
                        binding.btnConfirm.text = getString(R.string.checkout_confirm_order_button)
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