package it.matteobarbera.solvers

import com.sun.org.apache.xpath.internal.operations.Bool
import it.matteobarbera.model.MyMatrix

object JacobiSolver: SPDSolver {
    override var performSPDTest: Boolean = true
    var performDDTest: Boolean = true
    override fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult {
        val start = System.currentTimeMillis()
        if (performSPDTest)
            if (!leftHandSide.isSPD())
                println("WARNING: leftHandSide is not SPD. Jacobi solver will not work well.")
        if (performDDTest)
            if (!leftHandSide.isDiagonalDominant())
                println("WARNING: leftHandSide is not diagonal dominant. Jacobi solver will not work well")
        val diag = leftHandSide.diag()
        val B = diag.minus(leftHandSide)

        val errors = mutableListOf<Double>()
        var xOld = initialGuess.copy()
        var xNew = xOld.plus(1.0)
        var nit = 0
        val invDiag = diag.invDiag()
        var err = 1.0
        while (err > tolerance && nit < maximumIterations) {
            xOld = xNew.copy()
            xNew = invDiag.times(B.times(xOld).plus(rightHandSide))
            nit++
            err = xNew.minus(xOld).normInf()
            errors.add(err)
        }
        val end = System.currentTimeMillis()
        val elapsed = end - start
        return AlgorithmResult(
            solution = xNew,
            errors = errors,
            iterations = nit,
            convergenceReached = !(xNew.minus(xOld).normInf() > tolerance && nit == maximumIterations),
            executionTime = elapsed
        )
    }
}