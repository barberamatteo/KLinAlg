package algo

import it.matteobarbera.io.MtxFileParser.parse
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.ConjugatedGradientDescentSolver
import java.io.IOException
import kotlin.test.Test

class ConjugatedGradientDescentSolverTest {

    private val solver = ConjugatedGradientDescentSolver
    init{
        solver.performSPDTest = false
    }
    @Test
    @Throws(IOException::class)
    fun withSPDMatrixFromFile_spa1() {
        val LHS = parse("src/test/resources/spa1.mtx")
        val LHSDim = LHS.rowDimension




        val sol = solver.solve(
            LHS,
            MyMatrix.onesVec(LHSDim),
            MyMatrix.zerosVec(LHSDim),
            10e-7,
            20000
        )
        println("Iterations: " + sol.iterations)
        println("Convergence reached by tolerance: " + sol.convergenceReached)
        println("Execution time: " + sol.executionTime + " ms")
        val xFinal = sol.solution
        println("Solution (as a row vector):")
        xFinal.transpose().print(0, 17)
    }
}