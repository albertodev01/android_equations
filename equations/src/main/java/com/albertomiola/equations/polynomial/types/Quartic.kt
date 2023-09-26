package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

/**
 * Concrete implementation of [PolynomialEquation] that represents a fourth degree polynomial
 * equation in the form _ax^4 + bx^3 + cx^2 + dx + e = 0_. This equation has 4 solutions, which can
 * be combined as follows:
 *
 *  - 2 distinct real roots and 2 complex conjugate roots
 *  - 4 real roots and 0 complex roots
 *  - 0 real roots and 4 complex conjugate roots
 *  - Multiple roots which can be all equal or paired (complex or real)
 *
 * The above cases depend on the value of the discriminant.
 *
 * @constructor These are examples of quartic equations, where the coefficient with the highest degree
 * goes first. For example:
 *
 * ```kotlin
 * // f(x) = -x^4 - 8x^3 - 1
 * val eq = Quartic(
 *   a = Complex(-1),
 *   b = Complex(-8)
 *   e = Complex(-1)
 * )
 *
 * // f(x) = ix^4 - ix^2 + (8 + 12i)x
 * val eq = Quartic(
 *   a = Complex.i(),
 *   c = Complex(0, -1)
 *   d = Complex(8, 12)
 * )
 * ```
 *
 * @property a The first coefficient of the equation in the form _f(x) = ax^4 + bx^3 + cx^2 + dx + e = 0_.
 * @property b The second coefficient of the equation in the form _f(x) = ax^4 + bx^3 + cx^2 + dx + e = 0_.
 * @property c The third coefficient of the equation in the form _f(x) = ax^4 + bx^3 + cx^2 + dx + e = 0_.
 * @property d The fourth coefficient of the equation in the form _f(x) = ax^4 + bx^3 + cx^2 + dx + e = 0_.
 * @property e The fifth coefficient of the equation in the form _f(x) = ax^4 + bx^3 + cx^2 + dx + e = 0_.
 *
 * @author Alberto Miola
 * */
class Quartic(val a: Complex, val b: Complex, val c: Complex, val d: Complex, val e: Complex) :
    PolynomialEquation(listOf(a, b, c, d, e)) {
    override val degree: Double
        get() = 4.0

    override fun discriminant(): Complex {
        val k = (b * b * c * c * d * d) -
                (d * d * d * b * b * b * Complex(4.0)) -
                (d * d * c * c * c * a * Complex(4.0)) +
                (d * d * d * c * b * a * Complex(18.0)) -
                (d * d * d * d * a * a * Complex(27.0)) +
                (e * e * e * a * a * a * Complex(256.0))
        val p = e *
                ((c * c * c * b * b * Complex(-4.0)) +
                        (b * b * b * c * d * Complex(18.0)) +
                        (c * c * c * c * a * Complex(16.0)) -
                        (d * c * c * b * a * Complex(80.0)) -
                        (d * d * b * b * a * Complex(6.0)) +
                        (d * d * a * a * c * Complex(144.0)))

        val r = (e * e) *
                (b * b * b * b * Complex(-27.0)) +
                b * b * c * a * Complex(144.0) -
                a * a * c * c * Complex(128.0) -
                d * b * a * a * Complex(192.0)

        return k + p + r
    }

    override fun derivative() = Cubic(
        a * Complex(4.0),
        b * Complex(3.0),
        c * Complex(2.0),
        d,
    )

    override fun roots(): List<Complex> {
        val fb = b / a
        val fc = c / a
        val fd = d / a
        val fe = e / a

        val q1 = (fc * fc) -
                (fb * fd * Complex(3.0)) +
                (fe * Complex(12.0))
        val q2 = (fc.pow(3.0) * Complex(2.0)) -
                (fb * fc * fd * Complex(9.0)) +
                (fd.pow(2.0) * Complex(27.0)) +
                (fb.pow(2.0) * fe * Complex(27.0)) -
                (fc * fe * Complex(72.0))
        val q3 = (fb * fc * Complex(8.0)) -
                (fd * Complex(16.0)) -
                (fb.pow(3.0) * Complex(2.0))
        val q4 = (fb.pow(2.0) * Complex(3.0)) -
                (fc * Complex(8.0))

        var temp = (q2 * q2 / Complex(4.0)) - (q1.pow(3.0))
        val q5 = (temp.sqrt() + (q2 / Complex(2.0))).pow(1.0 / 3.0)

        val q6 = if (q5.isZero) Complex() else ((q1 / q5) + q5) / Complex(3.0)
        temp = (q4 / Complex(12.0)) + q6

        val q7 = temp.sqrt() * Complex(2.0)
        val qFactor = if (q7.isZero) Complex() else q3 / q7

        temp = ((q4 * Complex(4.0)) / Complex(6.0)) -
                (q6 * Complex(4.0)) -
                qFactor
        val temp2 = ((q4 * Complex(4.0)) / Complex(6.0)) -
                (q6 * Complex(4.0)) +
                qFactor

        return listOf(
            (fb.negate() - q7 - temp.sqrt()) / Complex(4.0),
            (fb.negate() - q7 + temp.sqrt()) / Complex(4.0),
            (fb.negate() + q7 - temp2.sqrt()) / Complex(4.0),
            (fb.negate() + q7 + temp2.sqrt()) / Complex(4.0),
        )
    }
}