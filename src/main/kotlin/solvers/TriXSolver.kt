package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

interface TriXSolver {
    fun solve(
        leftHandSide: MyMatrix,
        rightHandSide: MyMatrix
    ): AlgorithmResult

}