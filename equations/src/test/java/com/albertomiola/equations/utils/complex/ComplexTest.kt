package com.albertomiola.equations.utils.complex

import com.albertomiola.equations.utils.fraction.Fraction
import kotlin.math.*
import kotlin.test.*

internal class ComplexTest {
    @Test
    fun complexNumberConstructorsTests() {
        assertEquals(Complex(), Complex(0.0, 0.0))
        assertEquals(Complex(2), Complex(2.0, 0.0))
        assertEquals(Complex(3, -5), Complex(3.0, -5.0))
        assertEquals(Complex.i(), Complex(0, 1))

        val complex = Complex(7.34, -9.1)
        val (real, imaginary) = complex
        assertEquals(real, 7.34)
        assertEquals(imaginary, -9.1)
    }

    @Test
    fun complexNumberFactories() {
        val fromFraction = Complex.createFromFraction(
            Fraction(3, 5),
            Fraction(1, 4),
        )
        assertEquals(fromFraction.real, 0.6)
        assertEquals(fromFraction.imaginary, 0.25)

        val fromPolar = Complex.createFromPolarCoordinates(
            r = 2.0,
            theta = 60.0,
            angleInRadians = false,
        )
        assertEquals(fromPolar.real.roundToInt(), 1)
        assertEquals(fromPolar.imaginary, sqrt(3.0))
    }

    @Test
    fun polarCoordinatesConversions() {
        val fromPolar = Complex.createFromPolarCoordinates(
            r = 2.0,
            theta = 60.0,
            angleInRadians = false,
        )
        assertEquals(fromPolar.real.roundToInt(), 1)
        assertEquals(fromPolar.imaginary, sqrt(3.0))

        val fromPolar2 = Complex.createFromPolarCoordinates(
            r = sqrt(2.0),
            theta = PI / 4,
        )
        assertEquals(fromPolar2.real, 1.0, absoluteTolerance = 1.0e-4)
        assertEquals(fromPolar2.imaginary, 1.0, absoluteTolerance = 1.0e-4)

        // To polar
        val toPolar = Complex(5, -7).toPolarCoordinates()
        assertEquals(toPolar.r, sqrt(74.0))
        assertEquals(toPolar.phiDegrees, -54.4623, absoluteTolerance = 1.0e-4)
    }

    @Test
    fun printingValues() {
        assertEquals("${Complex(-2, 6)}", "-2 + 6i")
        assertEquals("${Complex(-2, -6)}", "-2 - 6i")
        assertEquals("${Complex(-2.1, 6.3)}", "-2.1 + 6.3i")

        assertEquals("${Complex.i()}", "1i")
        assertEquals("${Complex(0, -8)}", "-8i")

        assertEquals(
            Complex(0.5, 3.2).toStringAsFraction(),
            "1/2 + 16/5i",
        )
        assertEquals(
            Complex(-0.5, -3.2).toStringAsFraction(),
            "-1/2 - 16/5i",
        )
        assertEquals(
            Complex(0.5, -3.2).toStringAsFraction(),
            "1/2 - 16/5i",
        )
    }

    @Test
    fun objectsEquality() {
        assertEquals(Complex(3, 12), Complex(3, 12))
        assertNotEquals(Complex(3, 12), Complex(3.001, 12.0))
        assertNotEquals(Complex(4, 12), Complex(5.1, 6.2))

        assertEquals(Complex(3, 12).hashCode(), Complex(3, 12).hashCode())
        assertNotEquals(Complex(3, 12).hashCode(), Complex(-3, 12).hashCode())
    }

    @Test
    fun valueComparisons() {
        assertEquals(Complex(2, 1).compareTo(Complex(3, 7)), -1)
        assertEquals(Complex(3, 7).compareTo(Complex(2, 1)), 1)
        assertEquals(Complex(2.3, 5.437).compareTo(Complex(2.3, 5.437)), 0)
    }

