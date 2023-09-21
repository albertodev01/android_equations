package com.albertomiola.equations.utils.fraction

import com.albertomiola.equations.utils.Rational
import com.albertomiola.equations.utils.exceptions.FractionException
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Kotlin representation of a fraction having both [numerator] and [denominator] as integers.
 *
 * If the [denominator] is zero, a [FractionException] object is thrown. To create a [Fraction]
 * object, use one of its constructors:
 *
 * ```kotlin
 * Fraction(-2, 5) // -2/5
 * Fraction.createFromString("-2/5") // -2/5
 * Fraction.createFromDouble(-0.4) // -2/5
 * ```
 *
 * The [Fraction] type is **immutable**, so methods that require changing the object's internal
 * state return a new instance. For example, the [reduce] method returns a new [Fraction] object
 * that does not depend on the original one. Another example:
 *
 * ```kotlin
 * val f1 = Fraction(5, 7) // 5/7
 * val f2 = Fraction(1, 5) // 1/5
 *
 * val sum = f1 + f2 // 32/35
 * val sub = f1 - f2 // 18/35
 * val mul = f1 * f2 // 1/7
 * val div = f1 / f2 // 25/7
 * ```
 *
 * Operators always return new objects. There are extension on [num] and [String] that allow you to
 * build [Fraction] objects "on the fly". For example:
 *
 * ```kotlin
 * 1.5.toFraction() // 3/2
 * "4/5".toFraction() // 4/5
 * ```
 *
 * If the string doesn't represent a valid fraction, a [FractionException] object is thrown.
 *
 * @constructor Creates a fraction with a numerator [num] and a denominator [den]. If the denominator
 * is negative, the fraction is 'normalized' so that the minus sign only appears in front of the
 * denominator. For example:
 *
 * ```kotlin
 * Fraction(3, 4)  // is interpreted as 3/4
 * Fraction(-3, 4) // is interpreted as -3/4
 * Fraction(3, -4) // is interpreted as -3/4
 * ```
 *
 * @exception FractionException if the denominator is zero or if you try to build a fraction object
 * from an invalid source.
 *
 * @author Alberto Miola
 * */
class Fraction(private var num: Int, private var den: Int = 1) : Rational() {
    // Makes sure that the denominator is not zero AND moves the - sign (if needed) to the numerator
    init {
        if (denominator == 0) {
            throw NumberFormatException("The denominator cannot be zero!")
        }

        // Fixing the sign of numerator and denominator
        if (den < 0) {
            num *= -1
            den *= -1
        }
    }

    override val numerator: Int get() = num

    override val denominator: Int get() = den

    override val isNegative: Boolean get() = numerator < 0

    override val isWhole: Boolean get() = denominator == 1

    /**
     * A "proper fraction" is a fraction whose numerator is smaller than the denominator.
     *
     * @return `true` if the numerator is smaller than the denominator.
     *
     * @author Alberto
     * */
    val isProper: Boolean get() = numerator < denominator

