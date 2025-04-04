package it.matteobarbera.solvers

import it.matteobarbera.main
import it.matteobarbera.model.MyMatrix
import model.NotTriangularException

object TrilSolver: TriXSolver {
    override fun solve(leftHandSide: MyMatrix, rightHandSide: MyMatrix): AlgorithmResult {
        if (!leftHandSide.isTril()) {
            throw NotTriangularException(false)
        }
        val guess: MyMatrix = MyMatrix.zerosVec(leftHandSide.rowDimension)
        guess[0, 0] = rightHandSide[0] / leftHandSide[0, 0]
        for (i in 1..<leftHandSide.rowDimension) {
            guess.set(
                i,
                (rightHandSide[i] - leftHandSide.getRow(i).subVec(i - 1).dotVV(guess)) / leftHandSide[i, i]
            )
        }

        return AlgorithmResult(
            solution = guess
        )
    }
}