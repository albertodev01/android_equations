package com.albertomiola.equations.utils.complex

import com.albertomiola.equations.utils.exceptions.ComplexException
import com.albertomiola.equations.utils.fraction.Fraction
import com.albertomiola.equations.utils.fraction.toFraction
import kotlin.math.*

/**
 * A Kotlin representation of a complex number in the form `a + bi` where `a` is the real part and
 * `bi` is the imaginary (or complex) part.
 *
 * A [Complex] object is **immutable**.
 *
 * @constructor Creates a complex number with the given real and imaginary parts.
 *
 * @author Alberto Miola
 * */
class Complex(val real: Double, val imaginary: Double) : Comparable<Complex> {
    /**
     * This is the same as calling `Complex(0, 0)`.
     *
     * @constructor Assigns `0` to the real and imaginary part of the complex number.
     *
     * @author Alberto Miola
     * */
    constructor() : this(0.0, 0.0)

    constructor(realPart: Double) : this(realPart, 0.0)

    constructor(realPart: Int) : this(realPart.toDouble(), 0.0)

    constructor(realPart: Int, complexPart: Int) : this(realPart.toDouble(), complexPart.toDouble())

    /**
     * Returns whether the complex number is zero or not.
     *
     * @author Alberto Miola
     * */
    val isZero: Boolean get() = real == 0.0 && imaginary == 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return if (other is Complex) {
            real == other.real && imaginary == other.imaginary
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imaginary.hashCode()
        return result
    }

    override fun compareTo(other: Complex): Int {
        // Performing == on floating point values is not numerically stable.
        //
        // Instead, '>' and '<' are more reliable in terms of machine precision so
        // 0 is just a fallback.
        if (abs() > other.abs()) {
            return 1
        }
        if (abs() < other.abs()) {
            return -1
        }
        return 0
    }

    override fun toString() = getStringRepresentation()

    fun copy(real: Double? = null, imaginary: Double? = null): Complex {
        return Complex(real ?: this.real, imaginary ?: this.imaginary)
    }

    /**
     * Returns the real and the imaginary parts of the complex number as fractions with the best
     * possible approximation.
     *
     * @see Fraction.createFromDouble
     *
     * @author Alberto Miola
     * */
    fun toStringAsFraction() = getStringRepresentation(true)

    /**
     * When a value is a whole number, it's returned without the fractional part. For example,
     * calling `_fixZero(5.0)` returns `"5"`.
     *
     * @author Alberto Miola
     * */
    private fun stringNumberFixZero(value: Double): String {
        val result = value.toString()
        return if (result.endsWith(".0")) result.replace(".0", "") else result
    }

    /**
     * Converts this complex number into a string. If [asFraction] is `true` then the real and the
     * imaginary part are converted into fractions.
     *
     * @author Alberto Miola
     * */
    private fun getStringRepresentation(asFraction: Boolean = false): String {
        var realPart = stringNumberFixZero(real);
        var imaginaryPart = "${stringNumberFixZero(imaginary)}i";
        var imaginaryPartAbs = "${stringNumberFixZero(imaginary.absoluteValue)}i"

        if (asFraction) {
            realPart = real.toFraction().toString()
            imaginaryPart = "${imaginary.toFraction()}i"
            imaginaryPartAbs = "${imaginary.absoluteValue.toFraction()}i"
        }

        if (real == 0.0) return imaginaryPart
        if (imaginary == 0.0) return realPart
        if (imaginary < 0) return "$realPart - $imaginaryPartAbs"

        return "$realPart + $imaginaryPart"
    }

    /**
     * The polar form of a complex number is another way of representing complex numbers. The form
     * `z = a + bi` is the rectangular form of a complex number, where (a, b) are the rectangular
     * coordinates. The polar form of a complex number is represented in terms of modulus and
     * argument of the complex number.
     *
     * @return A [PolarComplex] object that represents this complex number in polar coordinates.
     *
     * @author Alberto Miola
     * */
    fun toPolarCoordinates() = PolarComplex(abs(), phase(), phase() * 180 / PI)

    /**
     * Returns the square root of _(a<sup>2</sup> + b<sup>2</sup>)_ where `a` is the real part and
     * `b` is the imaginary part.
     *
     * This is is the modulus/magnitude/absolute value of the complex number.
     *
     * @author Alberto Miola
     * */
    fun abs(): Double {
        if ((real != 0.0) || (imaginary != 0.0)) {
            return sqrt(real * real + imaginary * imaginary)
        }
        return 0.0
    }

    /**
     * Converts rectangular coordinates to polar coordinates by computing an arc tangent of y/x in +
     * the range from -pi to pi.
     *
     * @return The phase of the complex number.
     *
     * @author Alberto Miola
     * */
    fun phase() = atan2(imaginary, real)

    /**
     * Changes the sign of the imaginary part of current object and returns a new [Complex] object.
     *
     * @author Alberto Miola
     * */
    fun conjugate() = Complex(real, -imaginary)

