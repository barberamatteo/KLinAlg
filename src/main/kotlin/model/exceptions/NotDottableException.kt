package it.matteobarbera.model.exceptions

import it.matteobarbera.model.Matrix

class NotDottableException(m1: Matrix, m2: Matrix):
    RuntimeException(
        "Can't perform dot product between a (${m1.rows}, ${m1.cols}) and" +
            "a (${m2.rows}, ${m2.cols}). Returning scalar 0."
    )