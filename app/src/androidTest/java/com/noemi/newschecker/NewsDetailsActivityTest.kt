package com.noemi.newschecker

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.noemi.newschecker.screens.details.NewsDetailsActivity
import com.noemi.newschecker.screens.details.NewsDetailsApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsDetailsActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<NewsDetailsActivity>()

    @Before
    fun setupNewsApp() {
        composeRule.activity.setContent {
            NewsDetailsApp(url = NewsDetailsActivity.FALLBACK_URL)
        }
    }


    @Test
    fun testAppbarProgressbarWebViewAreDisplayed() {
        composeRule.onNodeWithStringId(R.string.label_news_details).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.label_news_details_icon_content_description)).assertIsDisplayed()
        composeRule.waitUntil {
            composeRule
                .onNodeWithTag(composeRule.activity.getString(R.string.label_progress_indicator_tag)).isDisplayed()
        }

        composeRule.waitUntil {
            composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_web_view_tag)).isDisplayed()
        }
    }
}