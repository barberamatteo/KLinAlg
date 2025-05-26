package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix

interface SPDSolver{
    var performSPDTest: Boolean


    fun computeApproximateSolution(
        coefficientMatrix: Matrix,
        exactSolution: Matrix,
        rightHandSide: Matrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult

    fun solve(
        coefficientMatrix: Matrix,
        tolerance: Double,
        maximumIterations: Int,
        exactSolution: Matrix = createExactSolution(size = coefficientMatrix.rows),
        rightHandSide: Matrix = createRightHandSide(
            coefficientMatrix = coefficientMatrix,
            xVector = exactSolution
        )): AlgorithmResult{
        return computeApproximateSolution(
            coefficientMatrix,
            exactSolution,
            rightHandSide,
            tolerance,
            maximumIterations
        )
    }



    private fun createExactSolution(size: Int): Matrix{
        return Matrix.xFilledVector(
            dimension = size,
            value = 1.0,
            asColumnVector = true
        )
    }

    private fun createRightHandSide(coefficientMatrix: Matrix, xVector: Matrix): Matrix{
        return coefficientMatrix * xVector
    }

    fun getResidual(rightHandSide: Matrix, coefficientMatrix: Matrix, xOld: Matrix): Matrix{
        return rightHandSide - (coefficientMatrix * xOld)
    }


}