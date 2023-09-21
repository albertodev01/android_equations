package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import kotlin.math.absoluteValue

/**
 * Implements the chords method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method is guaranteed to converge to a root of `f(x)` if `f(x)` is a continuous function on
 *  the interval `[a, b]`. *
 *  - The values of `f(a)` and `f(b)` must have opposite signs AND there must be at least one root
 *  in `[a, b]`. These are 2 required conditions.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class Chords(
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

        // Initial points setup
        var x0 = (a * evaluateOn(b) - b * evaluateOn(a)) / (evaluateOn(b) - evaluateOn(a))
        var diff = evaluateOn(x0).absoluteValue

        while ((diff >= tolerance) && (n <= maxSteps)) {
            val fa = evaluateOn(a)
            val fx = evaluateOn(x0)

            x0 = if (fa * fx < 0) {
                (x0 * fa - a * fx) / (fa - fx)
            } else {
                val fb = evaluateOn(b)
                (x0 * fb - b * fx) / (fb - fx)
            }

            guesses.add(x0)
            diff = fx.absoluteValue
            ++n
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

        return if (other is Chords) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}