package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

/**
 * Concrete implementation of [PolynomialEquation] that represents a first degree polynomial
 * equation in the form `ax + b = 0`.
 *
 * This equation has exactly 1 solution, which can be real or complex.
 *
 * @constructor These are examples of linear equations, where the coefficient with the highest degree
 * goes first. For example:
 *
 * ```kotlin
 * // f(x) = 2x + 5
 * val eq = Linear(
 *   a = Complex(2),
 *   b = Complex(5)
 * )
 *
 * // f(x) = x - 3
 * val eq = Linear(
 *   a = Complex(-1),
 *   b = Complex(3),
 * )
 * ```
 *
 * @property a The first coefficient of the equation in the form _f(x) = ab + b_.
 * @property b The second coefficient of the equation in the form _f(x) = ab + b_.
 *
 * @author Alberto Miola
 * */
class Linear(val a: Complex, val b: Complex) : PolynomialEquation(listOf(a, b)) {
    override val degree: Double get() = 1.0

    override fun discriminant() = Complex(1.0)

    override fun derivative() = Constant(a)

    override fun roots() = listOf(b.negate() / a)

    fun copy(a: Complex?, b: Complex?) = Linear(a ?: this.a, b ?: this.b)
}