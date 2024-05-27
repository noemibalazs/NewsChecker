package com.noemi.newschecker

import com.noemi.newschecker.model.NetworkResult
import com.noemi.newschecker.model.News
import com.noemi.newschecker.model.NewsResults
import com.noemi.newschecker.repository.NewYorkTimesRepository
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCase
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetMostPopularNewsUseCaseImplTest {

    private val repository: NewYorkTimesRepository = mockk()

    private val newsResult: NewsResults = mockk()
    private val news: News = mockk()
    private val exception: Exception = mockk()
    private val error = "error message"
    private val key = "your_key"

    private lateinit var useCase: GetMostPopularNewsUseCase

    @Before
    fun setUp() {
        useCase = GetMostPopularNewsUseCaseImpl(repository)
    }

    @Test
    fun `test get news returns list successfully`() = runBlocking {
        val job = launch {
            coEvery { repository.getMostPopularNews(key) } returns NetworkResult.Success(newsResult)
            coEvery { newsResult.results } returns listOf(news)

            useCase.execute(key)

            coVerify { repository.getMostPopularNews(key) }
        }
        job.cancelAndJoin()
    }

    @Test
    fun `test get news returns empty list successfully`() = runBlocking {
        val job = launch {
            coEvery { repository.getMostPopularNews(key) } returns NetworkResult.Success(newsResult)
            coEvery { newsResult.results } returns emptyList()

            useCase.execute(key)

            coVerify { repository.getMostPopularNews(key) }
        }
        job.cancelAndJoin()
    }

    @Test
    fun `test get news handles error as expected`() = runBlocking {
        val job = launch {
            coEvery { repository.getMostPopularNews(key) } returns NetworkResult.Error(error)

            useCase.execute(key)

            coVerify { repository.getMostPopularNews(key) }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `test get news handles failure as expected`() = runBlocking {
        val job = launch {
            coEvery { repository.getMostPopularNews(key) } returns NetworkResult.Failure(exception)

            useCase.execute(key)

            coVerify { repository.getMostPopularNews(key) }
        }

        job.cancelAndJoin()
    }
}