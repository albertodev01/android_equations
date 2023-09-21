package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.types.Chords
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class ChordsTest {
    @Test
    fun seriesConvergenceTest() {
        val chords = Chords(
            function = "cos(x+3)-ln(x)+2",
            a = 0.1,
            b = 7.0,
            maxSteps = 5,
        )

        assertEquals(chords.maxSteps, 5)
        assertEquals(chords.tolerance, 1.0e-15)
        assertEquals(chords.function, "cos(x+3)-ln(x)+2")
        assertEquals(chords.toString(), "f(x) = cos(x+3)-ln(x)+2")
        assertEquals(chords.a, 0.1)
        assertEquals(chords.b, 7.0)

        // Solving the equation, making sure that the series converged
        val solutions = chords.solve()
        assertTrue { solutions.guesses.size <= 5 }
        assertEquals(solutions.convergence, 0.5, absoluteTolerance = 1.0)
        assertEquals(solutions.efficiency, 0.8, absoluteTolerance = 1.0e-1)
        assertEquals(solutions.guesses.last(), 5.0, absoluteTolerance = 1.0e-1)
    }

    @Test
    fun malformedInputTest() {
        assertFails { Chords(1.0, 2.0, "sen(x)-3") }
    }

    @Test
    fun equalityTest() {
        val chords = Chords(1.0, 2.0, "x-2")

        assertEquals(Chords(1.0, 2.0, "x-2"), chords)
        assertEquals(Chords(1.0, 2.0, "x-2").hashCode(), chords.hashCode())
        assertNotEquals(Chords(1.1, 2.0, "x-2"), chords)
        assertNotEquals(Chords(1.0, 2.0001, "x-2"), chords)
        assertNotEquals(Chords(1.0, 2.0, "x-3"), chords)
        assertNotEquals(Chords(1.0, 2.0, "x-2", 1.0e-5), chords)
        assertNotEquals(Chords(1.0, 2.0, "x-2", maxSteps = 3), chords)
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
        val expectedSolutions = listOf(0.856, 0.901, 5.0, 3.605, -1.0234)

        for (i in equations.indices) {
            val solutions = Chords(
                a = initialGuesses[i][0],
                b = initialGuesses[i][1],
                function = equations[i]
            ).solve()
            assertEquals(solutions.guesses.last(), expectedSolutions[i], absoluteTolerance = 1.0e-3)
        }
    }
}