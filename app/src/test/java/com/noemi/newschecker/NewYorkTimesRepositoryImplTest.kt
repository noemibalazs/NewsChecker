package com.noemi.newschecker

import com.noemi.newschecker.helper.ApiResponseHelper
import com.noemi.newschecker.model.NetworkResult
import com.noemi.newschecker.model.News
import com.noemi.newschecker.network.NewYorkTimesApi
import com.noemi.newschecker.repository.NewYorkTimesRepository
import com.noemi.newschecker.repository.NewYorkTimesRepositoryImpl
import com.noemi.newschecker.utils.YOUR_KEY
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class NewYorkTimesRepositoryImplTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var newYorkTimesAPI: NewYorkTimesApi

    @Mock
    private lateinit var responseHelper: ApiResponseHelper

    private lateinit var repository: NewYorkTimesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        repository = NewYorkTimesRepositoryImpl(
            dispatcher = dispatcher,
            apiResponseHelper = responseHelper,
            newYorkTimesApi = newYorkTimesAPI
        )
    }

    @Test
    fun `test get most popular news and should be successful`() = runBlocking {
        val news: News = mock()
        val newsList = listOf(news)

        val job = launch {
            val response = responseHelper.handleApiResponse { newYorkTimesAPI.getMostPopularNews(YOUR_KEY) }
            response.shouldBe(NetworkResult.Success(newsList))
        }

        repository.getMostPopularNews()

        job.cancelAndJoin()
    }

    @Test
    fun `test get most popular news and should return network result failure`() = runBlocking {
        val exception: Exception = mock()

        val job = launch {
            val response = responseHelper.handleApiResponse { newYorkTimesAPI.getMostPopularNews(YOUR_KEY) }
            response.runCatching {
                throw exception
            }
            response.shouldBe(NetworkResult.Failure(exception))
        }

        repository.getMostPopularNews()

        job.cancelAndJoin()
    }

    @Test
    fun `test get most popular news and should return network result error`() = runBlocking {
        val exception: HttpException = mock()

        val job = launch {
            val response = responseHelper.handleApiResponse { newYorkTimesAPI.getMostPopularNews(YOUR_KEY) }
            response.runCatching {
                throw exception
            }
            response.shouldBe(NetworkResult.Error(exception.message()))
        }

        repository.getMostPopularNews()

        job.cancelAndJoin()
    }
}