package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix

interface TriXSolver {
    fun solve(
        coefficientMatrix: Matrix,
        rightHandSide: Matrix
    ): AlgorithmResult

}