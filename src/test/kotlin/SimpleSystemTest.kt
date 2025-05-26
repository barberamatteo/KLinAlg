import it.matteobarbera.model.Matrix
import it.matteobarbera.solvers.ConjugatedGradient
import it.matteobarbera.solvers.GaussSeidel
import it.matteobarbera.solvers.Gradient
import it.matteobarbera.solvers.Jacobi
import org.junit.jupiter.api.Assertions.assertArrayEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleSystemTest {

    @Test
    fun simpleSystem(){
        val vals = arrayOf(
            doubleArrayOf(6.0, 2.0, 1.0, .0),
            doubleArrayOf(2.0, 5.0, 2.0, 1.0),
            doubleArrayOf(1.0, 2.0, 4.0, 1.0),
            doubleArrayOf(.0, 1.0, 1.0, 3.0)
        )

        val rhsVals = doubleArrayOf(0.0, 0.0, 0.0, 0.5)

        val coeff = Matrix(vals)
        val rhs = Matrix(rhsVals, asColumnVector = true)
        val solver1 = Jacobi
        val solver2 = GaussSeidel
        val solver3 = Gradient
        val solver4 = ConjugatedGradient
        val sol1 = solver1.solve(
            coefficientMatrix = coeff,
            rightHandSide = rhs,
            tolerance = 1e-10,
            maximumIterations = 20000
        )
        val sol2 = solver2.solve(
            coefficientMatrix = coeff,
            rightHandSide = rhs,
            tolerance = 1e-10,
            maximumIterations = 20000
        )
        val sol3 = solver3.solve(
            coefficientMatrix = coeff,
            rightHandSide = rhs,
            tolerance = 1e-10,
            maximumIterations = 20000
        )
        val sol4 = solver4.solve(
            coefficientMatrix = coeff,
            rightHandSide = rhs,
            tolerance = 1e-10,
            maximumIterations = 20000
        )

        assertEquals(sol1.solution, sol2.solution)
        assertEquals(sol2.solution, sol3.solution)
        assertEquals(sol3.solution, sol4.solution)
    }
}