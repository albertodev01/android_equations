package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.utils.complex.Complex

class Linear(val a: Complex, val b: Complex) : PolynomialEquation(listOf(a, b)) {
    override val degree: Double get() = 1.0

    override fun discriminant() = Complex(1.0)

    override fun derivative() = Constant(a)

    override fun roots() = listOf(b.negate() / a)

    fun copy(a: Complex?, b: Complex?) = Linear(a ?: this.a, b ?: this.b)
}