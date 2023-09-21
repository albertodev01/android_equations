package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.utils.exceptions.NonLinearException
import kotlin.math.absoluteValue

/**
 * Implements the regula falsi method (also known as "_false position method_") to find the roots of
 * a given equation.
 *
 * **Characteristics**:
 *  - The method requires the root to be bracketed between two points [a] and [a] otherwise it will
 *  not work.
 *  - If you cannot assume that a function may be interpolated by a linear function, then applying
 *  this method method could result in worse results than the bisection method.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class RegulaFalsi(
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
        var toleranceCheck = true
        var n = 1

        var tempA = a
        var tempB = b

        while (toleranceCheck && (n <= maxSteps)) {
            // Evaluating on A and B the function
            val fa = evaluateOn(tempA)
            val fb = evaluateOn(tempB)

            // Computing the guess
            val c = (fa * tempB - fb * tempA) / (fa - fb)
            val fc = evaluateOn(c)

            // Making sure the evaluation is not zero
            if (fc == 0.0) {
                break
            }

            // Shrink the interval
            if (fa * fc < 0) {
                tempB = c
            } else {
                tempA = c
            }

            // Add the root to the list
            guesses.add(c)

            toleranceCheck = fc.absoluteValue > tolerance
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

        return if (other is RegulaFalsi) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}