package it.matteobarbera

import it.matteobarbera.charts.Chart
import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.ConjugatedGradientDescentSolver
import it.matteobarbera.solvers.GaussSeidelSolver
import it.matteobarbera.solvers.GradientDescentSolver
import it.matteobarbera.solvers.JacobiSolver

fun main() {
    val solver = JacobiSolver
    solver.performDDTest = false
    solver.performSPDTest = false
    /*val spdVals = arrayOf(
        doubleArrayOf(2.0, -1.0, 0.0),
        doubleArrayOf(-1.0, 2.0, -1.0),
        doubleArrayOf(0.0, -1.0, 2.0)
    )

    val rhsVals = arrayOf(
        doubleArrayOf(10.0, 20.0, 30.0)
    )
    val spd = MyMatrix.constructWithCopy(spdVals)
    val rhs = MyMatrix.constructWithCopy(rhsVals).transpose()
    val sol = solver.solve(spd, rhs, MyMatrix.zerosVec(3), 10e-14, 20000)*/
    val spd = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
    val rhs = MyMatrix.onesVec(spd.rowDimension)
    val sol = solver.solve(
        leftHandSide = spd,
        rightHandSide = rhs,
        initialGuess = MyMatrix.zerosVec(spd.rowDimension),
        tolerance = 10e-7,
        maximumIterations = 30000
    )
    Chart.simpleDiscretePlot(
        values = sol.errors,
        seriesName = "Errors",
        windowTitle = "KLinAlg",
        plotTitle = "Error over iterations (Jacobi method)",
        xAxisLabel = "Iteration number",
        yAxisLabel = "Error"
    )
}