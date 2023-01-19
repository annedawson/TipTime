package net.annedawson.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test

class TipCalculatorTests {
    @Test
    fun calculate_20_percent_tip_no_roundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = "$2.00"
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}
// remember that for the calculateTip(...) call to work
// in a test its definition in MainActivity.kt
// must be changed to an internal rather than
// a private function and annotated with @VisibleForTesting
