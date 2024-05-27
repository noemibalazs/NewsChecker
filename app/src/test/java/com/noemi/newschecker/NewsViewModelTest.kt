package com.noemi.newschecker

import android.app.Application
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.noemi.newschecker.screens.news.NewsViewModel
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val useCase: GetMostPopularNewsUseCase = mockk()
    private val application: Application = mockk()

    private lateinit var viewModel: NewsViewModel
    private var key = "you_key"

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = NewsViewModel(
            getMostPopularNewsUseCase = useCase,
            app = application
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get news and should be successful`() = runBlocking {
        val job = launch {

            assertThat(viewModel.newsState.value.isLoading).isTrue()

            coEvery { application.getString(R.string.new_york_times_key) } returns key

            viewModel.newsState.test {
                val result = awaitItem()

                useCase.execute(key).onSuccess { news ->
                    assertThat(result.isLoading).isFalse()
                    assertThat(result.news).isEqualTo(news)
                }

                cancelAndConsumeRemainingEvents()
            }

            coVerify { application.getString(R.string.new_york_times_key) }
        }

        viewModel.fetchPopularNews()

        job.cancelAndJoin()
    }


    @Test
    fun `test get news and should be failure`() = runBlocking {

        val job = launch {

            assertThat(viewModel.newsState.value.isLoading).isTrue()

            coEvery { application.getString(R.string.new_york_times_key) } returns key

            viewModel.newsState.test {
                val result = awaitItem()

                useCase.execute(key).onFailure { throwable ->
                    assertThat(result.isLoading).isFalse()

                    viewModel.errorState.test {
                        val error = awaitItem()
                        assertThat(error.isShow).isTrue()
                        assertThat(error.message).isEqualTo(throwable.message)
                    }
                }

                cancelAndConsumeRemainingEvents()
            }

            coVerify { application.getString(R.string.new_york_times_key) }
        }

        viewModel.fetchPopularNews()

        job.cancelAndJoin()
    }

}