package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.types.Brent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class BrentTest {
    @Test
    fun seriesConvergenceTest() {
        val brent = Brent(
            function = "x^3-x-2",
            a = 1.0,
            b = 2.0,
            maxSteps = 5,
        )

        assertEquals(brent.maxSteps, 5)
        assertEquals(brent.tolerance, 1.0e-15)
        assertEquals(brent.function, "x^3-x-2")
        assertEquals(brent.toString(), "f(x) = x^3-x-2")
        assertEquals(brent.a, 1.0)
        assertEquals(brent.b, 2.0)

        // Solving the equation, making sure that the series converged
        val solutions = brent.solve()
        assertTrue { solutions.guesses.size <= 5 }
        assertEquals(solutions.convergence, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.efficiency, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.guesses.last(), 1.5, absoluteTolerance = 1.0e-1)
    }

    @Test
    fun malformedInputTest() {
        assertFails { Brent(1.0, 2.0, "sqtr(x)-3") }
    }

    @Test
    fun equalityTest() {
        val brent = Brent(1.0, 2.0, "x-2")

        assertEquals(Brent(1.0, 2.0, "x-2"), brent)
        assertEquals(Brent(1.0, 2.0, "x-2").hashCode(), brent.hashCode())
        assertNotEquals(Brent(1.1, 2.0, "x-2"), brent)
        assertNotEquals(Brent(1.0, 2.0001, "x-2"), brent)
        assertNotEquals(Brent(1.0, 2.0, "x-3"), brent)
        assertNotEquals(Brent(1.0, 2.0, "x-2", 1.0e-5), brent)
        assertNotEquals(Brent(1.0, 2.0, "x-2", maxSteps = 3), brent)
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
            listOf(4.95, 5.25),
            listOf(3.0, 4.0),
            listOf(-1.5, 0.9),
        )
        val expectedSolutions = listOf(0.856, 0.901, 5.0, 3.605, -1.0)

        for (i in equations.indices) {
            val solutions = Brent(
                a = initialGuesses[i][0],
                b = initialGuesses[i][1],
                function = equations[i]
            ).solve()
            assertEquals(solutions.guesses.last(), expectedSolutions[i], absoluteTolerance = 1.0e-3)
        }
    }
}