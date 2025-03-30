package algo

import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.TrilSolver
import kotlin.test.Test
import kotlin.test.assertEquals

class TriangularTest {
    private val solver = TrilSolver

    @Test
    fun solveTril() {
        val vals = arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0, 0.0),
            doubleArrayOf(2.0, 3.0, 0.0, 0.0),
            doubleArrayOf(3.0, 8.0, 5.0, 0.0),
            doubleArrayOf(4.0, 4.0, 4.0, 4.0)
        )
        val rhsVals = arrayOf(
            doubleArrayOf(1.0, 2.0, 9.0, 18.0)
        )
        val matrix = MyMatrix.constructWithCopy(vals)
        val rhs = MyMatrix.constructWithCopy(rhsVals)

        val expectedSolution = arrayOf(
            doubleArrayOf(1.0, 0.0, 1.2, 2.3)
        )
        val sol = solver.solve(matrix, rhs)

        val expected = MyMatrix.constructWithCopy(expectedSolution)
        val solution = sol["solution"] as MyMatrix
        solution.print(0, 1)
        assertEquals(solution.norm2(), expected.norm2(), 0.001)
    }
}