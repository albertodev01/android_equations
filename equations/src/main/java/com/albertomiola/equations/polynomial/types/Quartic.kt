package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

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
        val q6 = ((q1 / q5) + q5) / Complex(3.0)
        temp = (q4 / Complex(12.0)) + q6
        val q7 = temp.sqrt() * Complex(2.0)

        temp = ((q4 * Complex(4.0)) / Complex(6.0)) -
                (q6 * Complex(4.0)) -
                (q3 / q7)
        val temp2 = ((q4 * Complex(4.0)) / Complex(6.0)) -
                (q6 * Complex(4.0)) +
                (q3 / q7)

        return listOf(
            (fb.negate() - q7 - temp.sqrt()) / Complex(4.0),
            (fb.negate() - q7 + temp.sqrt()) / Complex(4.0),
            (fb.negate() + q7 - temp2.sqrt()) / Complex(4.0),
            (fb.negate() + q7 + temp2.sqrt()) / Complex(4.0),
        )
    }
}