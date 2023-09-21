package com.albertomiola.equations.utils.fraction

import com.albertomiola.equations.utils.exceptions.FractionException

/**
 * Extension function on [Int] that converts an integer number into a [Fraction] object.
 *
 * @author Alberto Miola
 * */
fun Int.toFraction(): Fraction = Fraction(this)

/**
 * Extension function on [Double] that converts a floating-point number into a [Fraction] object.
 *
 * @exception FractionException if [this] is NaN or Infinite.
 *
 * @author Alberto Miola
 * */
fun Double.toFraction(): Fraction = Fraction.createFromDouble(this)

/**
 * Extension function on [String] that converts a string into a [Fraction] object.
 *
 * @exception FractionException if [this] is does not represent a fraction.
 *
 * @author Alberto Miola
 * */
fun String.toFraction(): Fraction = Fraction.createFromString(this)

/**
 * Checks whether the String contains a valid representation of a fraction in the 'a/b' format or
 * not. For example, this getter returns `true` if the String is `'1/4'`.
 *
 * @return True if the String represents a fraction, false otherwise.
 *
 * @author Alberto Miola
 * */
val String.isFraction: Boolean
    get() {
        return try {
            Fraction.createFromString(this)
            true
        } catch (_: Exception) {
            false
        }
    }