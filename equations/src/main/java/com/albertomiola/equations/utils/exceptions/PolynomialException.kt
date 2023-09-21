package com.albertomiola.equations.utils.exceptions

import com.albertomiola.equations.polynomial.PolynomialEquation

/**
 * Exception object thrown by a [PolynomialEquation] object.
 *
 * @constructor Creates an exception object with the given [message].
 *
 * @author Alberto Miola
 * */
class PolynomialException(errorMessage: String) : Exception(errorMessage)