package it.matteobarbera.solvers

import it.matteobarbera.model.Matrix
import kotlin.time.Duration

data class AlgorithmResult(
    val solution: Matrix,
    val errors: MutableList<Double>,
    val iterations: Int,
    val convergenceReached: Boolean,
    val executionTime: Duration,
    val cutFirstError: Boolean = true

){
    var includeSolutionInToString: Boolean = false
    var includeErrorsInToString: Boolean = false
    init{
        if (errors.isNotEmpty() && cutFirstError)
            errors.removeFirst()
    }


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
        val toRet: StringBuilder = StringBuilder()
        if (includeSolutionInToString)
            toRet.append(" solution=$solution,\n")
        if (includeErrorsInToString)
            toRet.append(" errors=$errors,\n")
        toRet.append(
                " iterations=$iterations,\n" +
                " convergenceReached=$convergenceReached,\n" +
                " executionTime=$executionTime"
        )
        return toRet.toString()
    }


    fun relativeError(exactSolution: Matrix): Double {
        return ((solution - exactSolution) norm 2.0) / (exactSolution norm 2.0)
    }



}