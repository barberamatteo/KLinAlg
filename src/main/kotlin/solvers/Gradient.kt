package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import kotlin.time.Duration
import kotlin.time.measureTime

object Gradient: SPDSolver {
    override var performSPDTest: Boolean = true

    override fun computeApproximateSolution(
        coefficientMatrix: Matrix,
        exactSolution: Matrix,
        rightHandSide: Matrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult {
        val errors = mutableListOf<Double>()
        var numberOfIterations = 0
        var error = 1.0
        val xOld = Matrix.xFilledVector(dimension = coefficientMatrix.rows, value = 0.0, asColumnVector = true)
        val phiInput = xOld.copy()
        var numerator = 0.0
        var denominator = 0.0

        val xNew = xOld.copy()
        val executionTime: Duration
        measureTime {
            for (k in 0 until maximumIterations) {
                if (error <= tolerance) {
                    numberOfIterations = k
                    break
                }

                val residual = getResidual(rightHandSide, coefficientMatrix, xOld)
                phiInput < coefficientMatrix * residual
                numerator = residual.transpose() dot residual
                denominator = residual.transpose() dot phiInput
                xNew < (xOld + (residual * (numerator / denominator)))
                xOld < xNew
                error = (residual norm 2.0) / (rightHandSide norm 2.0)
                errors.add(error)
                numberOfIterations++
            }
        }.also { executionTime = it }
        return AlgorithmResult(
            solution = xNew,
            errors = errors,
            iterations = numberOfIterations,
            convergenceReached = numberOfIterations != maximumIterations,
            executionTime = executionTime
        )

    }
}