package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

object JacobiSolver: SPDSolver {
    override fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): MutableMap<String, Any> {
        val start = System.currentTimeMillis()
        val toRet = mutableMapOf<String, Any>()
        if (!leftHandSide.isSPD())
            println("WARNING: leftHandSide is not SPD. Jacobi solver will not work well.")
        if (!leftHandSide.isDiagonalDominant())
            println("WARNING: leftHandSide is not diagonal dominant. Jacobi solver will not work well")
        val diag = leftHandSide.diag()
        val B = diag.minus(leftHandSide)

        var xOld = initialGuess.copy()
        var xNew = xOld.plus(1.0)
        var nit = 0
        val invDiag = diag.invDiag()
        while (xNew.minus(xOld).normInf() > tolerance && nit < maximumIterations) {
            xOld = xNew.copy()
            B.times(xOld)
            xNew = invDiag.times(B.times(xOld).plus(rightHandSide))
            nit++
        }
        val end = System.currentTimeMillis()
        val elapsed = end - start
        toRet["solution"] = xNew
        toRet["iterations"] = nit
        toRet["convergenceReachedByTolerance"] = !(xNew.minus(xOld).normInf() > tolerance && nit == maximumIterations)
        toRet["executionTime"] = elapsed
        return toRet
    }
}