package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.utils.exceptions.NonLinearException
import kotlin.math.absoluteValue

/**
 * Implements Brent's method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method is guaranteed to converge to a root of `f(x)` if `f(x)` is a continuous function on
 *  the interval `[a, b]`. *
 *  - The root must be inside the `[a, b]` interval. For this reason, the method will fail if
 *  `f(a) * f(b) >= 0`.
 *
 * @property a The starting point of the interval.
 * @property b The ending point of the interval.
 *
 * @author Alberto Miola
 * */
class Brent(
    val a: Double,
    val b: Double,
    function: String,
    tolerance: Double = 1.0e-15,
    maxSteps: Int = 20,
) :
    NonLinear(function, tolerance, maxSteps) {

    private fun condition1(s: Double, a: Double, b: Double): Boolean {
        return !((s >= (a * 3 + b) / 4) && (s <= b))
    }

    private fun condition2(s: Double, b: Double, c: Double, flag: Boolean): Boolean {
        return flag && ((s - b).absoluteValue >= ((b - c).absoluteValue / 2))
    }

    private fun condition3(s: Double, b: Double, c: Double, d: Double, flag: Boolean): Boolean {
        return !flag && ((s - b).absoluteValue >= ((c - d).absoluteValue / 2))
    }

    private fun condition4(c: Double, b: Double, flag: Boolean): Boolean {
        return flag && ((b - c).absoluteValue <= tolerance.absoluteValue)
    }

    private fun condition5(c: Double, d: Double, flag: Boolean): Boolean {
        return !flag && ((c - d).absoluteValue <= tolerance.absoluteValue)
    }

    override fun solve(): NonLinearResult {
        val guesses = mutableListOf<Double>()
        var n = 1

        val evalA = evaluateOn(a)
        val evalB = evaluateOn(b)

        // Making sure that the root is in the given interval
        if (evalA * evalB >= 0) {
            throw NonLinearException("The root is not bracketed.")
        }

        // Variables setup
        var valueA = a
        var valueB = b
        var valueC = a
        var valueD = 0.0
        var diff = (valueB - valueA).absoluteValue
        var s: Double
        var flag = true

        if (evalA.absoluteValue < evalB.absoluteValue) {
            val temp = valueA
            valueA = valueB
            valueB = temp
        }

        while ((diff >= tolerance) && (n <= maxSteps)) {
            val fa = evaluateOn(valueA)
            val fb = evaluateOn(valueB)
            val fc = evaluateOn(valueC)

            s = if ((fa != fc) && (fb != fc)) {
                // Inverse quadratic interpolation method
                val term1 = valueA * fb * fc / ((fa - fb) * (fa - fc))
                val term2 = valueB * fa * fc / ((fb - fa) * (fb - fc))
                val term3 = valueC * fa * fb / ((fc - fa) * (fc - fb))

                term1 + term2 + term3
            } else {
                // Secant method
                valueB - (fb * ((valueB - valueA) / (fb - fa)))
            }

            if (condition1(s, valueA, valueB) ||
                condition2(s, valueB, valueC, flag) ||
                condition3(s, valueB, valueC, valueD, flag) ||
                condition4(valueB, valueC, flag) ||
                condition5(valueC, valueD, flag)
            ) {
                // Bisection method
                s = (valueA + valueB) / 2
            } else {
                flag = false
            }

            // 's' is the value of the root to be discovered
            guesses.add(s)

            // Generating new brackets for the next iteration
            val fs = evaluateOn(s)
            valueD = valueC
            valueC = valueB

            if (fa * fs < 0) {
                valueB = s
            } else {
                valueA = s
            }

            if (fa.absoluteValue < fb.absoluteValue) {
                val temp = valueA
                valueA = valueB
                valueB = temp
            }

            // Updating the exit conditions
            ++n
            diff = (valueB - valueA).absoluteValue
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

        return if (other is Brent) a == other.a && b == other.b else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }
}