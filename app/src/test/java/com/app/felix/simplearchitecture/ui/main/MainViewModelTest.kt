package com.app.felix.simplearchitecture.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.felix.simplearchitecture.MainCoroutineRule
import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import com.app.felix.simplearchitecture.data.repository.FactRepository
import com.app.felix.simplearchitecture.data.state.ResultState
import com.app.felix.simplearchitecture.data.state.ViewState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mainCoroutineDispatcher = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val repository: FactRepository = mockk(relaxed = true)

    private val factViewStateObserver: Observer<ViewState<ChuckNorrisFact>> = mockk(relaxed = true)
    private val categoriesViewStateObserver: Observer<ViewState<List<String>>> =
        mockk(relaxed = true)

    @Before
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `test load fact and categories correctly`() = runBlockingTest {
        val chuckNorrisFact = ChuckNorrisFact(
            icon_url = "minha_url",
            id = "1",
            url = "minha_url",
            value = "alguma coisa sobre o chuck norrus"
        )
        coEvery { repository.getRandomFact() } returns ResultState.Success(chuckNorrisFact)

        val categories = listOf("categoria 1", "categoria 2")
        coEvery { repository.getCategories() } returns ResultState.Success(categories)

        val viewModel = MainViewModel(repository)
        viewModel.factViewState.observeForever(factViewStateObserver)
        viewModel.categoriesViewState.observeForever(categoriesViewStateObserver)

        verify {
            factViewStateObserver.onChanged(ViewState.ContentLoaded(chuckNorrisFact))
            categoriesViewStateObserver.onChanged(ViewState.ContentLoaded(categories))
        }
    }

    @Test
    fun `test load fact and categories with error`() = runBlockingTest {
        coEvery { repository.getRandomFact() } returns ResultState.Error
        coEvery { repository.getCategories() } returns ResultState.Error

        val viewModel = MainViewModel(repository)
        viewModel.factViewState.observeForever(factViewStateObserver)
        viewModel.categoriesViewState.observeForever(categoriesViewStateObserver)

        verify {
            factViewStateObserver.onChanged(ViewState.ContentFailure)
            categoriesViewStateObserver.onChanged(ViewState.ContentFailure)
        }
    }

    @Test
    fun `test fetch fact with selected category`() {
        val chuckNorrisFact = ChuckNorrisFact(
            icon_url = "minha_url",
            id = "1",
            url = "minha_url",
            value = "alguma coisa sobre o chuck norrus"
        )
        coEvery { repository.getRandomFact() } returns ResultState.Success(chuckNorrisFact)

        val categorizedChuckNorrisFact = ChuckNorrisFact(
            icon_url = "minha_url",
            id = "2",
            url = "minha_url",
            value = "alguma outra coisa sobre o chuck norrus"
        )
        coEvery {
            repository.getCategorizedRandomFact("categoria 1")
        } returns ResultState.Success(categorizedChuckNorrisFact)

        val categories = listOf("categoria 1", "categoria 2")
        coEvery { repository.getCategories() } returns ResultState.Success(categories)

        val viewModel = MainViewModel(repository)
        viewModel.factViewState.observeForever(factViewStateObserver)

        verify {
            factViewStateObserver.onChanged(ViewState.ContentLoaded(chuckNorrisFact))
        }

        viewModel.setCategory("categoria 1")
        viewModel.getFact()

        verifySequence {
            factViewStateObserver.onChanged(ViewState.ContentLoaded(chuckNorrisFact))
            factViewStateObserver.onChanged(ViewState.Loading)
            factViewStateObserver.onChanged(ViewState.ContentLoaded(categorizedChuckNorrisFact))
        }
    }
}