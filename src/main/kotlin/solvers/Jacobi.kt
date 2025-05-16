package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import kotlin.time.Duration
import kotlin.time.measureTime

object Jacobi: SPDSolver {
    override var performSPDTest: Boolean = true
    var performDDTest: Boolean = true

    override fun computeApproximateSolution(
        coefficientMatrix: Matrix,
        exactSolution: Matrix,
        rightHandSide: Matrix,
        tolerance: Double,
        maximumIterations: Int
    ): AlgorithmResult {
        if (performDDTest)
            if (!coefficientMatrix.isDiagonalDominant())
                println("The coefficient matrix is not diagonal dominant. Jacobi method doesn't guarantee convergence")
        val errors = mutableListOf<Double>()
        var numberOfIterations = 0
        var error = 1.0
        val aDiagInv = !(coefficientMatrix.createDiag())
        val xOld = Matrix.xFilledVector(dimension = aDiagInv.rows, value = 0.0, asColumnVector = true)
        val xNew = xOld.copy()
        val executionTime: Duration
        measureTime {
            for (k in 0 until maximumIterations) {
                if (error <= tolerance) {
                    numberOfIterations = k
                    break
                }
                val residual = rightHandSide - (coefficientMatrix * xOld)
                val residualByDiagInv = aDiagInv * residual
                (xOld + residualByDiagInv).copyInto(xNew)
                xNew.copyInto(xOld)
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
