package com.app.felix.simplearchitecture.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import com.app.felix.simplearchitecture.data.repository.FactRepository
import com.app.felix.simplearchitecture.data.state.ResultState
import com.app.felix.simplearchitecture.data.state.ViewState
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FactRepository
) : ViewModel() {

    private val _factViewState = MutableLiveData<ViewState<ChuckNorrisFact>>()
    val factViewState: LiveData<ViewState<ChuckNorrisFact>> = _factViewState

    private val _categoriesViewState = MutableLiveData<ViewState<List<String>>>()
    val categoriesViewState: LiveData<ViewState<List<String>>> = _categoriesViewState

    var searchCategory = ""

    init {
        getFact()
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            when (val categoriesResponse = repository.getCategories()) {
                is ResultState.Success -> _categoriesViewState.postValue(
                    ViewState.ContentLoaded(
                        categoriesResponse.data
                    )
                )
                is ResultState.Error -> _categoriesViewState.postValue(ViewState.ContentFailure)
            }
        }
    }

    fun getFact() {
        viewModelScope.launch {
            _factViewState.postValue(ViewState.Loading)

            val response = if (searchCategory.isNotEmpty()) {
                repository.getCategorizedRandomFact(category = searchCategory)
            } else {
                repository.getRandomFact()
            }

            when (response) {
                is ResultState.Success -> {
                    _factViewState.postValue(ViewState.ContentLoaded(response.data))
                }
                is ResultState.Error -> _factViewState.postValue(ViewState.ContentFailure)
            }
        }
    }

    fun setCategory(text: String) {
        searchCategory = text
    }

}