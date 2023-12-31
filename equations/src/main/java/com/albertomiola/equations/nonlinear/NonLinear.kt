package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.utils.NonLinearResult
import com.albertomiola.equations.nonlinear.types.*
import com.albertomiola.equations.utils.exceptions.NonLinearException
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression
import kotlin.math.*

/**
 * An abstract class that represents a nonlinear equation, which can be solved with a particular
 * root-finding algorithm. No complex numbers are allowed.
 *
 * The so called "**root-finding algorithms**" are iterative methods that start from an initial
 * value (or a couple of values) and try to build a scalar succession that converges as much as
 * possible to the root. The algorithms implemented by this library are:
 *
 *  - [Bisection]
 *  - [Brent]
 *  - [Chords]
 *  - [Newton]
 *  - [RegulaFalsi]
 *  - [Riddler]
 *  - [Secant]
 *
 * Each subclass of [NonLinear] has to override the [solve] method to build the scalar succession
 * with a certain logic. It's expected to produce a series of values that progressively get closer
 * to the real root.
 *
 * @property function The function f(x) for which the algorithm has to find a solution.
 * @property tolerance The algorithm accuracy. The default value is `1.0e-15`.
 * @property maxSteps The maximum number of iterations to be made by the algorithm. The default
 * value is `20`.
 *
 * @author Alberto Miola
 * */
abstract class NonLinear(
    val function: String,
    val tolerance: Double = 1.0e-15,
    val maxSteps: Int = 20,
) {
    init {
        if (!Expression(function, Argument("x")).checkSyntax()) {
            throw NonLinearException("The function '$function' has syntax errors.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return if (other is NonLinear) {
            function == other.function && tolerance == other.tolerance && maxSteps == other.maxSteps
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = function.hashCode()
        result = 31 * result + tolerance.hashCode()
        result = 31 * result + maxSteps
        return result
    }

    override fun toString() = "f(x) = $function"

    /**
     * To get a meaningful result, it makes sense to compute the rate of convergence only if the
     * algorithm made **at least** 3 (iterations).
     *
     * If the length of [guesses] is 2 or lower, [Double.NaN] is returned.
     *
     * @author Alberto Miola
     * */
    fun convergence(guesses: List<Double>): Double {
        val size = guesses.size - 1

        if (size >= 3) {
            val numerator =
                (guesses[size] - guesses[size - 1]).absoluteValue / (guesses[size - 1] - guesses[size - 2]).absoluteValue
            val denominator =
                (guesses[size - 1] - guesses[size - 2]).absoluteValue / (guesses[size - 2] - guesses[size - 3]).absoluteValue

            return ln(numerator) / ln(denominator)
        }

        return Double.NaN
    }

    /**
     * The efficiency is evaluated only if the convergence is not [Double.NaN]. The formula is:
     *
     * - efficiency = convergenceRate^(1 / max_steps)
     *
     * @author Alberto Miola
     * */
    fun efficiency(guesses: List<Double>, steps: Int) = convergence(guesses).pow(1.0 / steps)

    /**
     * Evaluates of the function on the given [x] value.
     *
     * @return The evaluation of the function on [x]. If the evaluation fails, [Double.NaN] is
     * returned.
     *
     * @author Alberto Miola
     * */
    fun evaluateOn(x: Double): Double {
        val variable = Argument("x = $x")
        return Expression(function, variable).calculate()
    }

    /**
     * Evaluates the derivative of the function on the given [x] value.
     *
     * @return The evaluation of the function derivative on [x]. If the evaluation fails,
     * [Double.NaN] is returned.
     *
     * @author Alberto Miola
     * */
    fun evaluateDerivativeOn(x: Double): Double {
        val h = 1.0e-15.pow(1 / 3) * x
        val upper = evaluateOn(x + h)
        val lower = evaluateOn(x - h)

        return (upper - lower) / (h * 2)
    }

    /**
     * Generates the succession generated by the root-finding algorithm. Returns a [NonLinearResult]
     * object whose members are:
     *
     *  - a `guesses` field, which contains the list of values generated by the algorithm on each step;
     *  - a `convergence` field, whose value represents the convergence rate for the generated
     *  succession (computed using [convergence]).
     *  - a `efficiency` named field, whose value represents the efficiency of the algorithm
     *  (computed using [efficiency]).
     *
     * @author Alberto Miola
     * */
    abstract fun solve(): NonLinearResult
}