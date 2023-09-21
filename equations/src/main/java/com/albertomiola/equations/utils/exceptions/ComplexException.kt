package com.albertomiola.equations.utils.exceptions

import com.albertomiola.equations.utils.complex.Complex

/**
 * Exception object thrown by a [Complex] object.
 *
 * @constructor Creates an exception object with the given [message].
 *
 * @author Alberto Miola
 * */
class ComplexException(errorMessage: String) : Exception(errorMessage)