package com.app.felix.simplearchitecture.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.felix.simplearchitecture.MainCoroutineRule
import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import com.app.felix.simplearchitecture.data.network.ChuckNorrisService
import com.app.felix.simplearchitecture.data.state.ResultState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

@ExperimentalCoroutinesApi
class FactRepositoryImplTest {

    @get:Rule
    val mainCoroutineDispatcher = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val service: ChuckNorrisService = mockk(relaxed = true)
    private lateinit var repository: FactRepository

    @Before
    fun setup() {
        clearAllMocks()
        repository = FactRepositoryImpl(service)
    }

    @Test
    fun `test fetch random fact`() = runBlocking {
        val chuckNorrisFact = ChuckNorrisFact(
            iconUrl = "minha_url",
            id = "1",
            url = "outra_url",
            value = "algum fato sobre chuck norris"
        )

        coEvery { service.getRandomFact() } returns chuckNorrisFact

        val resultState = repository.getRandomFact()
        Assert.assertEquals(resultState, ResultState.Success(chuckNorrisFact))
    }

    @Test
    fun `test throw error when try random fact`() = runBlocking {
        coEvery { service.getRandomFact() } throws IOException()

        val resultState = repository.getRandomFact()
        Assert.assertEquals(resultState, ResultState.Error)
    }

    @Test
    fun `test fetch categories`() = runBlocking {
        val categories = listOf("categoria 1", "categoria 1")
        coEvery { service.getCategories() } returns categories

        val resultState = repository.getCategories()

        Assert.assertEquals(resultState, ResultState.Success(categories))
    }

    @Test
    fun `test throw error when try get categories`() = runBlocking {
        coEvery { service.getCategories() } throws IOException()

        val resultState = repository.getCategories()
        Assert.assertEquals(resultState, ResultState.Error)
    }

    @Test
    fun `test fetch random fact categorized`() = runBlocking {
        val category = "categoria 1"
        val chuckNorrisFact = ChuckNorrisFact(
            iconUrl = "minha_url",
            id = "1",
            url = "outra_url",
            value = "algum fato sobre chuck norris"
        )

        coEvery { service.getCategorizedRandomFact(category) } returns chuckNorrisFact

        val resultState = repository.getCategorizedRandomFact(category)
        Assert.assertEquals(resultState, ResultState.Success(chuckNorrisFact))
    }

    @Test
    fun `test throw error when try random fact categorized`() = runBlocking {
        val category = "categoria 1"
        coEvery { service.getCategorizedRandomFact(category) } throws IOException()

        val resultState = repository.getCategorizedRandomFact(category)
        Assert.assertEquals(resultState, ResultState.Error)
    }
}