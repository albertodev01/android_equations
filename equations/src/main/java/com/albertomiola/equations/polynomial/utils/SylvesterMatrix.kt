package com.albertomiola.equations.polynomial.utils

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.polynomial.types.*
import com.albertomiola.equations.utils.complex.Complex
import kotlin.math.*

data class SylvesterMatrix(val polynomial: PolynomialEquation) {
    private fun buildMatrix(): ComplexMatrix {
        // Computing the derivative of the polynomial and the size of the matrix
        val coefficients = polynomial.coefficients
        val derivative = polynomial.derivative().coefficients
        val size = (coefficients.size - 1) + (derivative.size - 1)
        var pos = 0

        val flatData = Array<Complex>(size * size) { _ -> Complex() }

        for (i in 0 until size - coefficients.size + 1) {
            for (j in coefficients.indices) {
                flatData[size * i + (j + i)] = coefficients[j]
            }
        }

        for (i in size - coefficients.size + 1 until size) {
            for (j in derivative.indices) {
                flatData[size * i + (j + pos)] = derivative[j];
            }
            pos++
        }

        return ComplexMatrix(flatData.toList(), size, size)
    }

    fun matrixDeterminant() = buildMatrix().determinant()

    fun polynomialDiscriminant(): Complex {
        val quarticOrLower = polynomial is Constant ||
                polynomial is Linear ||
                polynomial is Quadratic ||
                polynomial is Cubic ||
                polynomial is Quartic

        // In case the optimization flag was 'true' and the degree of the polynomial is <= 4, then
        // go for the easy way.
        return if (quarticOrLower) {
            polynomial.discriminant()
        } else {
            // The determinant of the Sylvester matrix
            val determinant = matrixDeterminant()

            // Once we got the determinant, we need to make the last calculation to also determine
            // the sign. The formula is the following:
            //
            //  - Disc(A) = (-1)^(n*(n-1)/2) * 1/A[n] * Res(A, A')
            //
            // In the above formula, 'n' is the degree of the polynomial, A(x) is the polynomial
            // and A'(x) is the derivative of A(x). Res(A, A') is the resultant of A(x) and A'(x),
            // which is nothing more than the determinant of the Sylvester matrix.
            val coefficients = polynomial.coefficients
            val degree = coefficients.size - 1
            val sign = (-1.0).pow(degree * (degree - 1) / 2)

            // Returning the determinant with the correct sign
            Complex(sign) / coefficients.first() * determinant
        }
    }
}