package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix
import model.NotSPDException

object ConjugatedGradientDescentSolver: SPDSolver {
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
                println("ERROR: left hand side is not a SPD matrix. Conjugated Gradient Descent won't work. Returning.")
                throw NotSPDException()
            }

        val errors = mutableListOf<Double>()
        var nit = 0
        var err = 1.0
        var xOld = initialGuess.copy()
        var xNew = initialGuess.copy()
        var rOld = rightHandSide - leftHandSide * xOld
        var rNew: MyMatrix
        var pOld = rOld.copy()
        var pNew: MyMatrix
        var step: Double
        var beta: Double

        while (nit < maximumIterations && err > tolerance){
            step = (pOld.transpose().dotVV(rOld)) / pOld.transpose().dotVV(leftHandSide * pOld)
            xNew = xOld + pOld * step
            rNew = rOld - leftHandSide * step * pOld
            beta = ((leftHandSide * pOld).transpose().dotVV(rNew)) / ((leftHandSide * pOld).transpose().dotVV(pOld))
            pNew = rNew - pOld * beta
            err = (rightHandSide - leftHandSide * xNew).norm2() / xNew.norm2()
            errors.add(err)
            xOld = xNew.copy()
            rOld = rNew.copy()
            pOld = pNew.copy()
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

}