package com.noemi.newschecker.screens.composables

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.noemi.newschecker.R
import com.noemi.newschecker.utils.NewsAppBar

@Composable
fun NewsDetailsApp(url: String) {

    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        NewsAppBar(title = stringResource(id = R.string.label_news_details), contentDescription = stringResource(id = R.string.label_news_details_icon_content_description))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 3.dp,
                    modifier = Modifier.testTag(stringResource(id = R.string.label_progress_indicator_tag))
                )
            }

            AndroidView(
                factory = { context ->
                    WebView(context).apply {

                        settings.apply {
                            javaScriptEnabled = true
                            loadsImagesAutomatically = true
                            setSupportZoom(true)
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }

                            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                                super.onReceivedError(view, request, error)
                                isLoading = false
                            }
                        }

                        loadUrl(url)
                    }
                },
                modifier = Modifier.testTag(stringResource(id = R.string.label_web_view_tag))
            )
        }
    }
}

