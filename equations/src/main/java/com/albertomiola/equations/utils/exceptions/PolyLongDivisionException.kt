package com.albertomiola.equations.utils.exceptions

import com.albertomiola.equations.polynomial.utils.PolynomialLongDivision

/**
 * Exception object thrown by a [PolynomialLongDivision] object.
 *
 * @constructor Creates an exception object with the given [message].
 *
 * @author Alberto Miola
 * */
class PolyLongDivisionException(message: String) : Exception(message)