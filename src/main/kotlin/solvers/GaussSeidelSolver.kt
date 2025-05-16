package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

object GaussSeidelSolver/*: SPDSolverOld {
    private val trilSolver: TriXSolver = TrilSolver
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
                println("WARNING: leftHandSide is not SPD. Gauss-Seidel solver will not work well.")
        if (performDDTest)
            if (!leftHandSide.isDiagonalDominant())
                println("WARNING: leftHandSide is not diagonal dominant. Gauss-Seidel solver will not work well")
        val L = leftHandSide.tril()
        val B = leftHandSide - L
        val errors = mutableListOf<Double>()
        var xOld = initialGuess.copy()
        var xNew = xOld.plus(1.0)
        var nit = 0
        while ((xNew - xOld).normInf() > tolerance && nit < maximumIterations) {
            xOld = xNew.copy()
            xNew = trilSolver.solve(L, rightHandSide - B * xOld).solution
            nit++
            errors.add(xNew.minus(xOld).normInf())

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
}*/