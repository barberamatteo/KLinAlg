package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import model.exceptions.NotTriangularException

object TrilSolver: TriXSolver {
    override fun solve(coefficientMatrix: Matrix, rightHandSide: Matrix): AlgorithmResult {
        if (!coefficientMatrix.isTril()) {
            throw NotTriangularException(false)
        }
        val guess = Matrix.xFilledVector(coefficientMatrix.rows, 0.0, true)
        guess[0, 0] = rightHandSide[0] / coefficientMatrix[0, 0]
        for (i in 1 until coefficientMatrix.rows) {
            guess[i] =
                (rightHandSide[i] -
                        (coefficientMatrix.getRow(i)/*.prefixVector(i)*/ dot (guess/*.prefixVector(i)*/))) / coefficientMatrix[i, i]
        }

        return AlgorithmResult(
            solution = guess
        )
    }
}