package com.leehendryp.coordinatesapp.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leehendryp.coordinatesapp.presentation.CoordinatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CoordinatesViewModel::class)
    abstract fun bindViewModel(viewModel: CoordinatesViewModel): ViewModel
}