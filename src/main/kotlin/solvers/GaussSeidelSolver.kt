package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

object GaussSeidelSolver: SPDSolver {
    private val trilSolver: TriXSolver = TrilSolver
    var performSPDTest: Boolean = true
    var performDDTest: Boolean = true


    override fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): MutableMap<String, Any> {

        val start = System.currentTimeMillis()
        val toRet = mutableMapOf<String, Any>()
        if (performSPDTest)
            if (!leftHandSide.isSPD())
                println("WARNING: leftHandSide is not SPD. Gauss-Seidel solver will not work well.")
        if (performDDTest)
            if (!leftHandSide.isDiagonalDominant())
                println("WARNING: leftHandSide is not diagonal dominant. Gauss-Seidel solver will not work well")
        val L = leftHandSide.tril()
        val B = leftHandSide - L
        var xOld = initialGuess.copy()
        var xNew = xOld.plus(1.0)
        var nit = 0
        while ((xNew - xOld).normInf() > tolerance && nit < maximumIterations) {
            xOld = xNew.copy()
            xNew = trilSolver.solve(L, rightHandSide - B * xOld)["solution"] as MyMatrix
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