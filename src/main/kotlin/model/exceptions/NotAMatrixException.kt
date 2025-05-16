package it.matteobarbera.model.exceptions

import it.matteobarbera.model.Matrix

class NotAMatrixException(other: Matrix):
    RuntimeException(
        "Parameter is not a matrix, is in fact a (${other.rows}, 1) column vector." +
                    "Since the calling matrix is a row vector, you must infix operator \"dot\" instead." +
                "Operation required is ignored."
    )