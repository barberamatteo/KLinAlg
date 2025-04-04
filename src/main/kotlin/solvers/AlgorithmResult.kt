package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix
import java.io.FileWriter

data class AlgorithmResult(
    val solution: MyMatrix,
    val errors: MutableList<Double>,
    val iterations: Int,
    val convergenceReached: Boolean,
    val executionTime: Long
){


     //Used only for TriXSolver, where analytics are not considered
    constructor(solution: MyMatrix) :
            this(
                solution,
                ArrayList(),
                0,
                false,
                0L
            )



    fun writeAsMtxFile(path: String){
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
    }


}