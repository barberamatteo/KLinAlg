package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix
import model.exceptions.NotSPDException

object GradientDescentSolver/*: SPDSolverOld {
    override var performSPDTest: Boolean = true

    override fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult {
        val start = System.currentTimeMillis()
        if (performSPDTest)
            if (!leftHandSide.isSPD()) {
                println("ERROR: left hand side is not a SPD matrix. Gradient Descent won't work. Returning.")
                throw NotSPDException()
            }
        val errors = mutableListOf<Double>()
        var nit = 0
        var err = 1.0
        var xOld = initialGuess.copy()
        var xNew = xOld.copy()
        while (nit < maximumIterations && err > tolerance) {
            val residual = rightHandSide - leftHandSide * xOld
            val step = (residual.transpose().dotVV(residual)) / (residual.transpose().dotVV(leftHandSide * residual))
            xNew = xOld + residual * step
            err = ( (rightHandSide - leftHandSide * xNew).norm2() / xNew.norm2())
            errors.add(err)
            xOld = xNew.copy()
            nit++
        }

        val end = System.currentTimeMillis()
        val elapsed = end - start
        return AlgorithmResult(
            solution = xNew,
            errors = errors,
            iterations = nit,
            convergenceReached = !(err > tolerance && nit == maximumIterations),
            executionTime = elapsed
        )

    }

}*/