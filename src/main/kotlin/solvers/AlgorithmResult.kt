package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import java.io.FileWriter
import kotlin.time.Duration

data class AlgorithmResult(
    val solution: Matrix,
    val errors: MutableList<Double>,
    val iterations: Int,
    val convergenceReached: Boolean,
    val executionTime: Duration
){


     //Used only for TriXSolver, where analytics are not considered
    constructor(solution: Matrix) :
            this(
                solution,
                ArrayList(),
                0,
                false,
                Duration.ZERO
            )

    override fun toString(): String {
        return "solution=$solution,\n" +
                " errors=$errors,\n" +
                " iterations=$iterations,\n" +
                " convergenceReached=$convergenceReached,\n" +
                " executionTime=$executionTime"
    }


    /*fun writeAsMtxFile(path: String){
        val writer = FileWriter(path)
        val strBuilder = StringBuilder()
        strBuilder.append(solution.rowDimension).append(" ")
        strBuilder.append(solution.columnDimension).append(" ")
        val sparseDecomposition = solution.decomposeAsSparse()
        strBuilder.append(sparseDecomposition.size).append("\n")

        for (row in sparseDecomposition) {
            strBuilder.appendLine(
                row.key.first.plus(1).toString()
                        + " " +
                        row.key.second.plus(1).toString()
                        + " " +
                        row.value.toString()
            )
        }

        writer.write(strBuilder.toString())
        writer.close()
    }*/



}