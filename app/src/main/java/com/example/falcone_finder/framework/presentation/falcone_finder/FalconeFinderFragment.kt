package com.example.falcone_finder.framework.presentation.falcone_finder

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.falcone_finder.R
import com.example.falcone_finder.business.domain.models.FindResponse
import com.example.falcone_finder.databinding.FragmentFalconeFinderBinding
import com.example.falcone_finder.framework.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FalconeFinderFragment : BaseFragment<FalconeFinderViewModel, FragmentFalconeFinderBinding>() {
    override fun getViewBinding(): FragmentFalconeFinderBinding {
        return FragmentFalconeFinderBinding.inflate(layoutInflater)
    }

    override fun onViewReady(view: View?, savedStateInstance: Bundle?, arguments: Bundle?) {
        observeData()
        setUpListeners()
        setupOnBackPressedCallback {
            val animationOptions = NavOptions.Builder().setEnterAnim(R.anim.no_anim)
                .setExitAnim(R.anim.no_anim)
                .setPopEnterAnim(R.anim.no_anim)
                .setPopExitAnim(R.anim.no_anim).build()
            findNavController().navigate(
                R.id.action_falconeFinderFragment_to_falconeSelectionFragment,
                null,animationOptions
            )
        }
    }

    private fun setUpListeners() {
        binding.startAgain.setOnClickListener {
            findNavController().navigate(
                R.id.action_falconeFinderFragment_to_falconeSelectionFragment,
                null
            )
        }
    }

    private fun renderView(state: FindResponse) {
        when (state) {
            is FindResponse.Success -> {
                binding.status.text =
                    getString(R.string.princes_found_on, state.planetName)
                binding.status.setTextColor(resources.getColor(R.color.color_primary))
            }

            is FindResponse.Failure -> {
                binding.status.text = getString(R.string.princes_not_found)
                binding.status.setTextColor(Color.RED)
            }
        }
        binding.startAgain.isVisible = true
    }

    private fun observeData() {
        viewModel.statusData.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }
}

fun Fragment.setupOnBackPressedCallback(block: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = block.invoke()
        }
    )
}