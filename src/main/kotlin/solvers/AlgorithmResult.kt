package it.matteobarbera.solvers

import it.matteobarbera.model.MyMatrix

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


}