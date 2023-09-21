package com.albertomiola.equations.nonlinear

import com.albertomiola.equations.nonlinear.types.Newton
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class NewtonTest {
    @Test
    fun seriesConvergenceTest() {
        val newton = Newton(
            function = "sqrt(x) - e^2",
            x0 = 52.0,
            maxSteps = 6,
        )

        assertEquals(newton.maxSteps, 6)
        assertEquals(newton.tolerance, 1.0e-15)
        assertEquals(newton.function, "sqrt(x) - e^2")
        assertEquals(newton.toString(), "f(x) = sqrt(x) - e^2")
        assertEquals(newton.x0, 52.0)

        // Solving the equation, making sure that the series converged
        val solutions = newton.solve()
        assertTrue { solutions.guesses.size <= 6 }
        assertEquals(solutions.convergence, 2.0, absoluteTolerance = 1.0)
        assertEquals(solutions.efficiency, 1.0, absoluteTolerance = 1.0e-1)
        assertEquals(solutions.guesses.last(), 54.596, absoluteTolerance = 1.0e-3)
    }

    @Test
    fun malformedInputTest() {
        assertFails { Newton(2.0, "sen(x)-3") }
    }

    @Test
    fun equalityTest() {
        val newton = Newton(1.0, "x-2")

        assertEquals(Newton(1.0, "x-2"), newton)
        assertEquals(Newton(1.0, "x-2").hashCode(), newton.hashCode())
        assertNotEquals(Newton(1.01, "x-2"), newton)
        assertNotEquals(Newton(1.0, "x"), newton)
        assertNotEquals(Newton(1.0, "x-2", 1.0e-2), newton)
        assertNotEquals(Newton(1.0, "x-2", maxSteps = 30), newton)
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
        val initialGuesses = listOf(1.0, 0.6, 7.0, 2.0, 0.5)
        val expectedSolutions = listOf(0.856, 0.901, 5.0, 3.605, -1.0)

        for (i in equations.indices) {
            val solutions = Newton(
                x0 = initialGuesses[i],
                function = equations[i]
            ).solve()
            assertEquals(solutions.guesses.last(), expectedSolutions[i], absoluteTolerance = 1.0e-3)
        }
    }
}