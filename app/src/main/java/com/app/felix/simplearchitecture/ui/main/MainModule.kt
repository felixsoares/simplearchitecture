package com.app.felix.simplearchitecture.ui.main

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModule = module {
    viewModel { MainViewModel(repository = get()) }
}