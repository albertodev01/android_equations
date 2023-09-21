package com.albertomiola.equations.utils

import com.albertomiola.equations.utils.fraction.Fraction
import com.albertomiola.equations.utils.fraction.isFraction

/**
 * Kotlin representation of a rational number.
 *
 * A **rational number** is a number that can be expressed as the quotient `x/y` of two integers, a
 * numerator `x` and a non-zero denominator `y`. Any rational number is also a real number.
 *
 * [Rational] and all of its sub-types are **immutable**.
 *
 * @constructor Asks for the numerator and the denominator of the rational number.
 *
 * @see [Fraction]
 *
 * @author Alberto Miola
 * */
abstract class Rational : Comparable<Rational> {
    override fun compareTo(other: Rational): Int {
        val thisDouble = toDouble()
        val otherDouble = other.toDouble()

        // Using == on floating point values isn't reliable due to potential machine precision
        // losses. Comparisons with > and < are safer.
        if (thisDouble < otherDouble) {
            return -1
        }

        if (thisDouble > otherDouble) {
            return 1
        }

        return 0
    }

    /**
     * Returns the floating point representation of this rational number.
     *
     * @author Alberto
     * */
    abstract fun toDouble(): Double

    /**
     * Changes the sign of this fraction object.
     *
     * @return A new [Rational] object whose sign is inverted.
     *
     * @author Alberto
     * */
    abstract fun negate(): Rational

    /**
     * Reduces this rational number to the lowest terms and returns a new [Rational] object.
     *
     * @author Alberto
     * */
    abstract fun reduce(): Rational

    /**
     * The dividend `a` of the `a/b` division, which also is the numerator of the associated
     * fraction.
     *
     * @author Alberto
     * */
    abstract val numerator: Int

    /**
     * The divisor `b` of the `a/b` division, which also is the denominator of the associated
     * fraction.
     *
     * @author Alberto
     * */
    abstract val denominator: Int

    /**
     * True or false whether this rational number is positive or negative.
     *
     * @author Alberto
     * */
    abstract val isNegative: Boolean

    /**
     * True or false whether this rational number is whole or not.
     *
     * @author Alberto
     * */
    abstract val isWhole: Boolean

    /**
     * A companion object for parsing rational numbers from [String]s.
     *
     * @author Alberto Miola
     * */
    companion object Parser {
        /**
         * This function tries to convert a String into a [Fraction] object. If the conversion fails,
         * `null` is returned. For example:
         *
         * ```kotlin
         * Rational.tryParse('1/2') // Fraction(1, 2);
         * Rational.tryParse('') // null
         * ```
         *
         * @return A [Rational] object if the conversion succeeds and `null` otherwise.
         *
         * @see Fraction
         *
         * @author Alberto Miola
         * */
        fun tryParse(value: String): Rational? {
            if (value.isFraction) {
                return Fraction.createFromString(value)
            }
            return null
        }
    }
}