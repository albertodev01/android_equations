package com.albertomiola.equations.utils.fraction

import kotlin.test.*

internal class FractionTest {
    @Test
    fun constructors() {
        val fraction = Fraction(5, 7)
        assertEquals(fraction.numerator, 5)
        assertEquals(fraction.denominator, 7)
        assertEquals(fraction.toString(), "5/7")

        assertEquals(Fraction(-5, 7).numerator, -5)
        assertEquals(Fraction(-5, 7).denominator, 7)
        assertEquals(Fraction(-5, 7).toString(), "-5/7")
        assertEquals(Fraction(5, -7).numerator, -5)
        assertEquals(Fraction(5, -7).denominator, 7)
        assertEquals(Fraction(5, -7).toString(), "-5/7")

        assertFails { Fraction(1, 0) }

        val (num, den) = fraction
        assertEquals(num, 5)
        assertEquals(den, 7)
    }

    @Test
    fun factoryConstructors() {
        assertEquals(Fraction.createFromString("3/5"), Fraction(3, 5))
        assertEquals(Fraction.createFromString("-3/5"), Fraction(-3, 5))
        assertEquals(Fraction.createFromString("5"), Fraction(5))
        assertEquals(Fraction.createFromString("-8"), Fraction(-8))

        assertFails { Fraction.createFromString("5/0") }
        assertFails { Fraction.createFromString("5/-1") }
        assertFails { Fraction.createFromString("0/0") }
        assertFails { Fraction.createFromString("3/") }
        assertFails { Fraction.createFromString("/2") }
        assertFails { Fraction.createFromString("3/-") }

        assertEquals(Fraction.createFromDouble(5.6), Fraction(28, 5))
        assertEquals(Fraction.createFromDouble(0.0025), Fraction(1, 400))
        assertEquals(Fraction.createFromDouble(-3.8), Fraction(-19, 5))
        assertEquals(Fraction.createFromDouble(0.0), Fraction(0, 1))

        assertFails { Fraction.createFromDouble(Double.NaN) }
        assertFails { Fraction.createFromDouble(Double.POSITIVE_INFINITY) }
        assertFails { Fraction.createFromDouble(Double.NEGATIVE_INFINITY) }
    }

    @Test
    fun objectsEquality() {
        assertEquals(Fraction(3, 12), Fraction(1, 4))
        assertEquals(Fraction(6, 13), Fraction(6, 13))
        assertNotEquals(Fraction(3, 12), Fraction(2, 4))
        assertNotEquals(Fraction(3, 12), Fraction(-1, 4))

        assertEquals(Fraction(6, 13).hashCode(), Fraction(6, 13).hashCode())
        assertNotEquals(Fraction(3, 12).hashCode(), Fraction(2, 4).hashCode())
        assertNotEquals(Fraction(3, 12).hashCode(), Fraction(-1, 4).hashCode())
    }

    @Test
    fun classMethods() {
        assertEquals(Fraction(10, 2).toDouble(), 5.0)
        assertEquals(Fraction(-6, 8).toDouble(), -0.75)

        val fraction = Fraction(3, 7)
        assertTrue(fraction.isProper)
        assertFalse(fraction.isImproper)
        assertTrue(fraction.inverse().isImproper)
        assertFalse(fraction.inverse().isProper)

        assertEquals(Fraction(10, 2).inverse(), Fraction(2, 10))
        assertEquals(Fraction(-10, 2).inverse(), Fraction(-2, 10))
        assertEquals(Fraction(-10, 2).negate(), Fraction(10, 2))
        assertEquals(Fraction(2, 4).negate(), Fraction(-1, 2))

        assertFalse(Fraction(2, 4).isNegative)
        assertFalse(Fraction(2, 4).isWhole)

        assertEquals(Fraction(16, 46).reduce(), Fraction(8, 23))
        assertEquals(Fraction(-9, 3).reduce(), Fraction(-3))
    }

    @Test
    fun operators() {
        val fraction1 = Fraction(7, 13)
        val fraction2 = Fraction(-4, 3)

        assertEquals(fraction1 + fraction2, Fraction(-31, 39))
        assertEquals(fraction1 - fraction2, Fraction(73, 39))
        assertEquals(fraction1 * fraction2, Fraction(-28, 39))
        assertEquals(fraction1 / fraction2, Fraction(-21, 52))

        assertTrue(Fraction(10) > Fraction(8))
        assertTrue(Fraction(10) >= Fraction(8))
        assertTrue(Fraction(10) >= Fraction(10))
        assertTrue(Fraction(8) < Fraction(10))
        assertTrue(Fraction(8) <= Fraction(10))
        assertTrue(Fraction(8) <= Fraction(8))
    }

    @Test
    fun copy() {
        val fraction = Fraction(7, -4)

        assertEquals(fraction.copy(), fraction)
        /*assertEquals(fraction.copy(7), fraction)
        assertEquals(fraction.copy(denominator = -4), fraction)
        assertNotEquals(fraction.copy(17), fraction)
        assertEquals(fraction.copy(denominator = 4), fraction)
        assertEquals(fraction.copy(7, 4), fraction)*/
    }
}