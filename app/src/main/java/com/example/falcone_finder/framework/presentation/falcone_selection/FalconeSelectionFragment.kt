package com.example.falcone_finder.framework.presentation.falcone_selection

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.falcone_finder.R
import com.example.falcone_finder.business.domain.utils.TokenStatus
import com.example.falcone_finder.databinding.ErrorPlaceholderLayoutBinding
import com.example.falcone_finder.databinding.FragmentFalconeFinderBinding
import com.example.falcone_finder.databinding.FragmentFalconeSelectionBinding
import com.example.falcone_finder.framework.utils.collectLifecycleAwareChannelFlow
import com.example.falcone_finder.framework.utils.BaseFragment
import com.example.falcone_finder.framework.utils.autoCleared
import com.example.falcone_finder.framework.utils.collectLifecycleAwareFlow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FalconeSelectionFragment :
    BaseFragment<FalconeSelectionViewModel, FragmentFalconeSelectionBinding>() {

    @Inject
    lateinit var requestManager: RequestManager

    private var errorViewStub: ErrorPlaceholderLayoutBinding by autoCleared()
    override fun getViewBinding(): FragmentFalconeSelectionBinding =
        FragmentFalconeSelectionBinding.inflate(layoutInflater)


    override fun setUpWork() {
        super.setUpWork()
        fetchPlanetAndVehicleSelection()
    }

    override fun onViewReady(view: View?, savedStateInstance: Bundle?, arguments: Bundle?) {
        observeData()
        setUpClickListeners()
        respondToUiEvents()
    }


    private fun fetchPlanetAndVehicleSelection() = viewModel.populatePlanetAndVehicleIndexs()


    private fun respondToUiEvents() {
        collectLifecycleAwareChannelFlow(viewModel.uiEvents) { event ->
            when (event) {
                is FalconeScreenUIEvent.navigateToFindPrincess -> {
                    val bundle = Bundle()
                    bundle.apply {
                        putParcelable("falconeFindingData", event.data)

                    }
                    findNavController().navigate(
                        R.id.action_falconeSelectionFragment_to_falconeFinderFragment,
                        bundle
                    )
                }

                is FalconeScreenUIEvent.prevConfirmDialog -> {
                    viewModel.onEventRecieved(FalconeSelectionScreenEvent.onPrevConfirm)
                }

                is FalconeScreenUIEvent.refreshUI -> {
                    if (event.prevState == null) {
                        resetPlanetSpinnerAndVehicleGroup(0, 0)
                    } else {
                        resetPlanetSpinnerAndVehicleGroup(
                            event.prevState.first,
                            event.prevState.second
                        )
                    }
                    binding.prevButton.isVisible =
                        ((event.indexOfSelection <= 1)).not()
                    binding.forwardButton.text = if (event.isFinalStage) "Find Falcone!" else "Next"
                    binding.planetHeading.text =
                        "Destination Planet ${event.indexOfSelection}  : "
                }

                is FalconeScreenUIEvent.progressBarStatus -> {
                    binding.progressBar.isVisible = event.show
                }

                is FalconeScreenUIEvent.showTost -> {
                    Toast.makeText(context, event.content, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpClickListeners() {
        binding.prevButton.setOnClickListener {
            viewModel.onEventRecieved(FalconeSelectionScreenEvent.onPrev)
        }
        binding.forwardButton.setOnClickListener {
            viewModel.onEventRecieved(FalconeSelectionScreenEvent.onNext)
        }
    }

    private fun observeData() {
        viewModel.planetsData.observe(viewLifecycleOwner) { response ->
            setUpSpinnerView(response.map { it.name })
            binding.forwardButton.isVisible = true
        }
        viewModel.vehiclesData.observe(viewLifecycleOwner) { response ->
            setRadioGroupChangeListener(response.map { it.name })
            binding.forwardButton.isVisible = true
        }
        collectLifecycleAwareFlow(viewModel.tokenStatus) { response ->
            if (response == TokenStatus.TOKEN_FETCH_FAILED) {
                binding.group.isVisible = false
                binding.errorPlaceholderStub.setOnInflateListener { _, view ->
                    errorViewStub = ErrorPlaceholderLayoutBinding.bind(view)
                }
                binding.errorPlaceholderStub.inflate()
            } else if (response == TokenStatus.TOKEN_FETCH_SUCCESS) {
                binding.group.isVisible = true
                binding.prevButton.isVisible = false
                binding.forwardButton.isVisible = false
                binding.errorPlaceholderStub.isVisible=false
            }
        }
        viewModel.selectionData.observe(viewLifecycleOwner) { triple ->
            val (spinnerIndex, radioGroupIndex, destinationIndex) = triple
            resetPlanetSpinnerAndVehicleGroup(spinnerIndex, radioGroupIndex)
            loadImage(spinnerIndex + 1, radioGroupIndex + 1)
            binding.planetHeading.text =
                "Destination Planet ${destinationIndex}  : "
        }
    }


    // setup spinner and radio buttons.
    private fun setUpSpinnerView(spinnerData: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            R.id.itemText,
            spinnerData
        )
        binding.planetSpinner.adapter = adapter
        binding.planetSpinner.animation = null
        binding.planetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedPlanet = adapterView?.getItemAtPosition(position).toString()
                viewModel.onEventRecieved(
                    FalconeSelectionScreenEvent.onPlanetSelection(
                        position, selectedPlanet
                    )
                )
                reloadPlanetImage(position + 1)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }
    }

    private fun setRadioGroupChangeListener(options: List<String>) {
        if (options.isEmpty()) {
            binding.vehicleGroup.visibility = View.GONE
            return
        }
        for (option in options) {
            val radioButton = RadioButton(context)
            radioButton.text = option
            binding.vehicleGroup.addView(radioButton)
        }
        binding.vehicleGroup.check(binding.vehicleGroup.getChildAt(0).id)
        viewModel.onEventRecieved(
            FalconeSelectionScreenEvent.onVehicleSelection(
                0, options[0]
            )
        )
        binding.vehicleGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton = view?.findViewById<RadioButton>(checkedId)
            val selectedIndex = radioGroup.indexOfChild(radioButton)
            if (radioButton != null) {
                val selectedOption = radioButton.text.toString()
                viewModel.onEventRecieved(
                    FalconeSelectionScreenEvent.onVehicleSelection(
                        selectedIndex, selectedOption
                    )
                )
                reloadVehicleImage(selectedIndex + 1)
            }
        }
    }

    private fun resetPlanetSpinnerAndVehicleGroup(spinnerPosition: Int, radioGroupPosition: Int) {
        binding.planetSpinner.setSelection(spinnerPosition)
        if (binding.vehicleGroup.childCount > radioGroupPosition) {
            binding.vehicleGroup.check(binding.vehicleGroup.getChildAt(radioGroupPosition).id)
            binding.vehicleGroup.jumpDrawablesToCurrentState()
        }
    }

    // image loading  helpers.
    private fun loadImage(planetIndex: Int, vehicleIndex: Int) {
        reloadPlanetImage(planetIndex)
        reloadVehicleImage(vehicleIndex)
    }

    private fun reloadPlanetImage(planetIndex: Int) {
        requestManager.load(getPlanetImageForPosition(planetIndex)).into(binding.planetImage)
    }

    private fun reloadVehicleImage(vehicleIndex: Int) {
        requestManager.load(getVehicleImageForPosition(vehicleIndex)).into(binding.vehicleImage)
    }

    private fun getPlanetImageForPosition(index: Int): Int {
        return when (index) {
            1 -> R.drawable.ic_planet3
            2 -> R.drawable.ic_planet5
            3 -> R.drawable.ic_planet7
            4 -> R.drawable.ic_planet8
            5 -> R.drawable.ic_planet6
            6 -> R.drawable.ic_planet10
            else -> R.drawable.ic_planet3
        }
    }

    private fun getVehicleImageForPosition(index: Int): Int {
        return when (index) {
            1 -> R.drawable.ic_spaceship1
            2 -> R.drawable.ic_spaceship2
            3 -> R.drawable.ic_spaceship3
            4 -> R.drawable.ic_spaceship4
            else -> R.drawable.ic_spaceship1
        }
    }
}