    /**
     * An "improper fraction" is a fraction whose numerator is greater than/equal to the denominator.
     *
     * @return `true` if the numerator is greater than/equal to the denominator.
     *
     * @author Alberto
     * */
    val isImproper: Boolean get() = numerator >= denominator

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        // Two fractions are equal if their "cross product" is equal. For example:
        //
        // Fraction(1, 2) == Fraction(2, 4) // true
        //
        // The above example returns true because the "cross product" of `one` and two` is equal
        // (1*4 = 2*2).
        return if (other is Fraction)
            numerator * other.denominator == denominator * other.numerator
        else
            false
    }

    override fun hashCode(): Int {
        var result = num
        result = 31 * result + den
        return result
    }

    override fun toString() = if (denominator == 1) "$numerator" else "$numerator/$denominator"

    override fun toDouble() = numerator.toDouble() / denominator.toDouble()

    override fun negate() = Fraction(numerator * -1, denominator)

    /**
     * Returns the greatest common divisor of two numbers [a] and [b].
     *
     * @author Alberto
     * */
    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    override fun reduce(): Rational {
        // Storing the sign for later use.
        val sign = if (numerator < 0) -1 else 1

        // Calculating the gcd for reduction.
        val lgcd = gcd(numerator, denominator)

        val num = (numerator * sign) / lgcd
        val den = (denominator * sign) / lgcd

        return Fraction(num, den)
    }

    /**
     * Returns a **deep** copy of the object and optionally uses the given parameters, if not null.
     *
     * @author Alberto
     * */
    fun copy(numerator: Int?, denominator: Int?): Fraction {
        return Fraction(
            numerator ?: this.numerator,
            denominator ?: this.denominator,
        )
    }

    /**
     * The numerator and the denominator of this object are swapped.
     *
     * @return A new [Fraction] object that has inverted numerator and denominator.
     *
     * @author Alberto
     * */
    fun inverse(): Fraction = Fraction(denominator, numerator)

    /**
     * The sum between two fractions.
     *
     * @return A [Fraction] object that is the sum of this and [other] rational objects.
     *
     * @author Alberto
     * */
    operator fun plus(other: Fraction): Fraction {
        return Fraction(
            numerator * other.denominator + denominator * other.numerator,
            denominator * other.denominator,
        )
    }

    /**
     * The difference between two fractions.
     *
     * @return A [Fraction] object that is the difference of this and [other] rational objects.
     *
     * @author Alberto
     * */
    operator fun minus(other: Fraction): Fraction {
        return Fraction(
            numerator * other.denominator - denominator * other.numerator,
            denominator * other.denominator,
        )
    }

    /**
     * The product between two fractions.
     *
     * @return A [Fraction] object that is the product of this and [other] rational objects.
     *
     * @author Alberto
     * */
    operator fun times(other: Fraction): Fraction {
        return Fraction(
            numerator * other.numerator,
            denominator * other.denominator,
        )
    }

    /**
     * The division between two fractions.
     *
     * @return A [Fraction] object that is the division of this and [other] rational objects.
     *
     * @author Alberto
     * */
    operator fun div(other: Fraction): Fraction {
        return Fraction(
            numerator * other.denominator,
            denominator * other.numerator,
        )
    }

    /**
     * Increments the fraction by 1.
     *
     * @author Alberto
     * */
    operator fun inc(): Fraction = this + Fraction(1)

    /**
     * Decrements the fraction by 1.
     *
     * @author Alberto
     * */
    operator fun dec(): Fraction = this - Fraction(1)

    operator fun component1(): Int = numerator

    operator fun component2(): Int = denominator

    /**
     *  A factory that creates new [Fraction] objects from strings or floating-point objects.
     *
     * @see createFromString
     * @see createFromDouble
     *
     * @author Alberto
     * */
    companion object Factory {
        private val fractionRegex = Regex("""(^-?|^\+?)(?:[1-9][0-9]*|0)(?:/[1-9][0-9]*)?""")

        /**
         * Makes sure that [value] is not [Double.NaN], [Double.POSITIVE_INFINITY] or
         * [Double.NEGATIVE_INFINITY].
         *
         * @exception FractionException if [value] is [Double.NaN], [Double.POSITIVE_INFINITY] or
         * [Double.NEGATIVE_INFINITY].
         *
         * @author Alberto
         * */
        private fun checkValue(value: Double) {
            if ((value.isNaN()) || (value.isInfinite())) {
                throw FractionException("NaN and Infinite are not allowed.")
            }
        }

        /**
         * Builds an instance of [Fraction] if [value] represents a valid fraction. Some correct
         * examples are:
         *
         * ```kotlin
         * Fraction.createFromString("5/2")
         * Fraction.createFromString("-5/2")
         * Fraction.createFromString("5")
         * ```
         *
         * The denominator cannot be negative or zero:
         *
         * ```kotlin
         * Fraction.createFromString("5/2")
         * Fraction.createFromString("-5/2")
         * Fraction.createFromString("5")
         * ```
         *
         * If the given [value] doesn't represent a fraction, an exception is thrown.
         *
         * @exception FractionException if [value] does not represent a fraction.
         *
         * @return A [Fraction] object that represents the given [value], if valid.
         *
         * @author Alberto
         * */
        fun createFromString(value: String): Fraction {
            if (!fractionRegex.matches(value) || value.contains("/-")) {
                throw NumberFormatException("The string $value is not a valid fraction")
            }

            // Remove the leading + (if any)
            val fraction = value.replace("+", "")

            // Look for the '/' separator
            val barPos = fraction.indexOf('/')

            return if (barPos == -1) {
                Fraction(fraction.toInt())
            } else {
                val den = fraction.substring(barPos + 1).toInt()

                if (den == 0) {
                    throw NumberFormatException("Denominator cannot be zero")
                }

                Fraction(fraction.substring(0, barPos).toInt(), den)
            }
        }

        /**
         * Tries to give a fractional representation of a [value] according with the given
         * [precision]. This implementation takes inspiration from the continued fraction algorithm.
         * For example
         *
         * ```kotlin
         * Fraction.createFromDouble(3.8) // represented as 19/5
         * ```
         *
         * Note that irrational numbers can **not** be represented as fractions. If you try to use
         * this method on π (3.1415...) for example, you won't get a valid result:
         *
         * ```kotlin
         * Fraction.createFromDouble(math.pi)
         * ```
         *
         * The above code doesn't throw. It returns a [Fraction] object because the algorithm only
         * considers the first 10 decimal digits (since [precision] is set to 1.0e-10).
         *
         * ```kotlin
         * Fraction.createFromDouble(math.pi, precision: 1.0e-20)
         * ```
         *
         * This example will return another different value because it considers the first 20
         * digits. It's still not a fractional representation of π because irrational numbers cannot
         * be expressed as fractions.
         *
         * You should only use this method with rational numbers.
         *
         * @exception FractionException if [value] is NaN or Infinite.
         *
         * @return A [Fraction] object that represents the given [value], if valid.
         *
         * @author Alberto
         * */
        fun createFromDouble(value: Double, precision: Double = 1.0e-12): Fraction {
            checkValue(value)
            checkValue(precision)

            // Storing the sign
            val mul = if (value >= 0) 1 else -1
            val x = value.absoluteValue

            // How many digits is the algorithm going to consider
            var h1 = 1.0
            var h2 = 0.0
            var k1 = 0.0
            var k2 = 1.0
            var y = value.absoluteValue

            do {
                val a = floor(y)
                var aux = h1
                h1 = a * h1 + h2
                h2 = aux
                aux = k1
                k1 = a * k1 + k2
                k2 = aux
                y = 1 / (y - a)
            } while ((x - h1 / k1).absoluteValue > x * precision)

            return Fraction((mul * h1).roundToInt(), k1.roundToInt())
        }
    }
}