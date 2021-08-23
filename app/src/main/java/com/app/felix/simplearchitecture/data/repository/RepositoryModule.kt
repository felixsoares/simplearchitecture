package com.app.felix.simplearchitecture.data.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<FactRepository> {
        FactRepositoryImpl(chuckNorrisService = get())
    }
}