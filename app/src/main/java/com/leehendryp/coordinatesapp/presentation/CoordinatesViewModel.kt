package com.leehendryp.coordinatesapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leehendryp.coordinatesapp.domain.Coordinates
import com.leehendryp.coordinatesapp.domain.CoordinatesRepository
import com.leehendryp.coordinatesapp.presentation.Action.GetUpdatedList
import com.leehendryp.coordinatesapp.presentation.Action.SaveUpdate
import javax.inject.Inject
import kotlinx.coroutines.launch

class CoordinatesViewModel @Inject constructor(
    private val repo: CoordinatesRepository
) : ViewModel() {

    private val action by lazy { MutableLiveData<Action>(GetUpdatedList) }

    val state: LiveData<State> by lazy { switchMap(action) { process(it) } }

    fun dispatch(action: Action) {
        this.action.value = action
    }

    private fun process(action: Action): LiveData<State> {
        return MutableLiveData<State>().apply {
            viewModelScope.launch {
                when (action) {
                    is SaveUpdate -> save(action.coordinates)
                    GetUpdatedList -> getUpdatedList()
                }
            }
        }
    }

    private suspend fun MutableLiveData<State>.save(coordinates: Coordinates) {
        value = State.Saving
        with(repo.save(coordinates)) {
            value = if (this) State.NewLocationSaved else State.Error
        }
        dispatch(GetUpdatedList)
    }

    private suspend fun MutableLiveData<State>.getUpdatedList() {
        value = State.Retrieving

        try {
            with(repo.getCoordinateEntries()) { State.UpdatedList(this) }
        } catch (throwable: Throwable) {
            State.Error
        }
    }
}