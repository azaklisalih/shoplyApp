package com.example.cartapp.presentation.ordersuccess

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cartapp.MainActivity
import com.example.cartapp.databinding.FragmentOrderSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSuccessFragment : Fragment() {
    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderSuccessViewModel by viewModels()
    private val args: OrderSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }

    private fun setupUI() {
        viewModel.setOrderNumber(args.orderNumber)
    }

    private fun setupListeners() {
        binding.btnGoHome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    
    private fun setupObservers() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 