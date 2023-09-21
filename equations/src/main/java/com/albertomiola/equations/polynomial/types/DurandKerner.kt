package com.albertomiola.equations.polynomial.types

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.polynomial.utils.SylvesterMatrix
import com.albertomiola.equations.utils.complex.Complex
import kotlin.math.*

class DurandKerner(
    coefficients: List<Complex>,
    val initialGuess: List<Complex> = listOf(),
    val precision: Double = 1.0e-10,
    val maxSteps: Int = 30,
) :
    PolynomialEquation(coefficients) {

    override val degree: Double
        get() = coefficients.size - 1.0

    override fun discriminant() = SylvesterMatrix(this).polynomialDiscriminant()

    override fun derivative(): PolynomialEquation {
        return when (coefficients.size) {
            1 -> Constant(coefficients[0]).derivative()
            2 -> Linear(coefficients[0], coefficients[1]).derivative()
            3 -> Quadratic(coefficients[0], coefficients[1], coefficients[2]).derivative()
            4 -> Cubic(
                coefficients[0],
                coefficients[1],
                coefficients[2],
                coefficients[3]
            ).derivative()

            5 -> Quartic(
                coefficients[0],
                coefficients[1],
                coefficients[2],
                coefficients[3],
                coefficients[4],
            ).derivative()

            6 -> {
                val coeffs = derivativeOf()
                Quartic(
                    coeffs[0],
                    coeffs[1],
                    coeffs[2],
                    coeffs[3],
                    coeffs[4],
                ).derivative()
            }

            else -> DurandKerner(derivativeOf(), initialGuess, precision, maxSteps)
        }
    }

    override fun roots(): List<Complex> {
        if (coefficients.size <= 1) {
            return listOf()
        }

        val coefficientsLength = coefficients.size
        val reversedCoeffs = coefficients.reversed()
        val realBuffer = reversedCoeffs.map { value -> value.real }.toTypedArray()
        val imaginaryBuffer = reversedCoeffs.map { value -> value.imaginary }.toTypedArray()

        var upperReal = realBuffer[coefficientsLength - 1]
        var upperComplex = imaginaryBuffer[coefficientsLength - 1]
        val squareSum = upperReal * upperReal + upperComplex * upperComplex

        upperReal /= squareSum
        upperComplex /= -squareSum

        var k1: Double
        var k2: Double
        var k3: Double

        val s = upperComplex - upperReal
        val t = upperReal + upperComplex

        for (i in 0 until coefficientsLength - 1) {
            k1 = upperReal * (realBuffer[i] + imaginaryBuffer[i])
            k2 = realBuffer[i] * s
            k3 = imaginaryBuffer[i] * t
            realBuffer[i] = k1 - k3
            imaginaryBuffer[i] = k1 + k2
        }

        realBuffer[coefficientsLength - 1] = 1.0
        imaginaryBuffer[coefficientsLength - 1] = 0.0

        // Using default values to compute the solutions. If they aren't provided,
        // we will generate default ones.
        return if (initialGuess.isNotEmpty()) {
            val real = initialGuess.map { value -> value.real }.toTypedArray()
            val complex = initialGuess.map { value -> value.imaginary }.toTypedArray()
            solve(real, complex, realBuffer, imaginaryBuffer)
        } else {
            val real = Array<Double>(coefficientsLength - 1) { _ -> 0.0 }
            val complex = Array<Double>(coefficientsLength - 1) { _ -> 0.0 }

            val bound = bound(coefficientsLength, realBuffer, imaginaryBuffer)
            val factor = bound * 0.65
            val multiplier = cos(0.25 * 2 * PI)

            for (i in 0 until coefficientsLength - 1) {
                real[i] = factor * multiplier
                complex[i] = factor * sqrt(1.0 - multiplier * multiplier)
            }

            solve(real, complex, realBuffer, imaginaryBuffer)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        return if (other is DurandKerner) {
            precision == other.precision && maxSteps == other.maxSteps
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + precision.hashCode()
        result = 31 * result + maxSteps
        return result
    }

    private fun derivativeOf(): List<Complex> {
        val newLength = coefficients.size - 1
        val newList = mutableListOf<Complex>()

        for (i in 0..newLength) {
            newList.add(i, coefficients[i] * Complex((newLength - i) * 1.0))
        }

        return newList
    }

    private fun near(a: Double, b: Double, c: Double, d: Double): Boolean {
        val qa = a - c
        val qb = b - d

        return (qa * qa + qb * qb) < precision
    }

    private fun bound(value: Int, realBuffer: Array<Double>, imagBuffer: Array<Double>): Double {
        var bound = 0.0

        for (i in 0..value) {
            val realSquare = realBuffer[i] * realBuffer[i]
            val imagSquare = imagBuffer[i] * imagBuffer[i]

            bound = max(bound, realSquare + imagSquare)
        }

        return 1.0 + sqrt(bound)
    }

    private fun solve(
        realValues: Array<Double>,
        imaginaryValues: Array<Double>,
        realBuffer: Array<Double>,
        imaginaryBuffer: Array<Double>,
    ): List<Complex> {
        val coefficientsLength = coefficients.size
        val realValuesLen = realValues.size

        // Variables setup.
        var pa: Double
        var pb: Double
        var qa: Double
        var qb: Double
        var k1: Double
        var k2: Double
        var k3: Double
        var na: Double
        var nb: Double
        var s1: Double
        var s2: Double

        // Main iteration loop of the Durand-Kerner algorithm.
        for (i in 0 until maxSteps) {
            var d = 0.0

            for (j in 0 until realValuesLen) {
                pa = realValues[j]
                pb = imaginaryValues[j]

                // Computing the denominator of type (zj - z0) * ... * (zj - z_{n-1}).
                var a = 1.0
                var b = 1.0
                for (k in 0 until realValuesLen) {
                    if (k == j) {
                        continue
                    }

                    qa = pa - realValues[k]
                    qb = pb - imaginaryValues[k]

                    // Tolerance test.
                    if (qa * qa + qb * qb < precision) {
                        continue
                    }

                    k1 = qa * (a + b)
                    k2 = a * (qb - qa)
                    k3 = b * (qa + qb)
                    a = k1 - k3
                    b = k1 + k2
                }

                // Computing the numerator.
                na = realBuffer[coefficientsLength - 1]
                nb = imaginaryBuffer[coefficientsLength - 1]
                s1 = pb - pa
                s2 = pa + pb

                for (k in coefficientsLength - 2 downTo 0) {
                    k1 = pa * (na + nb)
                    k2 = na * s1
                    k3 = nb * s2
                    na = k1 - k3 + realBuffer[k]
                    nb = k1 + k2 + imaginaryBuffer[k]
                }

                // Computing the reciprocal.
                k1 = a * a + b * b
                if (k1.absoluteValue > precision) {
                    a /= k1
                    b /= -k1
                } else {
                    a = 1.0
                    b = 0.0
                }

                // Multiplying and accumulating.
                k1 = na * (a + b)
                k2 = a * (nb - na)
                k3 = b * (na + nb)

                qa = k1 - k3
                qb = k1 + k2

                realValues[j] = pa - qa
                imaginaryValues[j] = pb - qb

                d = max(d, max(qa.absoluteValue, qb.absoluteValue))
            }

            // Exiting early if convergence is reached.
            if (d < precision) {
                break
            }
        }

        // Done! Now we need to combine together repeated roots.
        var count: Double

        for (i in 0 until realValuesLen) {
            count = 1.0
            var a = realValues[i]
            var b = imaginaryValues[i]
            for (j in 0 until realValuesLen) {
                if (i == j) {
                    continue
                }
                if (near(
                        realValues[i],
                        imaginaryValues[i],
                        realValues[j],
                        imaginaryValues[j],
                    )
                ) {
                    ++count
                    a += realValues[j]
                    b += imaginaryValues[j]
                }
            }
            if (count > 1) {
                a /= count
                b /= count
                for (j in 0 until realValuesLen) {
                    if (i == j) {
                        continue
                    }
                    if (near(
                            realValues[i],
                            imaginaryValues[i],
                            realValues[j],
                            imaginaryValues[j],
                        )
                    ) {
                        realValues[j] = a
                        imaginaryValues[j] = b
                    }
                }
                realValues[i] = a
                imaginaryValues[i] = b
            }
        }

        return realValues.mapIndexed { index, value ->
            Complex(
                real = value,
                imaginary = imaginaryValues[index]
            )
        }.toList()
    }
}