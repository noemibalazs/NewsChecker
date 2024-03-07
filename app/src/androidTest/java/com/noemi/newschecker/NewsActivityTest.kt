package com.noemi.newschecker

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import com.noemi.newschecker.screens.composables.NewsApp
import com.noemi.newschecker.screens.screens.NewsActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<NewsActivity>()

    @Before
    fun setupNewsApp() {
        composeRule.activity.setContent {
            NewsApp(loginViewModel = hiltViewModel())
        }
    }

    @Test
    fun testAppbarProgressbarAreDisplayed() {
        composeRule.onNodeWithStringId(R.string.label_popular_news).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.label_news_icon_content_description)).assertIsDisplayed()
        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_progress_indicator_tag)).assertIsDisplayed()
    }

    @Test
    fun testNewsAreDisplayed() {
        composeRule.waitUntil {
            composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_lazy_column_tag)).isDisplayed()
        }

        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_lazy_column_tag))
            .onChildren()[3]
            .assertIsDisplayed()
            .performClick()
    }
}


