package com.example.falcone_finder.framework.presentation.falcone_selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import com.example.falcone_finder.business.usecases.falcone_selection.FalconeSelectionUseCases
import com.example.falcone_finder.utils.getOrAwaitValueTest
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FalconeSelectionViewModelTest {
    private lateinit var viewModel: FalconeSelectionViewModel
    private val falconeSelectionUseCases: FalconeSelectionUseCases = mockk(relaxed = true)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Default) // Set up the main dispatcher
        viewModel = FalconeSelectionViewModel(falconeSelectionUseCases)
    }


    @Test
    fun `getPlanetsData should update planetsData on success and show error message on failure`() =
        runBlocking {
            // Mock planets API response
            val planetsApiResponseList = listOf(PlanetsApiResponse("Earth", 0))
            coEvery { falconeSelectionUseCases.fetchPlanetsUseCase.invoke() } returns Result.success(
                planetsApiResponseList
            )
            // Run the getPlanetsData function
            viewModel.getPlanetsData()
            val result = viewModel.planetsData.getOrAwaitValueTest()
            // Verify that planetsData is updated
            assert(result.size == planetsApiResponseList.size)
            assert(result == planetsApiResponseList)
        }


    @Test
    fun `getVehiclesData should update vehiclesData on success and show error message on failure`() =
        runBlocking {
            // Mock vehicles API response
            val vehiclesApiResponseList = listOf(VehiclesApiResponse("Rover", 2, 0, 0))
            coEvery { falconeSelectionUseCases.fetchVehiclesUseCase.invoke() } returns Result.success(
                vehiclesApiResponseList
            )

            // Run the getVehiclesData function
            viewModel.getVehiclesData()
            val result = viewModel.vehiclesData.getOrAwaitValueTest()
            // Verify that vehiclesData is updated
            assert(result == vehiclesApiResponseList)
        }


    @After
    fun teardown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}