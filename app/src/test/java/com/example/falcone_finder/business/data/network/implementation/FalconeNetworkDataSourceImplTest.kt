package com.example.falcone_finder.business.data.network.implementation

import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.domain.model.FindApiResponse
import com.example.falcone_finder.business.domain.model.TokenApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class FalconeNetworkDataSourceImplTest {
    private lateinit var dataSource: FalconeNetworkDataSourceImpl

    @Before
    fun setup() {
        dataSource = mockk()
    }

    @Test
    fun `getToken should return a Result with success`() = runBlocking {

        coEvery { dataSource.getToken() } returns Result.success(TokenApiResponse("sample_token"))


        val result = dataSource.getToken()


        assert(result.isSuccess)
    }

    @Test
    fun `getToken should return a Result with failure`() = runBlocking {

        coEvery { dataSource.getToken() } returns Result.failure(Throwable("404 not found"))


        val result = dataSource.getToken()


        assert(result.isFailure)
    }


    @Test
    fun `getPlanets should return a Result with success`() = runBlocking {

        val planetsApiResponse =
            listOf(PlanetsApiResponse("Earth", 0), PlanetsApiResponse("Mars", 0))

        coEvery { dataSource.getPlanets() } returns Result.success(planetsApiResponse)


        val result = dataSource.getPlanets()

        assert(result.isSuccess)
        val planets = (result).getOrNull()
        checkNotNull(planets)
        assert(planets.size == 2)
        assert(planets[0].name == "Earth")
        assert(planets[0].distance == 0)
        assert(planets[1].name == "Mars")
        assert(planets[1].distance == 0)
    }

    @Test
    fun `getVehicles should return a Result with success`() = runBlocking {

        val vehiclesApiResponse = listOf(VehiclesApiResponse("Rover", 2, 0, 2))
        coEvery { dataSource.getVehicles() } returns Result.success(vehiclesApiResponse)


        val result = dataSource.getVehicles()

        assert(result.isSuccess)
        val vehicles = (result).getOrNull()
        checkNotNull(vehicles)
        assert(vehicles.size == 1)
        assert(vehicles[0].name == "Rover")
        assert(vehicles[0].amount == 2)
        assert(vehicles[0].speed == 2)
        assert(vehicles[0].maxDistance == 0)
    }

    @Test
    fun `findPrincess should return a Result with success`() = runBlocking {

        val findApiResponse = FindApiResponse("success", "Found the princess!")
        coEvery {
            dataSource.findPrincess(
                any(),
                any()
            )
        } returns Result.success(findApiResponse)


        val result = dataSource.findPrincess(listOf("Earth"), listOf("Rover"))


        assert(result.isSuccess)
    }
}