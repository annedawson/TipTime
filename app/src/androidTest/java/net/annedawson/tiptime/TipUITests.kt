package net.annedawson.tiptime

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import net.annedawson.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test

class TipUITests {
    @get:Rule
    val composeTestRule = createComposeRule()

    // The compiler knows that methods annotated with @Test annotation
    // in the androidTest directory refer to instrumentation tests
    // and methods annotated with @Test annotation in the test directory
    // refer to local tests.
    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {  // This sets the UI content of the composeTestRule.
            TipTimeTheme {
                TipTimeScreen()
            }
        }
        // The code above should look similar to the code written
        // to set the content in the onCreate() method in the MainActivity.kt file.
        composeTestRule.onNodeWithText("Bill amount").performTextInput("10")
        composeTestRule.onNodeWithText("Tip (%)").performTextInput("20")
        composeTestRule.onNodeWithText("Tip amount: $2.00").assertExists()

        // Make sure "Bill amount", "Tip (%)" and "Tip amount: $2.00"
        // ***EXACTLY*** matches the text shown on the UI when it runs normally.
        // Try changing "Bill amount" above to "Bill Amount" to see the error.
    }
}

