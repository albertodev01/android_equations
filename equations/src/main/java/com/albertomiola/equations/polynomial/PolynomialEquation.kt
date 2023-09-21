package com.albertomiola.equations.polynomial

import com.albertomiola.equations.polynomial.types.Constant
import com.albertomiola.equations.polynomial.types.Cubic
import com.albertomiola.equations.polynomial.types.DurandKerner
import com.albertomiola.equations.polynomial.types.Linear
import com.albertomiola.equations.polynomial.types.Quadratic
import com.albertomiola.equations.polynomial.types.Quartic
import com.albertomiola.equations.polynomial.utils.PolynomialDivisionResult
import com.albertomiola.equations.polynomial.utils.PolynomialLongDivision
import com.albertomiola.equations.utils.complex.Complex
import com.albertomiola.equations.utils.exceptions.PolynomialException
import kotlin.math.max

/**
 * The message thrown by the constructor to indicate that the polynomial object cannot be created
 * correctly.
 *
 * @author Alberto Miola
 * */
private const val EXCEPTION_MESSAGE = """
To solve a polynomial equation (unless it's a constant), the coefficient with the highest degree cannot be zero. As such, make sure to pass a list that either:

 1. Contains a single value (like `[5]` or `[-2]`) to represent a polynomial whose degree is zero.
 2. Contains one or more values AND the first parameter is not zero (like `[1, 0]` or `[-6, -3, 2, 8]`).
"""

/**
 * Abstract class representing an _polynomial equation_, also know as _algebraic equation_, which
 * has a single variable with a maximum degree.
 *
 * The coefficients of the algebraic equations can be real numbers or complex numbers. These are
 * examples of an algebraic equations of third degree:
 *
 *  - x<sup>3</sup> + 5x + 2 = 0
 *  - 2x<sup>3</sup> + (6+i)x + 8i = 0
 *
 * This class stores the coefficients list starting from the one with the **highest** degree.
 *
 * @property coefficients The
 *
 * @throws PolynomialEquation s
 *
 * @author Alberto Miola
 * */
abstract class PolynomialEquation(val coefficients: List<Complex>) {
    init {
        // Unless this is a constant value, the coefficient with the highest degree cannot be zero.
        if (!isValid) {
            throw PolynomialException(EXCEPTION_MESSAGE)
        }
    }

    /**
     * A polynomial equation is **valid** if the coefficient associated to the variable of highest
     * degree is different from zero. In other words, the polynomial is valid if `a` is different
     * from zero.
     *
     * @author Alberto Miola
     * */
    private val isValid: Boolean get() = !coefficients.first().isZero || this is Constant

    /**
     * Determines whether the polynomial is real or not.
     *
     * @return True if all members of [coefficients] have the imaginary equal to zero.
     *
     * @author Alberto Miola
     * */
    val isRealEquation: Boolean
        get() = coefficients.all { c -> c.imaginary == 0.0 }

