package com.example.cartapp.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cartapp.R
import com.example.cartapp.databinding.FragmentSettingsBinding
import com.example.cartapp.presentation.ui_state.SettingsUIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }

    private fun setupUI() {
        binding.customAppBar.tvTitle.text = getString(R.string.settings_title)
        binding.customAppBar.btnBack.visibility = View.VISIBLE
        binding.customAppBar.btnBack.setImageResource(R.drawable.ic_arrow_back)
        binding.customAppBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.customAppBar.btnRightAction.visibility = View.GONE
        binding.customAppBar.customContentArea.visibility = View.GONE
    }

    private fun setupListeners() {
        // Language selection
        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val newLanguage = when (checkedId) {
                R.id.rbTurkish -> "tr"
                R.id.rbEnglish -> "en"
                else -> return@setOnCheckedChangeListener
            }
            
            if (viewModel.currentLanguage.value != newLanguage) {
                applyLanguageChange(newLanguage)
            }
        }
    }
    
    private fun applyLanguageChange(newLanguage: String) {
        viewModel.setLanguage(newLanguage)
        showLanguageChangeSuccess()
        restartApp()
    }
    
    private fun restartApp() {
        val intent = requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
        intent?.let { safeIntent ->
            safeIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(safeIntent)
            requireActivity().finish()
        }
    }
    
    private fun showLanguageChangeSuccess() {
        android.widget.Toast.makeText(
            requireContext(),
            getString(R.string.settings_language_changed_success),
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupObservers() {
        viewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            binding?.let { safeBinding ->
                when (language) {
                    "tr" -> safeBinding.rbTurkish.isChecked = true
                    "en" -> safeBinding.rbEnglish.isChecked = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 