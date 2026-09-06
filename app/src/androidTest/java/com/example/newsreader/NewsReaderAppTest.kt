package com.example.newsreader

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsreader.data.repository.FakeNewsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsReaderAppTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launch_displaysHeaderTitle() {
        val headerTitle =
            composeRule.activity.getString(R.string.news_header_title)

        composeRule
            .onNodeWithText(headerTitle)
            .assertIsDisplayed()
    }

    @Test
    fun launch_displaysNewsFromFakeRepository() {
        composeRule
            .onNodeWithText(FakeNewsRepository.DOMESTIC_TEST_TITLE)
            .assertIsDisplayed()
    }
}