    /**
     * Finds the multiplicative inverse (reciprocal) of the current instance and returns the result
     * in a new [Complex] object.
     *
     * If you let `z` be a complex in the standard form of `z = a + bi` then the reciprocal is
     * `1/z`, which can also be written as:
     *  - 1 / (a + bi)
     *  - (x - yi) / (x<sup>2</sup> + y<sup>2</sup>)
     *  - conj(a + bi) / modulo(a + bi)<sup>2</sup>
     *
     *  @throws ComplexException when [isZero] is true, because the reciprocal of 0i is undefined.
     *
     *  @author Alberto Miola
     * */
    fun reciprocal(): Complex {
        if (isZero) {
            throw ComplexException("The reciprocal of zero is undefined.")
        }

        val scale = real * real + imaginary * imaginary
        return Complex(real / scale, -imaginary / scale)
    }

    /**
     * Returns the square root of this complex number.
     *
     * @author Alberto Miola
     * */
    fun sqrt(): Complex {
        if (imaginary == 0.0 && real > 0) {
            return Complex(sqrt(real), 0.0)
        }

        val r = sqrt(abs())
        val theta = phase() / 2
        return Complex(r * cos(theta), r * sin(theta))
    }

    /**
     * Returns the power having a complex number as base and a real value as exponent. The
     * expression is in the form _(a + bi)<sup>x</sup>_.
     *
     * @author Alberto Miola
     * */
    fun pow(value: Double): Complex {
        val logIm = value * phase()
        val modAns = exp(value * ln(abs()))

        return Complex(modAns * cos(logIm), modAns * sin(logIm))
    }

    /**
     * Computes the complex n-th root of this object. The returned root is the one with the smallest
     * positive argument.
     *
     * @author Alberto Miola
     * */
    fun nthRoot(nth: Int): Complex {
        if (nth == 1) {
            return copy()
        }

        var neg = false
        var n = nth
        var a: Double
        var b: Double

        if (n < 0) {
            n = -n
            neg = true
        }
        if (n == 0) {
            a = 1.0
            b = 0.0
        } else {
            if (n == 1) {
                a = real
                b = imaginary
            } else {
                var length = abs()
                var angle = phase()

                if (angle < 0) {
                    angle += PI * 2
                }

                length = length.pow(1.0 / n)
                angle /= n

                a = length * cos(angle)
                b = length * sin(angle)
            }
        }

        if (neg) {
            val den = a * a + b * b
            a /= den
            b = -b / den
        }

        return Complex(a, b)
    }

    fun negate() = Complex(-real, -imaginary)

    /**
     * The sum of two complex numbers.
     *
     * @return A [Complex] object that is the sum of this and [other] complex numbers.
     *
     * @author Alberto
     * */
    operator fun plus(other: Complex) = Complex(real + other.real, imaginary + other.imaginary)

    /**
     * The difference between two complex numbers.
     *
     * @return A [Complex] object that is the difference between this and [other] complex numbers.
     *
     * @author Alberto
     * */
    operator fun minus(other: Complex) = Complex(real - other.real, imaginary - other.imaginary)

    /**
     * The product of two complex numbers.
     *
     * @return A [Complex] object that is the product of this and [other] complex numbers.
     *
     * @author Alberto
     * */
    operator fun times(other: Complex): Complex {
        val realPart = real * other.real - imaginary * other.imaginary
        val imagPart = real * other.imaginary + imaginary * other.real
        return Complex(realPart, imagPart)
    }

    /**
     * The division of two complex numbers.
     *
     * @return A [Complex] object that is the division of this and [other] complex numbers.
     *
     * @author Alberto
     * */
    operator fun div(other: Complex) = this * other.reciprocal()

    operator fun unaryMinus() = negate()

    operator fun component1(): Double = real

    operator fun component2(): Double = imaginary

    /**
     *  A factory that creates new [Complex] objects from various object types.
     *
     * @see createFromFraction
     * @see createFromPolarCoordinates
     *
     * @author Alberto
     * */
    companion object Factory {
        /**
         * Creates the `i` number, which is the same as `Complex(0, 1)`.
         *
         * @author Alberto Miola
         * */
        fun i() = Complex(0, 1)

        /**
         * Creates a complex number from a [Fraction] objects.
         *
         * @author Alberto Miola
         * */
        fun createFromFraction(real: Fraction, imaginary: Fraction): Complex {
            return Complex(real.toDouble(), imaginary.toDouble())
        }

        /**
         * Converts an angle from degrees to radians.
         *
         * @author Alberto Miola
         * */
        private fun degToRad(value: Double): Double = value * PI / 180

        /**
         * Creates a complex number from the given polar coordinates where [r] is the radius and
         * [theta] is the angle.
         *
         * By default, the angle [theta] must be expressed in *radians* but `angleInRadians = false`
         * allows the value to be in degrees.
         *
         * @return A [Complex] object taht is built from polar coordinates.
         *
         * @author Alberto Miola
         * */
        fun createFromPolarCoordinates(
            r: Double,
            theta: Double,
            angleInRadians: Boolean = true,
        ): Complex {
            val real = r * cos(if (angleInRadians) theta else degToRad(theta))
            val imaginary = r * sin(if (angleInRadians) theta else degToRad(theta))
            return Complex(real, imaginary)
        }
    }
}