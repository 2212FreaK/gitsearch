package com.aurora.gitsearch.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.aurora.gitsearch.data.local.SearchHistoryLocal
import com.aurora.gitsearch.data.local.SearchHistoryLocalImpl
import com.aurora.gitsearch.data.remote.GithubClient
import com.aurora.gitsearch.data.repository.SearchRepository
import com.aurora.gitsearch.data.repository.SearchRepositoryImpl
import com.aurora.gitsearch.ui.search.SearchViewModel
import com.aurora.gitsearch.ui.search.result.SearchResultViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "GitSearch")

val gitSearchModule = module {

    //moshi
    single<Moshi> {
        Moshi.Builder()
            .build()
    }

    // http client
    single<Retrofit> {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = Level.BODY
                    })
                    .build()
            )
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    // repository
    single<SearchHistoryLocal> {
        SearchHistoryLocalImpl(androidApplication().dataStore)
    }
    single<GithubClient> { get<Retrofit>().create(GithubClient::class.java) }
    single<SearchRepository> { SearchRepositoryImpl(local = get(), remote = get()) }

    // viewmodel
    viewModel { SearchViewModel(searchRepository = get()) }
    viewModel { (query: String) ->
        SearchResultViewModel(
            query = query,
            searchRepository = get()
        )
    }
}

