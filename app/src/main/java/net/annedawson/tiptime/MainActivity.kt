package net.annedawson.tiptime

/*
Date: Friday 20th January 2023, 13:57 PT
Programmer: Anne Dawson
App: Tip Time
File: MainActivity.kt
Purpose: Introduction to state in Compose
From: https://developer.android.com/codelabs/basic-android-kotlin-compose-using-state?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-2-pathway-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-using-state#0
Status: Completed to end of Unit 2, Pathway 3, Part 5
        Part 5 Write an instrumentation (UI) test
https://developer.android.com/codelabs/basic-android-kotlin-compose-write-automated-tests?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-2-pathway-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-write-automated-tests#4
*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.annedawson.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen() {

    var amountInput by remember {mutableStateOf("")}
    // The String amountInput is observable mutable state.
    // Initially, the value of amountInput is an empty string
    // amountInput state was hoisted from EditNumberField and then passed to EditNumberField

    // remember and mutableStateOf are functions (that you can step into using the debugger)

    var tipInput by remember { mutableStateOf("") }

    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    val amount = amountInput.toDoubleOrNull() ?: 0.0  //  convert String to Double
    // toDoubleOrNull() parses the string to a Double number
    // and returns the result or null if the string is not a valid representation of a number.
    // ?: is the Elvis operator
    // The Elvis operator permits the assignment of what's on the left, but if it's null, assign what's on the right



    val focusManager = LocalFocusManager.current  // Unit 6: Set keyboard actions

    var roundUp by remember { mutableStateOf(false) }
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(modifier = Modifier.padding(32.dp),verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(  // Unit 6: Set keyboard actions
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            // import androidx.compose.ui.focus.FocusDirection
            ),
            value = amountInput,  // value is what is displayed in the Textbox, refreshed after the event below
            onValueChange = { amountInput = it }
        )
            // Note that the "it" parameter holds the updated value in the text box
            // and is used to update the state (amountInput), which triggers recomposition i.e.
            // re-calling the composables that use that state.

            // If the user types a single character into the text box, the value in the text box changes.
            // That updated value is the "it" parameter.
            // Any input to the text box triggers the event to place the current value (it)
            // into the amountInput state, then as the state is observable,
            // the composable is run again with the new data, so that the UI is redrawn with the new data.
        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(  // Unit 6: Set keyboard actions
                onDone = { focusManager.clearFocus() }),
            value = tipInput,
            onValueChange = { tipInput = it }
        )
        RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it })
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.tip_amount, tip), // the string tip is substituted for %s in tip_amount
            // The above is an example of string formatting.
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Composable
fun EditNumberField(
    @StringRes label: Int,  // annotation  denote to that the label parameter is expected to be a string resource reference
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,  // to used in the TextField
    onValueChange: (String) -> Unit,  // to be used in the TextField
    //modifier: Modifier = Modifier
)
{
    TextField(
        value = value,  // the amountInput state is passed to TextField value
        // a TextField's value is the string displayed in its text box on the UI
        onValueChange = onValueChange,  // onValueChange is a callback lambda event -
        // i.e. what must occur when the value changes.
        // "it" is the updated text the text box.
        // If you type in 123, "it" is first "1", then "12", then "123".
        // Set a break point (for line and lambda) on the line: var amountInput by ... above.
        // Run through the debugger and monitor the values of variables at the point of app suspension.
        // Press Resume program button to resume.
        // You can see the value of "it" change as the program runs.
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged:   (Boolean) -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(  // import androidx.compose.material.Switch
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            )
        )
    }
}
// change "private" to "internal" to allow module access for local testing, see:
// https://developer.android.com/codelabs/basic-android-kotlin-compose-write-automated-tests?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-2-pathway-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-write-automated-tests#3
// private fun calculateTip( amount: Double, tipPercent: Double = 15.0, roundUp: Boolean ): String {

@VisibleForTesting
// This makes the method public, but indicates to others that it's only public for testing purposes.
internal fun calculateTip( amount: Double, tipPercent: Double = 15.0, roundUp: Boolean ): String {
    var tip = tipPercent / 100 * amount
    if (roundUp)
        tip = kotlin.math.ceil(tip)
    return NumberFormat.getCurrencyInstance().format(tip) // convert the number to a currency formatted string
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}

