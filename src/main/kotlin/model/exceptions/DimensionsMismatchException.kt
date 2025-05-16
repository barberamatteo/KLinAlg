package it.matteobarbera.model.exceptions

import it.matteobarbera.model.Matrix

class DimensionsMismatchException(msg: String): RuntimeException(msg) {
    constructor(m1: Matrix, m2: Matrix, exceptionKeepsM1Unchanged: Boolean) :
            this(
                "Dimensions mismatch. Calling matrix is (${m1.rows}, ${m1.cols})," +
                    "while parameter matrix is (${m2.rows}, ${m2.cols})." +
                    if (exceptionKeepsM1Unchanged)
                        "\nRequired operation is ignored."
                    else
                        "\nReturning a zero(s) matrix of dimensions (${m1.rows}, ${m1.cols})."
            )
}