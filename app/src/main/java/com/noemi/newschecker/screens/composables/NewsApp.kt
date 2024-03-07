package com.noemi.newschecker.screens.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.noemi.newschecker.screens.viewmodel.NewsViewModel
import com.noemi.newschecker.R
import com.noemi.newschecker.utils.NewsAppBar

@Composable
fun NewsApp(loginViewModel: NewsViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        NewsAppBar(title = stringResource(id = R.string.label_popular_news), contentDescription = stringResource(id = R.string.label_news_icon_content_description))

        NewsScreen(loginViewModel)
    }
}
