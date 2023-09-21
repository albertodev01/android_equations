package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.utils.exceptions.NonLinearException
import kotlin.math.*

/**
 * Implements the Riddler's method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method requires the root to be bracketed between two points [a] and [a] otherwise it will
 *  not work.
 *  - The rate of convergence is `sqrt(2)` and the convergence is guaranteed for not well-behaved
 *  functions.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class Riddler(
    val a: Double,
    val b: Double,
    function: String,
    tolerance: Double = 1.0e-15,
    maxSteps: Int = 20,
) :
    NonLinear(function, tolerance, maxSteps) {

    override fun solve(): NonLinearResult {
        // Exit immediately if the root is not bracketed
        if (evaluateOn(a) * evaluateOn(b) >= 0) {
            throw NonLinearException("The root is not bracketed in [$a, $b]")
        }

        val guesses = mutableListOf<Double>()
        var n = 1

        var x0 = a
        var x1 = b
        var y0 = evaluateOn(x0)
        var y1 = evaluateOn(x1)

        while (n <= maxSteps) {
            val x2 = (x0 + x1) / 2
            val y2 = evaluateOn(x2)

            // The guess on the n-th iteration
            val x = x2 + (x2 - x0) * (y0 - y1).sign * y2 / sqrt(y2 * y2 - y0 * y1)

            // Add the root to the list
            guesses.add(x)

            // Tolerance
            if (min((x - x0).absoluteValue, (x - x1).absoluteValue) < tolerance) {
                break
            }

            val y = evaluateOn(x)

            // Fixing signs
            if (y2.sign != y.sign) {
                x0 = x2
                y0 = y2
                x1 = x
                y1 = y
            } else {
                if (y1.sign != y.sign) {
                    x0 = x
                    y0 = y
                } else {
                    x1 = x
                    y1 = y
                }
            }

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

        return if (other is Riddler) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}