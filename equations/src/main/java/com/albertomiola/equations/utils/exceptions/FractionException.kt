package com.albertomiola.equations.utils.exceptions

import com.albertomiola.equations.utils.fraction.*

/**
 * Exception object thrown by a [Fraction] object.
 *
 * @constructor Creates an exception object with the given [message].
 *
 * @author Alberto Miola
 * */
class FractionException(errorMessage: String) : Exception(errorMessage)