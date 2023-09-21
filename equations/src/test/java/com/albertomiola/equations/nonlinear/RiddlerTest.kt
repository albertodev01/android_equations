package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.types.Riddler
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class RiddlerTest {
    @Test
    fun seriesConvergenceTest() {
        val riddler = Riddler(
            function = "x^3-x-2",
            a = 1.0,
            b = 2.0,
            maxSteps = 5,
        )

        assertEquals(riddler.maxSteps, 5)
        assertEquals(riddler.tolerance, 1.0e-15)
        assertEquals(riddler.function, "x^3-x-2")
        assertEquals(riddler.toString(), "f(x) = x^3-x-2")
        assertEquals(riddler.a, 1.0)
        assertEquals(riddler.b, 2.0)

        // Solving the equation, making sure that the series converged
        val solutions = riddler.solve()
        assertTrue { solutions.guesses.size <= 5 }
        assertEquals(solutions.convergence, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.efficiency, 1.0, absoluteTolerance = 1.0)
        assertEquals(solutions.guesses.last(), 1.5, absoluteTolerance = 1.0e-1)
    }

    @Test
    fun malformedInputTest() {
        assertFails { Riddler(1.0, 2.0, "x^") }
    }

    @Test
    fun equalityTest() {
        val riddler = Riddler(1.0, 2.0, "x-2")

        assertEquals(Riddler(1.0, 2.0, "x-2"), riddler)
        assertEquals(Riddler(1.0, 2.0, "x-2").hashCode(), riddler.hashCode())
        assertNotEquals(Riddler(1.1, 2.0, "x-2"), riddler)
        assertNotEquals(Riddler(1.0, 2.0001, "x-2"), riddler)
        assertNotEquals(Riddler(1.0, 2.0, "x-3"), riddler)
        assertNotEquals(Riddler(1.0, 2.0, "x-2", 1.0e-5), riddler)
        assertNotEquals(Riddler(1.0, 2.0, "x-2", maxSteps = 3), riddler)
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
            val solutions = Riddler(
                a = initialGuesses[i][0],
                b = initialGuesses[i][1],
                function = equations[i]
            ).solve()
            assertEquals(solutions.guesses.last(), expectedSolutions[i], absoluteTolerance = 1.0e-3)
        }
    }
}