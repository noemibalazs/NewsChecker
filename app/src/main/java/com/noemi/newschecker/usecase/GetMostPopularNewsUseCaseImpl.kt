package com.noemi.newschecker.usecase

import com.noemi.newschecker.model.NetworkResult
import com.noemi.newschecker.model.News
import com.noemi.newschecker.repository.NewYorkTimesRepository
import javax.inject.Inject

class GetMostPopularNewsUseCaseImpl @Inject constructor(private val gitHubRepository: NewYorkTimesRepository) : GetMostPopularNewsUseCase {

    override suspend fun execute(): Result<List<News>> = when (val news = gitHubRepository.getMostPopularNews()) {
        is NetworkResult.Success -> Result.success(news.data.results)
        is NetworkResult.Error -> Result.failure(Throwable(news.message))
        is NetworkResult.Failure -> Result.failure(news.error)
        else -> Result.failure(Throwable("Illegal state exception"))
    }
}