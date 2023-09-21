package com.albertomiola.equations.nonlinear.types

import com.albertomiola.equations.nonlinear.NonLinear
import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.utils.exceptions.NonLinearException
import kotlin.math.absoluteValue

/**
 * Implements Newton's method to find the roots of a given equation.
 *
 * **Characteristics**:
 *  - The method is extremely powerful but it's not guaranteed to converge to a root of [function]
 *  - The algorithm may fail for example due to a division by zero, if the derivative evaluated at
 *  a certain value is 0, or because the initial guess is too far from the solution.
 *
 * @property x0 The initial guess of the algorithm
 *
 * @author Alberto Miola
 * */
class Newton(val x0: Double, function: String, tolerance: Double = 1.0e-15, maxSteps: Int = 20) :
    NonLinear(function, tolerance, maxSteps) {

    override fun solve(): NonLinearResult {
        var diff = tolerance + 1
        var n = 0
        var currx0 = x0
        val guesses = mutableListOf<Double>()

        while ((diff >= tolerance) && (n < maxSteps)) {
            val der = evaluateDerivativeOn(currx0)

            if ((der == 0.0) || (der.isNaN())) {
                throw NonLinearException("Couldn't evaluate f'($currx0)")
            }

            diff = -evaluateOn(currx0) / der
            currx0 += diff
            guesses.add(currx0)

            diff = diff.absoluteValue
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

        return if (other is Newton) x0 == other.x0 else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + x0.hashCode()
        return result
    }
}