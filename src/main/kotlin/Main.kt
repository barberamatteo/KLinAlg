package it.matteobarbera

import it.matteobarbera.charts.Chart
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.JacobiSolver

fun main() {
    val solver = JacobiSolver
    val spdVals = arrayOf(
        doubleArrayOf(2.0, -1.0, 0.0),
        doubleArrayOf(-1.0, 2.0, -1.0),
        doubleArrayOf(0.0, -1.0, 2.0)
    )

    val rhsVals = arrayOf(
        doubleArrayOf(10.0, 20.0, 30.0)
    )
    val spd = MyMatrix.constructWithCopy(spdVals)
    val rhs = MyMatrix.constructWithCopy(rhsVals).transpose()
    val sol = solver.solve(spd, rhs, MyMatrix.zerosVec(3), 10e-14, 20000)
    Chart.plot(sol.errors)
}