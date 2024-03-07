package com.noemi.newschecker.screens.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.noemi.newschecker.model.News
import com.noemi.newschecker.usecase.GetMostPopularNewsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getMostPopularNewsUseCase: GetMostPopularNewsUseCase
) : ViewModel() {

    private var _currentDateState = MutableStateFlow(String())
    val currentDateState = _currentDateState.asStateFlow()

    private var _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    private var _errorState = MutableSharedFlow<ErrorState>()
    val errorState = _errorState.asSharedFlow()


    init {
        fetchPopularNews()
        setCurrentDate()
    }

    fun fetchPopularNews() {
        viewModelScope.launch {

            _newsState.update {
                it.copy(isLoading = true)
            }

            getMostPopularNewsUseCase.execute()
                .onFailure {
                    _newsState.update { newsState ->
                        newsState.copy(isLoading = false)
                    }
                    _errorState.emit(ErrorState(true, it.message ?: "Error while fetching news"))
                }
                .onSuccess { news ->
                    _newsState.update { newsState ->
                        newsState.copy(
                            isLoading = false,
                            news = news
                        )
                    }

                }
        }
    }

    private fun setCurrentDate() {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val formattedDate = LocalDateTime.now().format(formatter)

        viewModelScope.launch {
            _currentDateState.value = formattedDate
        }
    }

    data class ErrorState(
        val isShow: Boolean = false,
        val message: String = ""
    )

    data class NewsState(
        val news: List<News> = emptyList(),
        val isLoading: Boolean = false
    )

    companion object {
        private const val DATE_PATTERN = "EEEE, MMM dd, yyyy"
    }
}