package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.utils.complex.Complex
import kotlin.test.*

internal class ConstantTest {
    @Test
    fun objectConstructionTest() {
        val equation = Constant(Complex(3, 7))

        assertEquals(equation.degree, 0.0)
        assertEquals(equation.derivative(), Constant(Complex()))
        assertEquals(equation.roots().size, 0)
        assertFalse(equation.isRealEquation)
        assertContentEquals(equation.coefficients, listOf(Complex(3, 7)))

        // Converting to string
        assertEquals(equation.toString(), "f(x) = 3 + 7i")
        assertEquals(equation.toStringWithFractions(), "f(x) = 3 + 7i")

        // No discriminant
        val discr = equation.discriminant()
        assertEquals(discr.real, Double.NaN)
        assertEquals(discr.imaginary, Double.NaN)

        // Evaluation
        assertEquals(equation.evaluateOn(2), Complex(3, 7))
    }

    @Test
    fun specialCaseZero() {
        val equation = Constant(Complex())
        assertEquals(equation.degree, Double.NEGATIVE_INFINITY)
        assertTrue { equation.isRealEquation }
    }

    @Test
    fun objectsComparisons() {
        val fx = Constant(Complex(6))

        assertEquals(Constant(Complex(6)), fx)
        assertEquals(Constant(Complex(6)).hashCode(), fx.hashCode())
        assertNotEquals(Constant(Complex(6.0001)), fx)
    }

    @Test
    fun copyMethod() {
        val constant = Constant(Complex(7.0, -8.5))

        assertEquals(constant.copy(), constant)
        assertEquals(constant.copy(Complex(7.0, -8.5)), constant)
        assertNotEquals(constant.copy(Complex(7.0, -8.4)), constant)
        assertNotEquals(constant.copy(Complex(6.0, -8.5)), constant)
    }
}