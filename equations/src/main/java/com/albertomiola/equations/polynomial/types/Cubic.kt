package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex
import kotlin.math.sqrt

/**
 * Concrete implementation of [PolynomialEquation] that represents a third degree polynomial
 * equation in the form `ax^3 + bx^2 + cx + d = 0`.
 *
 * This equation has 3 solutions, which can be combined as follows:
 *  - 3 distinct real roots and 0 complex roots;
 *  - 3 real roots (some of them are equal) and 0 complex roots;
 *  - 1 real root and 2 complex conjugate roots.
 *
 * The above cases depend on the value of the discriminant.
 *
 * @constructor These are examples of cubic equations, where the coefficient with the highest degree
 * goes first. For example:
 *
 * ```kotlin
 * // f(x) = 2x^3 + x^2 + (5 + 6i)
 * val eq = Cubic(
 *   a = Complex(2),
 *   b = Complex(1)
 *   d = Complex(5, 6)
 * )
 *
 * // f(x) = x^3 + (-2 + 6i)x
 * val eq = Cubic(
 *   a = Complex(1),
 *   c = Complex(-2, 6)
 * )
 * ```
 *
 * @property a The first coefficient of the equation in the form _f(x) = ax^3 + bx^2 + cx + d = 0_
 * @property b The second coefficient of the equation in the form _f(x) = ax^3 + bx^2 + cx + d = 0_
 * @property c The third coefficient of the equation in the form _f(x) = ax^3 + bx^2 + cx + d = 0_
 * @property d The fourth coefficient of the equation in the form _f(x) = ax^3 + bx^2 + cx + d = 0_
 *
 * @author Alberto Miola
 * */
class Cubic(
    val a: Complex = Complex(1),
    val b: Complex = Complex(),
    val c: Complex = Complex(),
    val d: Complex = Complex(),
) : PolynomialEquation(
    listOf(a, b, c, d)
) {
    override val degree: Double
        get() = 3.0

    override fun discriminant(): Complex {
        val p1 = c * c * b * b
        val p2 = d * b * b * b * Complex(4)
        val p3 = c * c * c * a * Complex(4)
        val p4 = a * b * c * d * Complex(18)
        val p5 = d * d * a * a * Complex(27)

        return p1 - p2 - p3 + p4 - p5
    }

    override fun derivative() = Quadratic(
        a * Complex(3),
        b * Complex(2),
        c,
    )

    override fun roots(): List<Complex> {
        val two = Complex(2)
        val three = Complex(3)
        val sigma = Complex(-1.0 / 2.0, 1.0 / 2.0 * sqrt(3.0))

        val d0 = b * b - a * c * three
        val d1 = (b.pow(3.0) * two) - (a * b * c * Complex(9)) + (a * a * d * Complex(27))
        val sqrtD = (discriminant() * a * a * Complex(-27)).sqrt()
        val valC = ((d1 + sqrtD) / two).nthRoot(3)
        val constTerm = Complex(-1) / (a * three)

        return if (valC.isZero) {
            listOf(
                constTerm * (b + valC),
                constTerm * (b + (valC * sigma)),
                constTerm * (b + (valC * sigma.pow(2.0))),
            )
        } else {
            listOf(
                constTerm * (b + valC + (d0 / valC)),
                constTerm * (b + (valC * sigma) + (d0 / (valC * sigma))),
                constTerm * (b + (valC * sigma.pow(2.0)) + (d0 / (valC * sigma.pow(2.0)))),
            )
        }
    }

    fun copy(a: Complex? = null, b: Complex? = null, c: Complex? = null, d: Complex? = null) =
        Cubic(
            a ?: this.a,
            b ?: this.b,
            c ?: this.c,
            d ?: this.d,
        )
}