package com.leehendryp.coordinatesapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leehendryp.coordinatesapp.data.local.Coordinates
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
        postValue(State.Saving)
        with(repo.save(coordinates)) {
            postValue(
                if (this) State.NewLocationSaved else State.Error
            )
        }
        dispatch(GetUpdatedList)
    }

    private suspend fun MutableLiveData<State>.getUpdatedList() {
        postValue(State.Retrieving)

        try {
            with(repo.getCoordinateEntries()) {
                postValue(State.UpdatedList(this))
            }
        } catch (throwable: Throwable) {
            postValue(State.Error)
        }
    }
}