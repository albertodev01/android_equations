package com.albertomiola.equations.utils.complex

/**
 * Kotlin representation of a complex number in polar coordinates.
 *
 * @property r The absolute value/modulus of the complex number
 * @property phiRadians The 'phi' angle expressed in radians
 * @property phiDegrees The 'phi' angle expressed in degrees.
 *
 * @author Alberto Miola
 * */
data class PolarComplex(val r: Double, val phiRadians: Double, val phiDegrees: Double)