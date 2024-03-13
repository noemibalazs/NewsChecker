package com.noemi.newschecker.screens.composables

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.noemi.newschecker.R
import com.noemi.newschecker.model.News
import com.noemi.newschecker.screens.screens.NewsDetailsActivity
import com.noemi.newschecker.screens.viewmodel.NewsViewModel

@Composable
fun NewsScreen(loginViewModel: NewsViewModel) {
    val newsState = loginViewModel.newsState.collectAsState()
    val errorState = loginViewModel.errorState.collectAsState(NewsViewModel.ErrorState())
    val currentDate = loginViewModel.currentDateState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        NewsScreenRoot(newsState = newsState.value, currentDate = currentDate.value)

        LaunchedEffect(key1 = errorState.value) {
            if (errorState.value.isShow && errorState.value.message.isNotBlank()) {
                Toast.makeText(context, errorState.value.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
private fun NewsScreenRoot(newsState: NewsViewModel.NewsState, currentDate: String) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (progressIndicator, news) = createRefs()

        when (newsState.isLoading) {
            true -> CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(progressIndicator) {
                        linkTo(parent.top, parent.bottom)
                        linkTo(parent.start, parent.end)
                    }
                    .testTag(stringResource(id = R.string.label_progress_indicator_tag)),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeWidth = 3.dp,
            )

            else -> LazyColumn(
                modifier = Modifier
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
private fun NewsItemRow(news: News) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
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
            modifier = Modifier
                .padding(8.dp)
                .clickable { navigateToNewsDetails(context = context, url = news.url) }
        ) {
            Column {

                Text(
                    text = news.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, end = 6.dp, start = 6.dp),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = news.abstract,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 6.dp),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = news.byline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 6.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = news.published_date,
                    modifier = Modifier
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
