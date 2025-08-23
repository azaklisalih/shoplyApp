package com.example.cartapp.presentation.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cartapp.databinding.FragmentCheckoutBinding
import com.example.cartapp.presentation.ui_state.CheckoutUIState
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
        setupListeners()
        setupObservers()
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
    }

    private fun validateInputs(name: String, email: String, phone: String): Boolean {
        if (name.isBlank()) {
            binding.etName.error = "Name is required"
            return false
        }
        if (email.isBlank()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        if (phone.isBlank()) {
            binding.etPhone.error = "Phone is required"
            return false
        }
        return true
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                if (uiState.isLoading) {
                    binding.btnConfirm.isEnabled = false
                    binding.btnConfirm.text = "Processing..."
                } else if (uiState.error != null) {
                    binding.btnConfirm.isEnabled = true
                    binding.btnConfirm.text = "Confirm"
                } else if (uiState.isOrderPlaced) {
                    findNavController().navigate(
                        CheckoutFragmentDirections.actionCheckoutFragmentToOrderSuccessFragment(
                            orderNumber = uiState.orderNumber
                        )
                    )
                } else {
                    binding.btnConfirm.isEnabled = true
                    binding.btnConfirm.text = "Confirm"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}