package com.albertomiola.equations.polynomial.utils

import com.albertomiola.equations.polynomial.PolynomialEquation
import com.albertomiola.equations.polynomial.types.Constant
import com.albertomiola.equations.utils.complex.Complex
import com.albertomiola.equations.utils.exceptions.PolyLongDivisionException

/**
 * The "Polynomial long division" algorithm divides a polynomial by another polynomial of the same
 * or lower degree.
 *
 * The only constraint of this procedure is that the degree of the denominator cannot exceed the
 * degree of the numerator. If this condition is not satisfied, an exception is thrown.
 *
 * @author Alberto Miola
 * */
data class PolynomialLongDivision(
    var polyNumerator: PolynomialEquation,
    val polyDenominator: PolynomialEquation,
) {
    /**
     * Divides [polyNumerator] by [polyDenominator] and returns the result in a [PolynomialDivisionResult] object.
     * An exception of type [PolynomialLongDivisionException] is thrown if the degree of the
     * denominator exceeds the degree of the numerator.
     *
     * @author Alberto Miola
     * */
    fun divide(): PolynomialDivisionResult {
        if (polyNumerator.degree < polyDenominator.degree) {
            throw PolyLongDivisionException("The denominator degree cannot exceed the numerator degree")
        }

        if (polyNumerator == polyDenominator) {
            return PolynomialDivisionResult(
                quotient = Constant(Complex(1.0)),
                remainder = Constant(Complex())
            )
        }

        if (polyNumerator is Constant && polyDenominator is Constant) {
            return PolynomialDivisionResult(
                quotient = Constant(
                    polyNumerator.coefficients[0] / polyDenominator.coefficients[0]
                ),
                remainder = Constant(Complex())
            )
        }

        val numerator = polyNumerator.coefficients.reversed().toTypedArray()
        val denominator = polyDenominator.coefficients.reversed().toList()
        var numDegree = polyNumerator.degree.toInt()
        val denDegree = polyDenominator.degree.toInt()

        var tempDen = Array(numDegree + 1) { _ -> Complex() }
        val quotient = Array(denDegree + 1) { _ -> Complex() }

        // Polynomial long division algorithm.
        if (numDegree >= denDegree) {
            while (numDegree >= denDegree) {
                tempDen = Array(tempDen.size) { _ -> Complex() }

                for (i in 0..denDegree) {
                    tempDen[i + numDegree - denDegree] = denominator[i]
                }
                quotient[numDegree - denDegree] = numerator[numDegree] / tempDen[numDegree]

                for (i in 0..numDegree - denDegree + 1) {
                    tempDen[i] = tempDen[i] * quotient[numDegree - denDegree];
                }
                for (i in 0..numDegree + 1) {
                    numerator[i] = numerator[i] - tempDen[i]
                }
                numDegree--
            }
        }

        // Creating the remainder array.
        val remainder = mutableListOf<Complex>()

        // Skipping leading zeroes which will raise an exception when reversing the list contents.
        for (i in 0..numDegree) {
            if (numerator[0] == Complex()) {
                continue;
            }
            remainder.add(numerator[i])
        }

        return PolynomialDivisionResult(
            PolynomialEquation.createFromComplexList(quotient.reversed()),
            PolynomialEquation.createFromComplexList(remainder.reversed())
        )
    }
}