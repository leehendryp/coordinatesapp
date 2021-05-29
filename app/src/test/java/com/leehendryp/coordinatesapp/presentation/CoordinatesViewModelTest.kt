package com.leehendryp.coordinatesapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.leehendryp.coordinatesapp.core.MainCoroutineRule
import com.leehendryp.coordinatesapp.domain.CoordinatesRepository
import io.mockk.coEvery
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoordinatesViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: CoordinatesRepository
    private lateinit var viewModel: CoordinatesViewModel
    private val coordinates = Coordinates(1.0, 1.0)

    private lateinit var mockedObserver: Observer<State>

    @Test
    @Before
    fun `set up`() {
        repo = spyk()
        mockedObserver = spyk(Observer { Unit })
        viewModel = CoordinatesViewModel(repo)
        viewModel.state.observeForever(mockedObserver)
    }

    @Test
    fun `should reach UpdatedList state upon SaveUpdate action dispatch`(): Unit = runBlocking {
        val stateSlots = mutableListOf<State>()

        coEvery { repo.saveAsync(coordinates) } returns true

        viewModel.dispatch(Action.SaveUpdate(coordinates))

        verify(exactly = 4) { mockedObserver.onChanged(capture(stateSlots)) }

        verifyOrder {
            mockedObserver.onChanged(State.Saving)
            mockedObserver.onChanged(State.NewLocationSaved)
            mockedObserver.onChanged(State.Retrieving)
            mockedObserver.onChanged(any<State.UpdatedList>())
        }
    }
}