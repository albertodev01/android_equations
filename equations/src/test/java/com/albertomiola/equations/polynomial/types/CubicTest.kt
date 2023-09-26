package com.albertomiola.equations.polynomial.types

import androidx.core.util.rangeTo
import com.albertomiola.equations.utils.complex.Complex
import com.albertomiola.equations.utils.fraction.Fraction
import kotlin.math.roundToLong
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
        println(solutions)
        assertEquals(solutions[0].real, 1.427598269660, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[0].imaginary, -1.055514309998, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[1].real, -2.855196539320, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[1].imaginary, 0.0, absoluteTolerance = 1.0e-1)
        assertEquals(solutions[2].real, 1.427598269660, absoluteTolerance = 1.0e-12)
        assertEquals(solutions[2].imaginary, 1.055514309998, absoluteTolerance = 1.0e-12)

        // Evaluation
        assertEquals(
            equation.evaluateOn(0.5),
            Complex.createFromFraction(Fraction(-53, 8), Fraction(0)),
        )
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

    @Test
    fun batchTests() {
        val equations = listOf(
            Cubic(
                a = Complex(2),
                b = Complex(3),
                c = Complex(-11),
                d = Complex(-6),
            ).roots(),
            Cubic(
                a = Complex.i(),
                c = Complex(-2, 5),
                d = Complex(7),
            ).roots(),
            Cubic(
                a = Complex(-4),
                c = Complex(8),
            ).roots(),
            Cubic(
                b = Complex(1),
                c = Complex(1),
                d = Complex(1),
            ).roots(),
            Cubic(
                a = Complex.i(),
                b = Complex(5, -8),
            ).roots(),
            Cubic(
                d = Complex(1),
            ).roots(),
            Cubic().roots(),
        )
        val solutions = listOf(
            listOf(
                Complex(-3),
                Complex(2),
                Complex(-0.5),
            ),
            listOf(
                Complex(0.73338, 0.97815),
                Complex(0.31133, -2.75745),
                Complex(-1.04472, 1.77929),
            ),
            listOf(
                Complex(1.41421),
                Complex(-1.41421),
                Complex(),
            ),
            listOf(
                Complex(-1),
                Complex(0, -1),
                Complex.i(),
            ),
            listOf(
                Complex(),
                Complex(),
                Complex(8, 5),
            ),
            listOf(
                Complex(-1),
                Complex(0.5, -0.86603),
                Complex(0.5, 0.86603),
            ),
            listOf(
                Complex(),
                Complex(),
                Complex(),
            ),
        )

        equations.forEachIndexed { index, value ->
            assertEquals(value[0].real, solutions[index][0].real, absoluteTolerance = 1.0e-5)
            assertEquals(
                value[0].imaginary,
                solutions[index][0].imaginary,
                absoluteTolerance = 1.0e-5,
            )
            assertEquals(value[1].real, solutions[index][1].real, absoluteTolerance = 1.0e-5)
            assertEquals(
                value[1].imaginary,
                solutions[index][1].imaginary,
                absoluteTolerance = 1.0e-5,
            )
            assertEquals(value[2].real, solutions[index][2].real, absoluteTolerance = 1.0e-5)
            assertEquals(
                value[2].imaginary,
                solutions[index][2].imaginary,
                absoluteTolerance = 1.0e-5,
            )
        }
    }
}