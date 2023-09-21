package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.utils.complex.Complex
import com.albertomiola.equations.utils.fraction.Fraction
import kotlin.test.*

internal class CubicTest {
    @Test
    fun objectConstructionTest() {
        val equation = Cubic(
            a = Complex(-1),
            c = Complex(5),
            d = Complex(-9)
        )

        assertEquals(equation.degree, 3.0)
        assertEquals(equation.derivative(), Quadratic(a = Complex(-3), c = Complex(5)))
        assertEquals(equation.discriminant(), Complex(-1687))
        assertEquals(equation.roots().size, 3)
        assertTrue(equation.isRealEquation)
        assertContentEquals(
            equation.coefficients,
            listOf(Complex(-1), Complex(), Complex(5), Complex(-9)),
        )

        // Coefficients
        assertEquals(equation.coefficient(3), Complex(-1))
        assertEquals(equation.coefficient(2), Complex())
        assertEquals(equation.coefficient(1), Complex(5))
        assertEquals(equation.coefficient(0), Complex(-9))
        assertNull(equation.coefficient(6))

        // Converting to string
        assertEquals(equation.toString(), "f(x) = -1x^3 + 5x + -9")
        assertEquals(equation.toStringWithFractions(), "f(x) = -1x^3 + 5x + -9")

        // No discriminant
        val solutions = equation.roots()
        assertEquals(solutions[2].real, 1.42759826966, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[2].real, -1.055514309999, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[0].real, 1.42759826966, absoluteTolerance = 1.0e-4)
        assertEquals(solutions[0].imaginary, 1.055514309999, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[1].real, -2.855196539321, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[1].imaginary, 0.0)

        // Evaluation
        assertEquals(equation.evaluateOn(2), Complex(3, 7))
    }

    @Test
    fun specialCaseZero() {
        assertFails { Cubic(Complex()) }
    }

    @Test
    fun objectsComparisons() {
        val fx = Cubic(
            Complex(2, -3),
            Complex.createFromFraction(Fraction(6, 5), Fraction(1)),
            Complex(5, -1),
            Complex(-6, -6),
        )
        val otherFx = Cubic(
            Complex(2, -3),
            Complex.createFromFraction(Fraction(6, 5), Fraction(1)),
            Complex(5, -1),
            Complex(-6, -6),
        )

        assertEquals(fx, otherFx)
        assertEquals(fx.hashCode(), otherFx.hashCode())

        assertNotEquals(
            fx,
            Cubic(
                Complex(1, -3),
                Complex.createFromFraction(Fraction(6, 5), Fraction(1)),
                Complex(5, -1),
                Complex(-6, -6),
            ),
        )
        assertNotEquals(
            fx,
            Cubic(
                Complex(2, -3),
                Complex.createFromFraction(Fraction(6, 5), Fraction(1)),
                Complex(-5, -1),
                Complex(-6, -6),
            ),
        )
        assertNotEquals(
            fx,
            Cubic(
                Complex(2, -3),
                Complex(),
                Complex(5, -1),
                Complex(-6, -6),
            ),
        )
        assertNotEquals(
            fx,
            Cubic(
                Complex(2, -3),
                Complex.createFromFraction(Fraction(6, 5), Fraction(1)),
                Complex(5, -1),
                Complex(-6.0, -6.0001),
            ),
        )
    }

    @Test
    fun copyMethod() {
        val cubic = Cubic(a = Complex(2, -3), d = Complex(-18))

        assertEquals(cubic.copy(), cubic)
        assertEquals(
            cubic.copy(
                a = Complex(2, -3),
                b = Complex(),
                c = Complex(),
                d = Complex(-18),
            ), cubic
        )
        assertNotEquals(
            cubic.copy(
                a = Complex(-2, -3),
                b = Complex(),
                c = Complex(),
                d = Complex(-18),
            ), cubic
        )
        assertNotEquals(cubic.copy(b = Complex(3)), cubic)
    }
}