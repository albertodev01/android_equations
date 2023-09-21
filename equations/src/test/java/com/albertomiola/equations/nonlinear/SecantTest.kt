package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.types.Secant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class SecantTest {
    @Test
    fun seriesConvergenceTest() {
        val secant = Secant(
            function = "x^3-x-2",
            a = 1.0,
            b = 2.0,
            maxSteps = 5,
        )

        assertEquals(secant.maxSteps, 5)
        assertEquals(secant.tolerance, 1.0e-15)
        assertEquals(secant.function, "x^3-x-2")
        assertEquals(secant.toString(), "f(x) = x^3-x-2")
        assertEquals(secant.a, 1.0)
        assertEquals(secant.b, 2.0)

        // Solving the equation, making sure that the series converged
        val solutions = secant.solve()
        assertTrue { solutions.guesses.size <= 5 }
        assertEquals(solutions.convergence, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.efficiency, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.guesses.last(), 1.5, absoluteTolerance = 1.0e-1)
    }

    @Test
    fun malformedInputTest() {
        assertFails { Secant(1.0, 2.0, "x^") }
    }

    @Test
    fun equalityTest() {
        val secant = Secant(1.0, 2.0, "x-2")

        assertEquals(Secant(1.0, 2.0, "x-2"), secant)
        assertEquals(Secant(1.0, 2.0, "x-2").hashCode(), secant.hashCode())
        assertNotEquals(Secant(1.1, 2.0, "x-2"), secant)
        assertNotEquals(Secant(1.0, 2.0001, "x-2"), secant)
        assertNotEquals(Secant(1.0, 2.0, "x-3"), secant)
        assertNotEquals(Secant(1.0, 2.0, "x-2", 1.0e-5), secant)
        assertNotEquals(Secant(1.0, 2.0, "x-2", maxSteps = 3), secant)
    }

    @Test
    fun batchTests() {
        val equations = listOf(
            "x^e-cos(x)",
            "3*x-sqrt(x+2)-1",
            "x^3-5*x^2",
            "x^2-13",
            "e^(x)*(x+1)"
        )
        val initialGuesses = listOf(
            listOf(0.5, 1.0),
            listOf(-1.0, 1.0),
            listOf(4.0, 6.0),
            listOf(3.0, 4.0),
            listOf(-2.0, 0.0),
        )
        val expectedSolutions = listOf(0.856, 0.901, 5.0, 3.605, -1.0)

        for (i in equations.indices) {
            val solutions = Secant(
                a = initialGuesses[i][0],
                b = initialGuesses[i][1],
                function = equations[i]
            ).solve()
            assertEquals(solutions.guesses.last(), expectedSolutions[i], absoluteTolerance = 1.0e-3)
        }
    }
}