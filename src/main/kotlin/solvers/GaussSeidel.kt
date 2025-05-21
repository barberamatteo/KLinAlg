package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import it.matteobarbera.solvers.Jacobi.performDDTest
import kotlin.time.Duration
import kotlin.time.measureTime

object GaussSeidel: SPDSolver {
    override var performSPDTest = true

    val trilSolver = TrilSolver
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

        val xOld = Matrix.xFilledVector(dimension = coefficientMatrix.rows, value = 0.0, asColumnVector = true)
        val xNew = xOld.copy()
        val lowerTriangular = coefficientMatrix.tril()
        val executionTime: Duration

        measureTime {
            for (k in 0 until maximumIterations) {
                if (error <= tolerance) {
                    numberOfIterations = k
                    break
                }
                val residual = getResidual(rightHandSide, coefficientMatrix, xOld)
                val trilSystem = trilSolver.solve(lowerTriangular, residual)
                xNew < xOld + trilSystem.solution
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