    /**
     * Returns the degree of the polynomial.
     *
     * @author Alberto Miola
     * */
    abstract val degree: Double

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return if (other is PolynomialEquation) coefficients == other.coefficients else false
    }

    override fun hashCode() = coefficients.hashCode()

    override fun toString() = convertPolynomialToString()

    /**
     * Returns a string representation of the polynomial where the coefficients are converted into
     * their fractional representation.
     *
     * @author Alberto Miola
     * */
    fun toStringWithFractions() = convertPolynomialToString(true)

    /**
     * Represents the equation as a string. If [asFraction] is `true` then the coefficients are
     * converted into their fractional representation with the best approximation possible.
     *
     * @author Alberto Miola
     * */
    private fun convertPolynomialToString(asFraction: Boolean = false): String {
        if (coefficients.size == 1) {
            return if (asFraction) {
                "f(x) = ${coefficients.first().toStringAsFraction()}"
            } else {
                "f(x) = ${coefficients.first().toString()}"
            }
        } else {
            val builder = StringBuilder();
            var power = coefficients.size - 1

            // Adding 'f(x) = ' at the beginning
            builder.append("f(x) = ")

            for (c in coefficients) {
                //1. If it's a coefficient 0, then skip
                if (c.isZero) {
                    --power
                    continue
                }

                // Write the sign unless it's the first coefficient
                if (power != coefficients.size - 1) {
                    builder.append(" + ")
                }

                //2. Write the complex
                if (asFraction) {
                    // Add parenthesis if needed
                    if ((c.real != 0.0) && (c.imaginary != 0.0)) {
                        builder.append('(').append(c.toStringAsFraction()).append(')')
                    } else {
                        builder.append(c.toStringAsFraction())
                    }
                } else {
                    builder.append(c)
                }

                //3. If it is power = 0 avoid it, we don't want x^0 (useless)
                if (power != 0) {
                    if (power == 1) {
                        builder.append('x');
                    } else {
                        builder.append("x^").append(power);
                    }
                }

                --power
            }

            return builder.toString()
        }
    }

    /**
     * Evaluates the polynomial a [Double] value [x].
     *
     * @return A value that indicates the evaluation of [x] on this polynomial.
     *
     * @author Alberto Miola
     * */
    fun evaluateOn(x: Complex): Complex {
        var value = Complex()
        var power = coefficients.size - 1.0

        // Actual valuation
        for (coefficient in coefficients) {
            value += if (power != 0.0) {
                x.pow(power) * coefficient
            } else {
                coefficient
            }
            power--
        }

        return value
    }

    /**
     * Evaluates the polynomial a [Int] value [x].
     *
     * @return A value that indicates the evaluation of [x] on this polynomial.
     *
     * @author Alberto Miola
     * */
    fun evaluateOn(x: Int) = evaluateOn(Complex(x))

    /**
     * Calculates the coefficient of the polynomial whose degree is [degree]. For example:
     *
     * ```kotlin
     * val quadratic = Quadratic(
     *   Complex(2, 0),
     *   Complex(-6, 0),
     *   Complex(5, 0),
     * )
     *
     * val degree0 = quadratic.coefficient(0) // Complex(5, 0)
     * val degree1 = quadratic.coefficient(0) // Complex(-6, 0)
     * val degree2 = quadratic.coefficient(0) // Complex(2, 0)
     * ```
     *
     * @return The coefficient of the polynomial whose degree is [degree]. If no coefficient for the
     * given [degree] is found, `null` is returned.
     *
     * @author Alberto Miola
     * */
    fun coefficient(degree: Int): Complex? {
        return if ((degree < 0) || (degree > coefficients.size - 1))
            null
        else
            coefficients[coefficients.size - degree - 1]
    }

    /**
     * The sum of two polynomials is performed by adding the corresponding coefficients. The degrees
     * of the two polynomials don't need to be the same. For example, you could sum a [Cubic] with
     * a [Linear].
     *
     * @return The sum of this and [other] polynomials.
     *
     * @author Alberto Miola
     * */
    operator fun plus(other: PolynomialEquation): PolynomialEquation {
        val maxDegree = max(coefficients.size, other.coefficients.size)
        val newCoefficients = mutableListOf<Complex>()

        for (degree in maxDegree downTo 0) {
            val thisCoefficient = coefficient(degree) ?: Complex()
            val otherCoefficient = other.coefficient(degree) ?: Complex()
            val sum = thisCoefficient + otherCoefficient

            if (!sum.isZero) newCoefficients.add(sum)
        }

        return createFromComplexList(newCoefficients)
    }

    /**
     * The difference of two polynomials is performed by adding the corresponding coefficients. The
     * degrees of the two polynomials don't need to be the same. For example, you could sum a
     * [Cubic] with a [Linear].
     *
     * @return The difference of this and [other] polynomials.
     *
     * @author Alberto Miola
     * */
    operator fun minus(other: PolynomialEquation): PolynomialEquation {
        val maxDegree = max(coefficients.size, other.coefficients.size)
        val newCoefficients = mutableListOf<Complex>()

        for (degree in maxDegree downTo 0) {
            val thisCoefficient = coefficient(degree) ?: Complex()
            val otherCoefficient = other.coefficient(degree) ?: Complex()
            val diff = thisCoefficient - otherCoefficient

            if (!diff.isZero) newCoefficients.add(diff)
        }

        return createFromComplexList(newCoefficients)
    }

    /**
     * The product of two polynomials is performed by multiplying the corresponding coefficients of
     * the polynomials. The degrees of the two polynomials don't need to be the same. For example,
     * you could multiply a [Constant] with a [DurandKerner].
     *
     * @return The product of this and [other] polynomials.
     *
     * @author Alberto Miola
     * */
    operator fun times(other: PolynomialEquation): PolynomialEquation {
        val newLength = coefficients.size + other.coefficients.size
        val newCoefficients = Array(newLength) { _ -> Complex() }

        for (i in 0..coefficients.size) {
            for (j in 0..other.coefficients.size) {
                newCoefficients[i + j] += coefficients[i] * other.coefficients[j]
            }
        }

        return createFromComplexList(newCoefficients.toList())
    }

    /**
     * This operator divides a polynomial by another polynomial of the same or lower degree. The
     * algorithm used to divide a polynomial by another is called "Polynomial long division".
     *
     * @return The division of this and [other] polynomials.
     *
     * @author Alberto Miola
     * */
    operator fun div(other: PolynomialEquation) = PolynomialLongDivision(this, other).divide()

    /**
     * Returns the polynomial discriminant, if it exists.
     *
     * @author Alberto Miola
     * */
    abstract fun discriminant(): Complex

    /**
     * Returns the derivative of the polynomial.
     *
     * @author Alberto Miola
     * */
    abstract fun derivative(): PolynomialEquation

    /**
     * This method finds the roots (the solutions) of the associated _P(x) = 0_ equation.
     *
     * @return The solutions of the _P(x) = 0_ equation.
     *
     * @author Alberto Miola
     * */
    abstract fun roots(): List<Complex>

    /**
     * A companion object for building polynomials from lists of numbers.
     *
     * @see createFromComplexList
     * @see createFromDoubleList
     *
     * @author Alberto Miola
     * */
    companion object Factory {
        /**
         * Creates a [PolynomialEquation] subtype according with the length of the [coefficients]
         * list. In particular:
         *
         *  - if the length is 1, a [Constant] object is returned;
         *  - if the length is 2, a [Linear] object is returned;
         *  - if the length is 3, a [Quadratic] object is returned;
         *  - if the length is 4, a [Cubic] object is returned;
         *  - if the length is 5, a [Quartic] object is returned;
         *  - if the length is 6 or higher, a [DurandKerner] object is returned.
         *
         * If the length of [coefficients] was 3 for example, it would mean that you're trying to
         * solve a quadratic equation (because a quadratic has exactly 3 coefficients). Another
         * example:
         *
         * ```kotlin
         * val linear = PolynomialEquation.createFromComplexList(listOf<Complex>(
         *   Complex(1, 3),
         *   Complex(0, 1)
         * ))
         * ```
         *
         * In this case, `linear` is of type [Linear] because the given coefficients list represent
         * the `(1 + 3i)x + i = 0` equation. Use this method when the coefficients can be complex
         * numbers. If there were only real numbers, use [createFromDoubleList].
         *
         * @see createFromDoubleList
         *
         * @author Alberto Miola
         * */
        fun createFromComplexList(coefficients: List<Complex>): PolynomialEquation =
            when (coefficients.size) {
                1 -> Constant(coefficients[0])
                2 -> Linear(coefficients[0], coefficients[1])
                3 -> Quadratic(coefficients[0], coefficients[1], coefficients[2])
                4 -> Cubic(coefficients[0], coefficients[1], coefficients[2], coefficients[3])
                5 -> Quartic(
                    coefficients[0],
                    coefficients[1],
                    coefficients[2],
                    coefficients[3],
                    coefficients[4],
                )

                else -> DurandKerner(coefficients)
            }
    }

    /**
     * Creates a [PolynomialEquation] subtype according with the length of the [coefficients]
     * list. In particular:
     *
     *  - if the length is 1, a [Constant] object is returned;
     *  - if the length is 2, a [Linear] object is returned;
     *  - if the length is 3, a [Quadratic] object is returned;
     *  - if the length is 4, a [Cubic] object is returned;
     *  - if the length is 5, a [Quartic] object is returned;
     *  - if the length is 6 or higher, a [DurandKerner] object is returned.
     *
     * If the length of [coefficients] was 3 for example, it would mean that you're trying to
     * solve a quadratic equation (because a quadratic has exactly 3 coefficients). Another
     * example:
     *
     * ```kotlin
     * val linear = PolynomialEquation.createFromDoubleList(listOf<Double>(0.5, 6.0))
     * ```
     *
     * In this case, `linear` is of type [Linear] because the given coefficients list represent
     * the `5x + 6 = 0` equation. Use this method when the coefficients are all real numbers. If
     * there were complex numbers as well, use [createFromComplexList].
     *
     * @see createFromComplexList
     *
     * @author Alberto Miola
     * */
    fun createFromDoubleList(coefficients: List<Double>): PolynomialEquation {
        val list = coefficients.map { value -> Complex(value) }.toList()
        return createFromComplexList(list)
    }
}