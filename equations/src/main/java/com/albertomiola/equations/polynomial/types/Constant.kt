package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

/**
 * Concrete implementation of [PolynomialEquation] that represents a constant value [a]. It can be
 * either real or complex. For example:
 *
 * - f(x) = 5
 * - f(x) = 3 + 6i
 *
 * In the context of a polynomial with one variable, the non-zero constant function is a polynomial
 * of degree 0.
 *
 * @property a The only coefficient of the polynomial
 *
 * @author Alberto Miola
 * */
class Constant(val a: Complex = Complex()) : PolynomialEquation(listOf(a)) {
    override val degree: Double
        get() = if (a.isZero) Double.NEGATIVE_INFINITY else 0.0

    override fun discriminant() = Complex(Double.NaN, Double.NaN)

    override fun derivative() = Constant(Complex())

    override fun roots() = listOf<Complex>()

    fun copy(a: Complex? = null) = Constant(a ?: this.a)
}