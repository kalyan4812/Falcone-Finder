package com.example.falcone_finder.business.usecases.falcone_selection

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Test


class FalconeDestinationStackUseCaseTest {

    private lateinit var useCase: FalconeDestinationStackUseCase

    @Before
    fun setUpWork() {
        // to do some work before any test case.
        useCase = FalconeDestinationStackUseCase()
    }

    @Test
    fun testPushSelection() {
        val planetIndex = 0
        val planetSelection = "Earth"
        val vehicleIndex = 1
        val vehicleSelection = "SpaceX Dragon"

        useCase.pushSelection(planetIndex, planetSelection, vehicleIndex, vehicleSelection)

        assertEquals(1, useCase.currentSize())
        assertEquals(planetIndex, useCase.peekSelection().planetIndex)
        assertEquals(planetSelection, useCase.peekSelection().planetName)
        assertEquals(vehicleIndex, useCase.peekSelection().vehicleIndex)
        assertEquals(vehicleSelection, useCase.peekSelection().vehicleName)
    }

    @Test
    fun testPopSelection() {
        useCase.pushSelection(0, "Earth", 1, "SpaceX Dragon")

        useCase.popSelection()

        assertEquals(0, useCase.currentSize())
    }

    @Test
    fun testPeekSelectionWithoutSelection() {
        useCase.pushSelection(0, "Earth", 1, "SpaceX Dragon")
        val result = useCase.peekSelection()
        assertEquals("Earth", result.planetName)
        assertEquals("SpaceX Dragon", result.vehicleName)
    }

    @Test
    fun testGetFindRequestFromStack() {
        useCase.pushSelection(0, "Earth", 1, "SpaceX Dragon")
        useCase.pushSelection(2, "Mars", 0, "Rover")

        val result = useCase.getFindRequestFromStack()

        assertEquals(2, result.planetNames?.size)
        assertEquals(2, result.vehicleNames?.size)
        assertEquals("Earth", result.planetNames?.getOrNull(0))
        assertEquals("Mars", result.planetNames?.getOrNull(1))
        assertEquals("SpaceX Dragon", result.vehicleNames?.getOrNull(0))
        assertEquals("Rover", result.vehicleNames?.getOrNull(1))
    }

    @Test
    fun testClearStack() {
        useCase.pushSelection(0, "Earth", 1, "SpaceX Dragon")
        useCase.pushSelection(2, "Mars", 0, "Rover")

        useCase.clearStack()

        assertEquals(0, useCase.currentSize())
    }

    @Test
    fun testIsMaxLimitReached() {
        useCase.pushSelection(0, "Earth", 1, "SpaceX Dragon")
        useCase.pushSelection(2, "Mars", 0, "Rover")
        useCase.pushSelection(1, "Venus", 3, "NASA Shuttle")
        useCase.pushSelection(3, "Venus", 3, "NASA Shuttle")
        assert(useCase.isMaxLimitReached())
        useCase.popSelection()
        assertFalse(useCase.isMaxLimitReached())
    }


}