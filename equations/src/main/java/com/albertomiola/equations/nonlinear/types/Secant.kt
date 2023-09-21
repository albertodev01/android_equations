package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.utils.exceptions.NonLinearException
import kotlin.math.absoluteValue

/**
 * Implements the secant method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method is not guaranteed to converge to a root of _f(x)_.
 *  - The secant method does not require the root to remain bracketed, like the bisection method
 *  does for example, so it doesn't always converge.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class Secant(
    val a: Double,
    val b: Double,
    function: String,
    tolerance: Double = 1.0e-15,
    maxSteps: Int = 20,
) :
    NonLinear(function, tolerance, maxSteps) {

    override fun solve(): NonLinearResult {
        val guesses = mutableListOf<Double>()
        var n = 1

        var xold = a
        var x0 = b

        var fold = evaluateOn(xold)
        var fnew = evaluateOn(x0)
        var diff = tolerance + 1

        while ((diff >= tolerance) && (n <= maxSteps)) {
            val den = fnew - fold

            if ((den == 0.0) || (den.isNaN())) {
                throw NonLinearException("Invalid denominator encountered ($den).")
            }

            diff = -(fnew * (x0 - xold)) / den
            xold = x0
            fold = fnew
            x0 += diff

            diff = diff.absoluteValue
            ++n

            guesses.add(x0)
            fnew = evaluateOn(x0)
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

        return if (other is Secant) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}