package com.noemi.newschecker

import com.noemi.newschecker.model.News
import com.noemi.newschecker.repository.NewYorkTimesRepository
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCase
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCaseImpl
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock

class GetMostPopularNewsUseCaseImplTest {

    @Mock
    private lateinit var repository: NewYorkTimesRepository

    private lateinit var useCase: GetMostPopularNewsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetMostPopularNewsUseCaseImpl(repository)
    }

    @Test
    fun `test get news and should be successful`() = runBlocking {
        val news: News = mock()

        val job = launch {
            val result = repository.getMostPopularNews()
            result.shouldBe(Result.success(listOf(news)))
        }

        useCase.execute()

        job.cancelAndJoin()
    }
}