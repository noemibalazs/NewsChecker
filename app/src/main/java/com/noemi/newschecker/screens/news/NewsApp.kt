package com.noemi.newschecker.screens.news

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.noemi.newschecker.R
import com.noemi.newschecker.model.News
import com.noemi.newschecker.screens.details.NewsDetailsActivity
import com.noemi.newschecker.utils.NewsAppBar

@Composable
fun NewsApp(modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxSize()) {
        NewsAppBar(title = stringResource(id = R.string.label_popular_news), contentDescription = stringResource(id = R.string.label_news_icon_content_description))

        NewsScreen()
    }
}

@Composable
private fun NewsScreen(modifier: Modifier = Modifier) {
    val loginViewModel = hiltViewModel<NewsViewModel>()
    val newsState = loginViewModel.newsState.collectAsState()
    val errorState = loginViewModel.errorState.collectAsState(NewsViewModel.ErrorState())
    val currentDate = loginViewModel.currentDateState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        ScreenRoot(newsState = newsState.value, currentDate = currentDate.value)

        LaunchedEffect(key1 = errorState.value) {
            if (errorState.value.isShow && errorState.value.message.isNotBlank()) {
                Toast.makeText(context, errorState.value.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
private fun ScreenRoot(newsState: NewsViewModel.NewsState, currentDate: String, modifier: Modifier = Modifier) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (progressIndicator, news) = createRefs()

        when (newsState.isLoading) {
            true -> CircularProgressIndicator(
                modifier = modifier
                    .constrainAs(progressIndicator) {
                        linkTo(parent.top, parent.bottom)
                        linkTo(parent.start, parent.end)
                    }
                    .testTag(stringResource(id = R.string.label_progress_indicator_tag)),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeWidth = 3.dp,
            )

            else -> LazyColumn(
                modifier = modifier
                    .constrainAs(news) {
                        linkTo(parent.top, parent.bottom)
                        linkTo(parent.start, parent.end)
                    }
                    .testTag(stringResource(id = R.string.label_lazy_column_tag))
            ) {
                item {
                    NewsHeader(date = currentDate)
                }
                items(
                    items = newsState.news,
                    key = { it.id }
                ) { item ->
                    NewsItemRow(news = item)
                }
            }
        }
    }
}

@Composable
private fun NewsHeader(date: String) {
    Text(
        text = date,
        style = MaterialTheme.typography.titleMedium
    )

    Text(
        text = stringResource(id = R.string.label_todays_paper),
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun NewsItemRow(news: News, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = MaterialTheme.shapes.large,
            modifier = modifier
                .padding(8.dp)
                .clickable { navigateToNewsDetails(url = news.url, context = context) }
        ) {
            Column {

                Text(
                    text = news.title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, end = 6.dp, start = 6.dp),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = news.abstract,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 6.dp),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = news.byline,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 6.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = news.published_date,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 6.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun navigateToNewsDetails(context: Context, url: String) {
    val intent = Intent(context, NewsDetailsActivity::class.java)
    intent.putExtra(NewsDetailsActivity.KEY_URL, url)
    context.startActivity(intent)
}