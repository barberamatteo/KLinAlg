package it.matteobarbera.model

import Jama.Matrix
import java.util.*

class MyMatrix(i: Int, j: Int): Matrix(i, j) {
    private var symmetric: Boolean? = null
    private var positiveDefinite: Boolean? = null
    private val isVector: Boolean = i == 1 || j == 1
    private val isSquare: Boolean = i == j

    constructor(matrix: Matrix) :
        this(matrix.rowDimension, matrix.columnDimension){
            setMatrix(
                0, matrix.rowDimension - 1,
                0, matrix.columnDimension - 1,
                matrix
            )
    }

    companion object{
        fun constructWithCopy(doubles: Array<DoubleArray>): MyMatrix {
            return MyMatrix(Matrix.constructWithCopy(doubles))
        }

        fun zerosVec(i: Int): MyMatrix {
            return MyMatrix(i, 1)
        }

        fun onesVec(i: Int): MyMatrix {
            val toRet = zerosVec(i)
            for (j in 0..<i) {
                toRet[j, 0] = 1.0
            }
            return toRet
        }
    }


    override fun copy(): MyMatrix {
        return MyMatrix(super.copy())
    }

    override operator fun plus(matrix: Matrix?): MyMatrix {
        return MyMatrix(super.plus(matrix))
    }

    override operator fun minus(matrix: Matrix?): MyMatrix {
        return MyMatrix(super.minus(matrix))
    }

    override operator fun times(matrix: Matrix?): MyMatrix {
        return MyMatrix(super.times(matrix))
    }

    override operator fun times(scalar: Double): MyMatrix{
        return MyMatrix(super.times(scalar))
    }

    override fun getMatrix(firstRow: Int, lastRow: Int, firstColumn: Int, lastColumn: Int): MyMatrix {
        return MyMatrix(super.getMatrix(firstRow, lastRow, firstColumn, lastColumn))
    }

    override fun transpose(): MyMatrix {
        return MyMatrix(super.transpose())
    }



    override fun equals(other: Any?): Boolean {
        if (other !is Matrix) {
            return false
        }
        if ((this.columnDimension != other.columnDimension)
            ||
            (this.rowDimension != other.rowDimension)
        ) {
            return false
        }
        for (i in 0..<this.rowDimension) {
            for (j in 0..<other.rowDimension) {
                if (this[i, j] != other[i, j]) return false
            }
        }
        return true
    }

    private fun isSymmetric(): Boolean {
        if (!isSquare) return false
        if (symmetric == null) {
            symmetric = equals(transpose())
        }
        return symmetric!!
    }

    private fun isPositiveDefinite(): Boolean {
        if (!isSquare) return false
        if (positiveDefinite == null) {
            positiveDefinite = Arrays.stream(eig().realEigenvalues).allMatch { v: Double -> v > 0 }
        }
        return positiveDefinite!!
    }

    fun isSPD(): Boolean {
        if (!isSquare) return false
        if (symmetric == null) {
            isSymmetric()
        }
        if (positiveDefinite == null) {
            isPositiveDefinite()
        }
        return symmetric!! && positiveDefinite!!
    }

    fun triu(): MyMatrix {
        val copy = copy()
        var stopIndex = 1
        for (i in 1..<this.rowDimension) {
            for (j in 0..<stopIndex) {
                copy[i, j] = 0.0
            }
            stopIndex++
        }
        return copy
    }

    fun tril(): MyMatrix {
        return minus(triu()).plus(diag())
    }

    fun isTril(): Boolean {
        return this == tril()
    }

    fun isTriu(): Boolean {
        return this == triu()
    }

    fun diag(): MyMatrix {
        val toRet: MyMatrix = MyMatrix(this.rowDimension, this.columnDimension)
        for (i in 0..<this.rowDimension) {
            toRet[i, i] = this[i, i]
        }
        return toRet
    }



    operator fun plus(scalar: Double): MyMatrix {
        val toRet = copy()
        for (i in 0..<this.rowDimension) {
            for (j in 0..<this.columnDimension) {
                toRet[i, j] = this[i, j] + scalar
            }
        }
        return toRet
    }


    fun isDiagonalDominant(): Boolean {
        if (!isSquare) return false
        for (i in 0..<this.rowDimension) {
            var rowSum = Arrays.stream(
                getMatrix(
                    i,
                    i,
                    0,
                    columnDimension - 1
                ).rowPackedCopy
            ).sum()
            rowSum -= get(i, i)
            if (rowSum > get(i, i)) {
                return false
            }
        }
        return true
    }

    fun invDiag(): MyMatrix {
        if (!isDiagonal()) throw RuntimeException("Not a diagonal")
        val toRet = copy()
        for (i in 0..<this.rowDimension) {
            if (get(i, i) == 0.0) throw RuntimeException("At least one entry is zero; aka det = 0; aka not invertible!")
            toRet[i, i] = 1 / get(i, i)
        }
        return toRet
    }

    fun isDiagonal(): Boolean {
        return (this.minus(diag())
                == MyMatrix(
            this.rowDimension,
            this.columnDimension
        ))
    }


    operator fun get(i: Int): Double {
        if (!isVector) throw RuntimeException("Not a vector")

        if (this.rowDimension == 1 && this.columnDimension == 1) return get(0, 0)
        if (this.rowDimension == 1) return get(0, i)
        return get(i, 0)
    }

    fun set(i: Int, value: Double) {
        if (!isVector) throw RuntimeException("Not a vector")
        if (this.rowDimension == 1 && this.columnDimension == 1) {
            set(0, 0, value)
            return
        }
        if (this.rowDimension == 1) {
            set(0, i, value)
            return
        }
        set(i, 0, value)
    }

    fun dotVV(other: MyMatrix): Double {
        if (!isVector) throw RuntimeException("Calling structure is not a vector")
        if (!other.isVector) throw RuntimeException("Parameter structure is not a vector")

        var ret = 0.0
        for (i in 0..<this.columnDimension) {
            ret += this.get(i) * other.get(i)
        }
        return ret
    }

    fun getRow(i: Int): MyMatrix {
        return getMatrix(i, i, 0, columnDimension - 1)
    }


    fun subVec(lastIndex: Int): MyMatrix {
        return subVec(0, lastIndex)
    }

    fun subVec(firstIndex: Int, lastIndex: Int): MyMatrix {
        return getMatrix(0, 0, firstIndex, lastIndex)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}