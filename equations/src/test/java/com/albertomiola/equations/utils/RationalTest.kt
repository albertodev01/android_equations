package com.albertomiola.equations.utils

import com.albertomiola.equations.utils.fraction.Fraction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class RationalTest {
    @Test
    fun testFractionConversion() {
        assertEquals(Rational.tryParse("-1/2"), Fraction(-1, 2))
        assertEquals(Rational.tryParse("1/2"), Fraction(1, 2))
        assertEquals(Rational.tryParse("1"), Fraction(1))
        assertEquals(Rational.tryParse("-1"), Fraction(-1))
        assertEquals(Rational.tryParse("0/1"), Fraction(0, 1))
        assertEquals(Rational.tryParse("-0/1"), Fraction(0, 1))
        assertEquals(Rational.tryParse("0"), Fraction(0))

        assertNull(Rational.tryParse(""))
        assertNull(Rational.tryParse("1/"))
        assertNull(Rational.tryParse("/2"))
        assertNull(Rational.tryParse("1/-2"))
        assertNull(Rational.tryParse("-1/-2"))
        assertNull(Rational.tryParse("1/0"))
        assertNull(Rational.tryParse("/0"))
    }
}