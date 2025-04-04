package io

import it.matteobarbera.io.MtxFileParser.parse
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.AlgorithmResult
import it.matteobarbera.solvers.ConjugatedGradientDescentSolver
import it.matteobarbera.solvers.GaussSeidelSolver
import it.matteobarbera.solvers.JacobiSolver
import kotlin.test.Test

class WriterTest {
    private val solver = JacobiSolver

    @Test
    fun writeSolution(){
        val LHS = parse("src/test/resources/dati/spa2.mtx")
        val LHSDim = LHS.rowDimension

        solver.performSPDTest = false
        val sol: AlgorithmResult = solver.solve(
            LHS,
            MyMatrix.onesVec(LHSDim),
            MyMatrix.zerosVec(LHSDim),
            10e-7,
            1000
        )
        sol.writeAsMtxFile("src/test/resources/spa2_sol.mtx")
    }
}