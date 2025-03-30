package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

object GradientDescentSolver: SPDSolver {
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
            println("ERROR: left hand side is not a SPD matrix. Gradient Descent won't work. Returning.")
            return mutableMapOf()
        }
        var nit = 0
        var err = 1.0
        var xOld = initialGuess.copy()
        var xNew = xOld.copy()
        while (nit < maximumIterations && err > tolerance) {
            val residual = rightHandSide - leftHandSide * xOld
            val step = (residual.transpose().dotVV(residual)) / (residual.transpose().dotVV(leftHandSide * residual))
            xNew = xOld + residual * step
            err = ( (rightHandSide - leftHandSide * xNew).norm2() / xNew.norm2())
            xOld = xNew.copy()
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