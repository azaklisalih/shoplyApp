package com.example.cartapp.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cartapp.R
import com.example.cartapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
        setupWindowInsets()
    }

    private fun setupUI() {
        binding.customAppBar.tvTitle.text = getString(R.string.profile_title)
        binding.customAppBar.btnBack.visibility = View.GONE
        binding.customAppBar.btnRightAction.visibility = View.GONE
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.ivAvatar.setOnClickListener {
        }

        binding.cardOrders.setOnClickListener {
        }

        binding.cardSettings.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            )
        }
    }

    private fun setupObservers() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 