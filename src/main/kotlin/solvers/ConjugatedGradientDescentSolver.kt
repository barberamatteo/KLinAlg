package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

object ConjugatedGradientDescentSolver: SPDSolver {
    override fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): MutableMap<String, Any> {
        val start = System.currentTimeMillis()
        val toRet = mutableMapOf<String, Any>()
        if (!leftHandSide.isSPD()) {
            println("ERROR: left hand side is not a SPD matrix. Conjugated Gradient Descent won't work. Returning.")
            return mutableMapOf()
        }

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
            xOld = xNew.copy()
            rOld = rNew.copy()
            pOld = pNew.copy()
            nit++
        }
        val end = System.currentTimeMillis()
        val elapsed = end - start
        toRet["solution"] = xNew
        toRet["iterations"] = nit
        toRet["convergenceReachedByTolerance"] = !(err > tolerance && nit == maximumIterations)
        toRet["executionTime"] = elapsed
        return toRet
    }

}