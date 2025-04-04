package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

interface SPDSolver {
    var performSPDTest: Boolean
    fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult
}