package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

interface SPDSolver {
    fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix,
        initialGuess: MyMatrix,
        tolerance: Double,
        maximumIterations: Int
    ): MutableMap<String, Any>
}