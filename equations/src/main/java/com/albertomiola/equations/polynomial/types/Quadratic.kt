package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

/**
 * Concrete implementation of [PolynomialEquation] that represents a second degree polynomial
 * equation in the form `ax^2 + bx + c = 0`.
 *
 * This equation has exactly 2 roots, both real or both complex, depending on the value of the
 * discriminant.
 *
 * @constructor These are examples of quadratic equations, where the coefficient with the highest
 * degree goes first. For example:
 *
 * ```kotlin
 * // f(x) = x^2 - 6x + 5
 * val eq = Quadratic(
 *   a = Complex(2),
 *   b = Complex(-6)
 *   c = Complex(5)
 * )
 *
 * // f(x) = ix^2 - 3
 * val eq = Quadratic(
 *   a = Complex.i(),
 *   c = Complex(3)
 * )
 * ```
 *
 * @property a The first coefficient of the equation in the form _f(x) = ax^2 + bx + c = 0_
 * @property b The second coefficient of the equation in the form _f(x) = ax^2 + bx + c = 0_
 * @property c The third coefficient of the equation in the form _f(x) = ax^2 + bx + c = 0_
 *
 * @author Alberto Miola
 * */
class Quadratic(val a: Complex, val b: Complex = Complex(), val c: Complex = Complex()) :
    PolynomialEquation(listOf(a, b, c)) {
    override val degree: Double
        get() = 2.0

    override fun discriminant() = b * b - Complex(4) * a * c

    override fun derivative() = Linear(a * Complex(2), b)

    override fun roots(): List<Complex> {
        val twoA = Complex(2) * a
        val disc = discriminant()

        return listOf(
            (b.negate() + disc.sqrt()) / twoA,
            (b.negate() - disc.sqrt()) / twoA,
        )
    }

    fun copy(a: Complex?, b: Complex?, c: Complex?) = Quadratic(
        a ?: this.a,
        b ?: this.b,
        c ?: this.c,
    )
}