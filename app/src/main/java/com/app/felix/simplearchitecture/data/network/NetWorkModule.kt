package com.app.felix.simplearchitecture.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val netWorkModule = module {
    single<Retrofit> {
        Retrofit
            .Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .baseUrl("https://api.chucknorris.io/jokes/")
            .build()
    }

    single<ChuckNorrisService> {
        get<Retrofit>().create(ChuckNorrisService::class.java)
    }

    single {
        OkHttpClient
            .Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<GsonConverterFactory> { GsonConverterFactory.create() }
}