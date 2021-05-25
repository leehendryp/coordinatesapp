package com.leehendryp.coordinatesapp.presentation

import com.leehendryp.coordinatesapp.domain.Coordinates

sealed class Action {
    data class SaveUpdate(val coordinates: Coordinates) : Action()
    object GetUpdatedList : Action()
}

sealed class State {
    object Saving : State()
    object Retrieving : State()
    object NewLocationSaved : State()
    data class UpdatedList(val list: List<Coordinates>) : State()
    object Error : State()
}