package algo

import it.matteobarbera.io.MtxFileParser.parse
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.AlgorithmResult
import it.matteobarbera.solvers.GaussSeidelSolver
import java.io.IOException
import kotlin.test.Test

class GaussSeidelTest {

    private val solver = GaussSeidelSolver
    init {
        solver.performSPDTest = false
        solver.performDDTest = false
    }
    @Test
    @Throws(IOException::class)
    fun withDD_SPDMatrix() {
        val spdVals = arrayOf(
            doubleArrayOf(2.0, -1.0, 0.0),
            doubleArrayOf(-1.0, 2.0, -1.0),
            doubleArrayOf(0.0, -1.0, 2.0)
        )

        val rhsVals = arrayOf(
            doubleArrayOf(10.0, 20.0, 30.0)
        )
        val spd = MyMatrix.constructWithCopy(spdVals)
        val rhs = MyMatrix.constructWithCopy(rhsVals).transpose()
        val sol = solver.solve(spd, rhs, MyMatrix.zerosVec(3), 10e-14, 20000)
        sol.solution.print(0, 17)
        println("Elapsed " + sol.executionTime + " ms")
    }

    @Test
    @Throws(IOException::class)
    fun withSPDMatrixFromFile_spa1() {
        val LHS = parse("src/test/resources/spa1.mtx")
        val LHSDim = LHS.rowDimension


        val sol: AlgorithmResult = solver.solve(
            LHS,
            MyMatrix.onesVec(LHSDim),
            MyMatrix.zerosVec(LHSDim),
            10e-14,
            1000
        )
        println("Iterations: " + sol.iterations)
        println("Convergence reached by tolerance: " + sol.convergenceReached)
        println("Execution time: " + sol.executionTime + " ms")
        val xFinal = sol.solution
        println("Solution (as a row vector):")
        xFinal.transpose().print(0, 17)
    }
}