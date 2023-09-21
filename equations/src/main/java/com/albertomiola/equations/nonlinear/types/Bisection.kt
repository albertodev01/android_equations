package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import kotlin.math.absoluteValue

/**
 * Implements the bisection method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method is guaranteed to converge to a root of `f(x)` if `f(x)` is a continuous function on
 *  the interval `[a, b]`. *
 *  - The values of `f(a)` and `f(b)` must have opposite signs.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class Bisection(
    val a: Double,
    val b: Double,
    function: String,
    tolerance: Double = 1.0e-15,
    maxSteps: Int = 20,
) :
    NonLinear(function, tolerance, maxSteps) {

    override fun solve(): NonLinearResult {
        var amp = tolerance + 1
        var n = 1
        val guesses = mutableListOf<Double>()
        var pA = a
        var pB = b
        var fa = evaluateOn(pA)

        while ((amp >= tolerance) && (n <= maxSteps)) {
            ++n
            amp = (pB - pA).absoluteValue
            val x0 = pA + amp * 0.5

            guesses.add(x0)
            val fx = evaluateOn(x0)

            if (fa * fx < 0) {
                pB = x0
            } else {
                if (fa * fx > 0) {
                    pA = x0
                    fa = fx
                } else {
                    amp = 0.0
                }
            }
        }

        return NonLinearResult(
            guesses = guesses,
            convergence = convergence(guesses),
            efficiency = efficiency(guesses, maxSteps),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        return if (other is Bisection) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}