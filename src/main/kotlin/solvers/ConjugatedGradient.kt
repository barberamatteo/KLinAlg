package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import kotlin.time.Duration
import kotlin.time.measureTime

object ConjugatedGradient: SPDSolver {
    override var performSPDTest = true

    override fun computeApproximateSolution(
        coefficientMatrix: Matrix,
        rightHandSide: Matrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult {
        val errors = mutableListOf<Double>()
        var numberOfIterations = 0
        var error = 1.0
        val xOld = Matrix.xFilledVector(
            dimension = coefficientMatrix.rows,
            value = 0.0,
            asColumnVector = true
        )
        val residualOld = getResidual(
            rightHandSide,
            coefficientMatrix,
            xOld
        )
        val direction = residualOld.copy()
        val residualNew = residualOld.copy()
        var beta: Double
        var numerator: Double
        var denominator: Double
        var alpha: Double
        val xNew = xOld.copy()
        val y = xOld.copy()
        val executionTime: Duration
        measureTime {
            for (k in 0 until maximumIterations) {
                if (error <= tolerance) {
                    numberOfIterations = k
                    break
                }
                residualOld < getResidual(
                    rightHandSide,
                    coefficientMatrix,
                    xOld
                )
                y < coefficientMatrix * direction
                numerator = +direction dot residualOld
                denominator = +direction dot y
                alpha = numerator / denominator
                xNew < xOld + direction * alpha
                xOld < xNew
                residualNew < residualOld - (y * alpha)
                beta = (+y dot residualNew) / (+y dot direction)
                direction < residualNew - direction * beta
                error = (residualOld norm 2.0) / (rightHandSide norm 2.0)
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