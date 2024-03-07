package com.noemi.newschecker

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.noemi.newschecker.screens.viewmodel.NewsViewModel
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var useCase: GetMostPopularNewsUseCase

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = NewsViewModel(
            getMostPopularNewsUseCase = useCase
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

            viewModel.newsState.test {
                val result = awaitItem()

                useCase.execute().onSuccess { news ->
                    assertThat(result.isLoading).isFalse()
                    assertThat(result.news).isEqualTo(news)
                }

                cancelAndConsumeRemainingEvents()
            }
        }

        viewModel.fetchPopularNews()

        job.cancelAndJoin()
    }


    @Test
    fun `test get news and should be failure`() = runBlocking {

        val job = launch {

            assertThat(viewModel.newsState.value.isLoading).isTrue()

            viewModel.newsState.test {
                val result = awaitItem()

                useCase.execute().onFailure { throwable ->
                    assertThat(result.isLoading).isFalse()

                    viewModel.errorState.test {
                        val error = awaitItem()
                        assertThat(error.isShow).isTrue()
                        assertThat(error.message).isEqualTo(throwable.message)
                    }
                }

                cancelAndConsumeRemainingEvents()
            }
        }

        viewModel.fetchPopularNews()

        job.cancelAndJoin()
    }

}