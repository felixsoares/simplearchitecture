package com.app.felix.simplearchitecture.data.repository

import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import com.app.felix.simplearchitecture.data.state.ResultState
import com.app.felix.simplearchitecture.data.network.ChuckNorrisService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface FactRepository {
    suspend fun getRandomFact(): ResultState<ChuckNorrisFact>
    suspend fun getCategorizedRandomFact(category: String): ResultState<ChuckNorrisFact>
    suspend fun getCategories(): ResultState<List<String>>
}

class FactRepositoryImpl(
    private val chuckNorrisService: ChuckNorrisService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FactRepository {

    override suspend fun getRandomFact(): ResultState<ChuckNorrisFact> = withContext(dispatcher) {
        return@withContext try {
            val fact = chuckNorrisService.getRandomFact()
            ResultState.Success(fact)
        } catch (exception: Exception) {
            ResultState.Error
        }
    }

    override suspend fun getCategorizedRandomFact(category: String): ResultState<ChuckNorrisFact> =
        withContext(dispatcher) {
            return@withContext try {
                val fact = chuckNorrisService.getCategorizedRandomFact(category)
                ResultState.Success(fact)
            } catch (exception: Exception) {
                ResultState.Error
            }
        }

    override suspend fun getCategories(): ResultState<List<String>> = withContext(dispatcher) {
        return@withContext try {
            val fact = chuckNorrisService.getCategories()
            ResultState.Success(fact)
        } catch (exception: Exception) {
            ResultState.Error
        }
    }

}