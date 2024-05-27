package com.noemi.newschecker

import com.noemi.newschecker.helper.ApiResponseHelper
import com.noemi.newschecker.model.NetworkResult
import com.noemi.newschecker.model.News
import com.noemi.newschecker.model.NewsResults
import com.noemi.newschecker.network.NewYorkTimesApi
import com.noemi.newschecker.repository.NewYorkTimesRepository
import com.noemi.newschecker.repository.NewYorkTimesRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewYorkTimesRepositoryImplTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val newYorkTimesAPI: NewYorkTimesApi = mockk()
    private val responseHelper: ApiResponseHelper = mockk()

    private lateinit var repository: NewYorkTimesRepository
    private val key = "your_key"

    private val response: Response<NewsResults> = mockk()
    private val newsResult: NewsResults = mockk()
    private val news: News = mockk()
    private val errorBody: ResponseBody = mockk()
    private val exception: Exception = mockk()
    private val error = "error"

    @Before
    fun setUp() {

        repository = NewYorkTimesRepositoryImpl(
            dispatcher = dispatcher,
            apiResponseHelper = responseHelper,
            newYorkTimesApi = newYorkTimesAPI
        )
    }

    @Test
    fun `test response helper returns empty list successfully`() = runBlocking {

        val job = launch {
            coEvery { newYorkTimesAPI.getMostPopularNews(key) } returns response
            coEvery { responseHelper.handleApiResponse { response } } returns NetworkResult.Success(newsResult)
            coEvery { newsResult.results } returns emptyList()

            repository.getMostPopularNews(key)

            coVerify { newYorkTimesAPI.getMostPopularNews(key) }
            coVerify { responseHelper.handleApiResponse { response } }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `test response helper returns list successfully`() = runBlocking {

        val job = launch {
            coEvery { newYorkTimesAPI.getMostPopularNews(key) } returns response
            coEvery { responseHelper.handleApiResponse { response } } returns NetworkResult.Success(newsResult)
            coEvery { newsResult.results } returns listOf(news)

            repository.getMostPopularNews(key)

            coVerify { newYorkTimesAPI.getMostPopularNews(key) }
            coVerify { responseHelper.handleApiResponse { response } }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `test response helper handles error as expected`() = runBlocking {

        val job = launch {
            coEvery { newYorkTimesAPI.getMostPopularNews(key) } returns response
            coEvery { response.isSuccessful } returns false
            coEvery { response.body() } returns null
            coEvery { response.errorBody() } returns errorBody
            coEvery { errorBody.toString() } returns error
            coEvery { responseHelper.handleApiResponse { response } } returns NetworkResult.Error(error)

            repository.getMostPopularNews(key)

            coVerify { newYorkTimesAPI.getMostPopularNews(key) }
            coVerify { responseHelper.handleApiResponse { response } }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `test response helper throws failure`() = runBlocking {

        val job = launch {
            coEvery { newYorkTimesAPI.getMostPopularNews(key) } throws exception
            coEvery { responseHelper.handleApiResponse { response } } returns NetworkResult.Failure(exception)

            repository.getMostPopularNews(key)

            coVerify { newYorkTimesAPI.getMostPopularNews(key) }
            coVerify { responseHelper.handleApiResponse { response } }
        }

        job.cancelAndJoin()
    }
}