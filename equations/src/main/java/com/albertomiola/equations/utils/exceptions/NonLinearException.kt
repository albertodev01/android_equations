package com.albertomiola.equations.utils.exceptions

import com.albertomiola.equations.nonlinear.NonLinear

/**
 * Exception object thrown by a [NonLinear] object.
 *
 * @constructor Creates an exception object with the given [message].
 *
 * @author Alberto Miola
 * */
class NonLinearException(errorMessage: String) : Exception(errorMessage)