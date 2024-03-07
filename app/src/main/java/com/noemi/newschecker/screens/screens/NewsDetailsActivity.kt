package com.noemi.newschecker.screens.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.noemi.newschecker.screens.composables.NewsDetailsApp
import com.noemi.newschecker.ui.theme.NewsTheme

class NewsDetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val url = intent.getStringExtra(KEY_URL) ?: FALLBACK_URL
                    NewsDetailsApp(url)
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {}


    companion object {
        const val KEY_URL = "key_url"
        const val FALLBACK_URL = "https://www.nytimes.com/2024/02/28/style/saint-laurent-dior-paris-fashion-week.html"
    }
}
