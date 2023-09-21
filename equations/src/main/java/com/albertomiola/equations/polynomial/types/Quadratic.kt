package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

class Quadratic(val a: Complex, val b: Complex = Complex(), val c: Complex = Complex()) :
    PolynomialEquation(listOf(a, b, c)) {
    override val degree: Double
        get() = 2.0

    override fun discriminant() = b * b - Complex(4) * a * c

    override fun derivative() = Linear(a * Complex(2), b)

    override fun roots(): List<Complex> {
        val twoA = Complex(2) * a
        var disc = discriminant()

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