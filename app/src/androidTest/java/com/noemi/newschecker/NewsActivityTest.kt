package com.noemi.newschecker

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.noemi.newschecker.screens.news.NewsActivity
import com.noemi.newschecker.screens.news.NewsApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<NewsActivity>()

    @Before
    fun setupNewsApp() {
        composeRule.activity.setContent {
            NewsApp()
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