    @Test
    fun copyMethod() {
        val complex = Complex(8, -11)

        assertEquals(complex, complex.copy())
        assertEquals(complex, complex.copy(8.0, -11.0))
        assertEquals(complex, complex.copy(imaginary = -11.0))

        assertNotEquals(complex, complex.copy(real = 1.0))
        assertNotEquals(complex, complex.copy(imaginary = -1.0))
        assertNotEquals(complex, complex.copy(8.000, -11.0001))
    }

    @Test
    fun classMethods() {
        assertEquals(Complex(3, 7).conjugate(), Complex(3, -7))
        assertEquals(Complex(2, 1).reciprocal(), Complex(0.4, -0.2))
        assertFails { Complex().reciprocal() }
        assertEquals(Complex(3, 7).abs(), sqrt(58.0))
        assertEquals(Complex(1, -2).abs(), sqrt(5.0))

        val complexSqrt = Complex(5, 1).sqrt()
        assertEquals(complexSqrt.real, 2.2471, absoluteTolerance = 1.0e-4)
        assertEquals(complexSqrt.imaginary, 0.2225, absoluteTolerance = 1.0e-4)

        val fifthSqrt = Complex(5, 1).nthRoot(5)
        assertEquals(fifthSqrt.real, 1.3840, absoluteTolerance = 1.0e-4)
        assertEquals(fifthSqrt.imaginary, 0.0546, absoluteTolerance = 1.0e-4)

        val negativeSqrt = Complex(5, 1).nthRoot(-2)
        assertEquals(negativeSqrt.real, 0.4406, absoluteTolerance = 1.0e-4)
        assertEquals(negativeSqrt.imaginary, -0.0436, absoluteTolerance = 1.0e-4)

        val sqrt1 = Complex(5, 1).nthRoot(1)
        assertEquals(sqrt1.real, 5.0)
        assertEquals(sqrt1.imaginary, 1.0)

        val pow1 = Complex(2, 7).pow(4.0)
        assertEquals(pow1.real.roundToInt(), 1241)
        assertEquals(pow1.imaginary.roundToInt(), -2520)

        val pow2 = Complex(2, 7).pow(4.0)
        assertEquals(pow2.real.roundToInt(), 1241)
        assertEquals(pow2.imaginary.roundToInt(), -2520)

        val pow3 = Complex(2, -1).pow(2.0)
        assertEquals(pow3.real.roundToInt(), 3)
        assertEquals(pow3.imaginary.roundToInt(), -4)

        val negation = Complex(3, -5)
        assertEquals(-negation, Complex(-3, 5))
        assertEquals(-(-negation), negation)

        val negativePhase = Complex(-0.5, -1.0)
        assertEquals(negativePhase.phase(), -2.0344, absoluteTolerance = 1.0e-4)
    }

    @Test
    fun operatorOverloads() {
        val value = Complex(3, -5) + Complex(-8, 13)
        assertEquals(value.real, -5.0)
        assertEquals(value.imaginary, 8.0)

        val value2 = Complex(5) + Complex(0, -16)
        assertEquals(value2.real, 5.0)
        assertEquals(value2.imaginary, -16.0)

        val value3 = Complex(3, -5) - Complex(-8, 13)
        assertEquals(value3.real, 11.0)
        assertEquals(value3.imaginary, -18.0)

        val value4 = Complex(5) - Complex(0, -16)
        assertEquals(value4.real, 5.0)
        assertEquals(value4.imaginary, 16.0)

        val value5 = Complex(3, -5) * Complex(-8, 13)
        assertEquals(value5.real, 41.0)
        assertEquals(value5.imaginary, 79.0)

        val value6 = Complex(5) * Complex(0, -16)
        assertEquals(value6.real, 0.0)
        assertEquals(value6.imaginary, -80.0)

        val value7 = Complex(3, -5) / Complex(-8, 13)
        val realValue = Fraction(-89, 233).toDouble()
        val imagValue = Fraction(1, 233).toDouble()

        assertEquals(value7.real, realValue, absoluteTolerance = 1.0e-5)
        assertEquals(value7.imaginary, imagValue, absoluteTolerance = 1.0e-5)
    }
}