package com.albertomiola.equations.utils.fraction

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FractionExtensionTest {
    @Test
    fun fractionExtensionOnIntDouble() {
        assertEquals(13.toFraction(), Fraction(13))
        assertEquals((-3).toFraction(), Fraction(-3))
        assertEquals(0.toFraction(), Fraction(0))

        assertEquals(8.46.toFraction(), Fraction(423, 50))
        assertEquals((-3.9).toFraction(), Fraction(-39, 10))
        assertEquals(0.0.toFraction(), Fraction(0))

        assertFails { Double.NaN.toFraction() }
        assertFails { Double.POSITIVE_INFINITY.toFraction() }
        assertFails { Double.NEGATIVE_INFINITY.toFraction() }
    }

    @Test
    fun fractionExtensionOnString() {
        assertEquals("1/3".toFraction(), Fraction(1, 3))
        assertEquals("-4/5".toFraction(), Fraction(-4, 5))
        assertEquals("5".toFraction(), Fraction(5))
        assertEquals("-5".toFraction(), Fraction(-5))
        assertEquals("0".toFraction(), Fraction(0))

        assertFails { "1/".toFraction() }
        assertFails { "1/0".toFraction() }
        assertFails { "x".toFraction() }
        assertFails { "1/-3".toFraction() }
        assertFails { "".toFraction() }
    }

    @Test
    fun fractionExtensionRecognizer() {
        assertTrue("3/5".isFraction)
        assertTrue("-3/5".isFraction)
        assertTrue("6".isFraction)
        assertTrue("-1".isFraction)

        assertFalse("/2".isFraction)
        assertFalse("-1/".isFraction)
        assertFalse("1/-2".isFraction)
    }
}