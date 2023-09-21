package com.albertomiola.equations.polynomial.utils

import com.albertomiola.equations.polynomial.PolynomialEquation

/**
 * This utility class holds the quotient and the remainder of a division between two polynomials.
 * When you use [PolynomialEquation.times], this class is returned. For example:
 *
 * ```kotlin
 * val numerator = Quadratic(1, -3, 2)
 * val denominator = Linear(1, 2)
 * val division = numerator / denominator // 'division' is of type 'AlgebraicDivision'
 * ```
 *
 * When dividing two polynomials, there always are a quotient and a reminder.
 *
 * @author Alberto Miola
 * */
data class PolynomialDivisionResult(
    var quotient: PolynomialEquation,
    val remainder: PolynomialEquation,
) {
    override fun toString(): String {
        val q = quotient.toString().replace("f(x) = ", "")
        val r = remainder.toString().replace("f(x) = ", "")

        return "Q = $q\nR = $r"
    }
}