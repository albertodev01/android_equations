package com.albertomiola.equations.nonlinear.utils

import com.albertomiola.equations.nonlinear.NonLinear

/**
 * Data class that holds the result returned by [NonLinear.solve]
 *
 * @property guesses The list of values generated by the algorithm on each step.
 * @property convergence The convergence rate of the algorithm.
 * @property efficiency The efficiency of the algorithm.
 *
 * @author Alberto Miola
 * */
data class NonLinearResult(
    val guesses: List<Double>,
    val convergence: Double,
    val efficiency: Double,
)