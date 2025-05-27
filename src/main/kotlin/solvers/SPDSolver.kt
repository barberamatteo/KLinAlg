package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix

interface SPDSolver{
    var performSPDTest: Boolean


    fun computeApproximateSolution(
        coefficientMatrix: Matrix,
        rightHandSide: Matrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult

    fun solve(
        coefficientMatrix: Matrix,
        tolerance: Double,
        maximumIterations: Int,
        rightHandSide: Matrix
    ): AlgorithmResult{
        return computeApproximateSolution(
            coefficientMatrix,
            rightHandSide,
            tolerance,
            maximumIterations
        )
    }



    fun getResidual(rightHandSide: Matrix, coefficientMatrix: Matrix, xOld: Matrix): Matrix{
        return rightHandSide - (coefficientMatrix * xOld)
    }


}