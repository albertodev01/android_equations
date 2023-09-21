package com.albertomiola.equations.polynomial.utils

import com.albertomiola.equations.utils.complex.Complex

internal class ComplexMatrix(private val data: List<Complex>, var rows: Int, val columns: Int) {
    private fun getDataAt(source: List<Complex>, row: Int, col: Int) = source[columns * row + col]

    private fun getArrayDataAt(source: Array<Complex>, row: Int, col: Int) = source[columns * row + col]

    private fun setArrayDataAt(source: Array<Complex>, row: Int, col: Int, value: Complex) {
        source[columns * row + col] = value
    }

    private fun compute2x2Determinant() = data.first() * data[3] - data[1] * data[2]

    private fun compute3x3Determinant(): Complex {
        val x = data.first() *
                ((data[4] * data[8]) -
                        (data[5] * data[7]))
        val y = data[1] *
                ((data[3] * data[8]) -
                        (data[5] * data[6]))
        val z = data[2] *
                ((data[3] * data[7]) -
                        (data[4] * data[6]))

        return x - y + z
    }

    private fun compute4x4Determinant(): Complex {
        val det20101 = data.first() * data[5] -
                data[1] * data[4]
        val det20102 = data.first() * data[6] -
                data[2] * data[4]
        val det20103 = data.first() * data[7] -
                data[3] * data[4]
        val det20112 = data[1] * data[6] -
                data[2] * data[5]
        val det20113 = data[1] * data[7] -
                data[3] * data[5]
        val det20123 = data[2] * data[7] -
                data[3] * data[6]

        val det3201012 = data[8] * det20112 -
                data[9] * det20102 +
                data[10] * det20101
        val det3201013 = data[8] * det20113 -
                data[9] * det20103 +
                data[11] * det20101
        val det3201023 = data[8] * det20123 -
                data[10] * det20103 +
                data[11] * det20102
        val det3201123 = data[9] * det20123 -
                data[10] * det20113 +
                data[11] * det20112

        return (det3201123 * data[12]).negate() +
                det3201023 * data[13] -
                det3201013 * data[14] +
                det3201012 * data[15]
    }

    private fun luDecomposition(): List<ComplexMatrix> {
        val L = Array<Complex>(rows * columns) { _ -> Complex() }
        val U = Array<Complex>(rows * columns) { _ -> Complex() }

        for (i in 0 until rows) {
            for (k in i until rows) {
                var sum = Complex()
                for (j in 0 until i) {
                    sum += getArrayDataAt(L, i, j) * getArrayDataAt(U, j, k)
                }
                setArrayDataAt(U, i, k, this[i, k] - sum)
            }

            for (k in i until rows) {
                if (i == k) {
                    setArrayDataAt(L, i, i, Complex(1.0))
                } else {
                    var sum = Complex()
                    for (j in 0 until i) {
                        sum += getArrayDataAt(L, k, j) * getArrayDataAt(U, j, i)
                    }
                    setArrayDataAt(L, k, i, (this[k, i] - sum) / getArrayDataAt(U, i, i))
                }
            }
        }

        return listOf(
            ComplexMatrix(L.toList(), rows, columns),
            ComplexMatrix(U.toList(), rows, columns)
        )
    }

    operator fun get(x: Int, y: Int) = getDataAt(data, x, y)

    fun determinant(): Complex {
        if (rows * columns == 1) {
            return data.first()
        }

        if (rows * columns == 4) {
            return compute2x2Determinant()
        }

        if (rows * columns == 9) {
            return compute3x3Determinant()
        }

        if (rows * columns == 16) {
            return compute4x4Determinant()
        }

        val lu = luDecomposition()
        var prodL = Complex(1.0)
        var prodU = Complex(1.0)

        for (i in 0 until rows) {
            prodL *= lu[0][i, i]
            prodU *= lu[1][i, i]
        }

        return prodL * prodU
    }
}