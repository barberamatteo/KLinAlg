package it.matteobarbera.model

import it.matteobarbera.model.exceptions.DimensionsMismatchException
import it.matteobarbera.model.exceptions.NormNotSupportedException
import it.matteobarbera.model.exceptions.NotAMatrixException
import it.matteobarbera.model.exceptions.NotDiagonalException
import it.matteobarbera.model.exceptions.NotDottableException
import it.matteobarbera.model.exceptions.NotSquareException
import it.matteobarbera.model.exceptions.SingularMatrixException
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/**
 * A Kotlin version of the NIST JAMA's Matrix class, enhanced with operator overloading,
 * faster accesses, design patterns, and convenient methods & constructors.
 *
 * Some methods of the original Matrix class were removed, because this implementation
 * is designed to work fast with iterative methods such as Jacobi, Gauss-Seidl, Gradient and Conjugated Gradient.
 * JAMA library can be found at [this link](https://math.nist.gov/javanumerics/jama/)
 * @author Matteo Barbera (UniMiB)
 *
 * @constructor Constructs a (rows, cols) zero-filled matrix
 * @property rows Number of rows
 * @property cols Number of columns
 */
class Matrix(val rows: Int, val cols: Int){



    val isSquare = rows == cols
    val isColumnVector = rows != 1 && cols == 1
    val isRowVector = cols != 1 && rows == 1
    val vals = Array(size = rows) { DoubleArray(size = cols) }

    /**
     * Constructs a Matrix starting from the primitive 2D Double array
     * @param vals A 2D Double Array
     */
    constructor(vals: Array<DoubleArray>) :
            this(vals.size, vals[0].size){
                vals.copyInto(this.vals)
            }

    /**
     * Copy constructor
     * @param other Matrix to be copied
     */
    constructor(other: Matrix) :
            this(other.rows, other.cols){
                for (i in 0 until rows)
                    for (j in 0 until cols)
                        vals[i][j] = other.vals[i][j]
            }

    /**
     * Constructs a vector
     * @param vals A 1D Double Array
     * @param asColumnVector True if a column vector is wanted (e.g. a (n, 1) vector),
     *                       false otherwise(e.g. a (1, n) vector)
     */
    constructor(vals: DoubleArray, asColumnVector: Boolean):
            this(
                rows = if (asColumnVector) vals.size else 1,
                cols = if (asColumnVector) 1 else vals.size
            ){
                if (asColumnVector) {
                    for (i in 0 until rows){
                        this.vals[i][0] = vals[i]
                    }
                } else {
                    vals.copyInto(this.vals[0])
                }
            }

    /**
     * Construct a matrix starting from a legacy JAMA Matrix object
     * @param matrix A JAMA matrix
     */
    constructor(matrix: Jama.Matrix):
            this(
                rows = matrix.rowDimension,
                cols = matrix.columnDimension
            ){
                matrix.arrayCopy.copyInto(this.vals)
            }


    companion object {
        const val EQUALITY_TOLERANCE = 10e-10
        fun xFilledVector(dimension: Int, value: Double, asColumnVector: Boolean): Matrix {
            return Matrix(DoubleArray(dimension) { value }, asColumnVector)
        }

        fun toJamaMatrix(matrix: Matrix): Jama.Matrix {
            return Jama.Matrix(matrix.vals)
        }
    }


    /**
     * [equals] override which check matrices equality entry by entry:
     * if each of the (i,j)th entries of both matrices are far from each other [EQUALITY_TOLERANCE] then the
     * two matrices are equal.
     * @param other The comparing matrix
     */
    override operator fun equals(other: Any?): Boolean {
        if (other !is Matrix)
            return false
        if (cols != other.cols || rows != other.rows)
            return false
        for (i in 0 until rows)
            for (j in 0 until cols)
                if (abs(vals[i][j] - other[i, j]) > EQUALITY_TOLERANCE) {
                    return false
                }
        return true
    }

    /**
     * Plain old hashCode
     * @return The hashcode
     */
    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        result = 31 * result + isSquare.hashCode()
        result = 31 * result + isColumnVector.hashCode()
        result = 31 * result + isRowVector.hashCode()
        result = 31 * result + vals.contentDeepHashCode()
        return result
    }


    /**
     * @param i Row index
     * @param j Column index
     * @return The (i,j) entry of this matrix
     */
    operator fun get(i: Int, j: Int): Double {
        return vals[i][j]
    }


    /**
     * [get] override for vectors.
     * @param i Index of the desired entry
     * @return i-th entry of this vector
     * @throws RuntimeException if the calling object is not a vector
     */
    operator fun get(i: Int): Double {
        if (!isRowVector && !isColumnVector) throw RuntimeException("Not a vector")
        if (isRowVector)
            return vals[0][i]
        return vals[i][0]
    }

    /**
     * @return The [i]-th row of this matrix as a [Matrix] object row vector.
     * @throws RuntimeException if the calling structure is a row vector or a column vector.
     */
    fun getRow(i: Int): Matrix{
        try{
            if (isRowVector)
                throw RuntimeException(
                    "Calling structure is already a row vector." +
                        "Returning the entire vector (ignoring i).)"
                )
        } catch (e: RuntimeException){
            return this
        }

        try {
            if (isColumnVector)
                throw RuntimeException(
                    "Calling structure is a column vector." +
                            "To get the scalar at the desired index, use the access operator [<index>]." +
                            "Returning the original vector (ignoring i)."
                )
        } catch (e: RuntimeException){
            return this
        }

        return Matrix(vals = Array(size = 1) { vals[i] })
    }


    /**
     * @return a [Matrix] object row vector slicing the calling structure vector from [start] to [end]
     * @throws RuntimeException if the calling structure is not a row vector
     */
    fun subVector(start: Int, end: Int): Matrix {
        try{
            if (!isRowVector)
                throw RuntimeException("Not a row vector")
        } catch (e: RuntimeException){
            return this
        }
         return Matrix(
             vals = Array(1) {
                 vals[0].sliceArray(start..end)
             }
         )



    }


    /**
     * Sets the (i, j) entry for this matrix to [value].
     * @param i Row index
     * @param j Column index
     * @param value Double value to be set in the (i, j) entry
     */
    operator fun set(i: Int, j: Int, value: Double) {
        vals[i][j] = value
    }

    /**
     * If this structure is a row vector, sets the (0, [i]) entry to [value].
     * If this structure is a column vector, sets the ([i], 0) entry to [value].
     * If this structure is a matrix, an exception is thrown.
     * @param i The vector index to be updated.
     * @throws RuntimeException if the calling structure is not a vector
     */
    operator fun set(i: Int, value: Double) {
        if (isRowVector) {
            vals[0][i] = value
        } else {
            if (isColumnVector)
                vals[i][0] = value
            else
                throw RuntimeException("Not a vector")
        }
    }


    /**
     * Overrides the semantic of the unaryPlus, transposing the matrix
     * @return A transposed copy of this matrix
     */
    operator fun unaryPlus(): Matrix{
        return transpose()
    }

    /**
     * Multiplies each entry of this matrix by -1.
     * @return A copy of this matrix, in which every entry is changed in sign.
     */
    operator fun unaryMinus(): Matrix{
        val toRet = copy()
        for (i in 0 until rows){
            for (j in 0 until cols){
                toRet[i, j] = -toRet[i, j]
            }
        }
        return toRet
    }


    /**
     * @return A deep copy of this matrix
     */
    fun copy(): Matrix{
        return Matrix(this)
    }


    /**
     * Performs an in-place copy.
     * @return The destination matrix
     * @throws DimensionsMismatchException if the dimensions of the two matrices mismatch
     */
    fun copyInto(other: Matrix) {
        matrixDimensionsMatchForSum(other)
        for (i in 0 until rows){
            for (j in 0 until cols){
                other[i, j] = vals[i][j]
            }
        }
    }

    /**
     * @return A transposed copy of this matrix
     */
    fun transpose(): Matrix{
        val toRet = Matrix(cols, rows)
        val arr = toRet.vals
        for (i in 0 until rows){
            for (j in 0 until cols){
                arr[j][i] = vals[i][j]
            }
        }

        return toRet
    }


    /**
     * Computes the [normVal]-norm of this matrix. It's an infix function that just calls another function
     * defined for the specified norm.
     * @see norm1
     * @see norm2
     * @see normInf
     */
    infix fun norm(normVal: Double): Double{
        return when (normVal){
            1.0 -> norm1()
            2.0 -> norm2()
            POSITIVE_INFINITY -> normInf()
            else -> throw NormNotSupportedException(normVal)
        }
    }

    /**
     * Computes the 1-norm of this matrix, a.k.a. the maximum column sum
     * @return The 1-norm of this matrix
     */
    private fun norm1(): Double{
        var f = 0.0
        for (j in 0 until cols){
            var s = 0.0
            for (i in 0 until rows){
                s += abs(vals[i][j])
            }
            f = max(f,s)
        }
        return f
    }

    /**
     * Computes the 2-norm of this matrix, relying on legacy Jama matrix methods which perform an SVD under the hood.
     * @return The 2-norm of this matrix.
     */
    private fun norm2(): Double{
        if (isRowVector){
            var accumulator = 0.0
            for (i in 0 until cols)
                accumulator += vals[0][i]
            return sqrt(accumulator)
        }
        return toJamaMatrix(this).norm2()
    }



    /**
     * Computes the infinity-norm of this matrix, a.k.a. the maximum row sum
     * @return The infinity-norm of this matrix
     */
    private fun normInf(): Double{
        var f = 0.0
        for (i in 0 until rows){
            var s = 0.0
            for (j in 0 until cols){
                s += abs(vals[i][j])
            }
            f = max(f,s)
        }
        return f
    }



    /**
     * Performs a matrix-matrix sum.
     * @param other Second addend
     * @throws DimensionsMismatchException if the two matrix have mismatching dimensions.
     * @return A copy of calling matrix in which every entry is now summed with the corresponding [other]'s entry.
     */
    operator fun plus(other: Matrix): Matrix{
        try{
            matrixDimensionsMatchForSum(other)
        } catch (_: DimensionsMismatchException) {
            return this
        }
        val toRet = copy()
        for (i in 0 until rows){
            for (j in 0 until cols){
                toRet.vals[i][j] += other.vals[i][j]
            }
        }
        return toRet

    }

    /**
     * Performs a matrix-matrix sum in place.
     * @param other Second addend
     * @throws DimensionsMismatchException if the two matrix have mismatching dimensions.
     */
    operator fun plusAssign(other: Matrix){
        try{
            matrixDimensionsMatchForSum(other)
        } catch (_: DimensionsMismatchException) {
            return
        }
        for (i in 0 until rows){
            for (j in 0 until cols){
                vals[i][j] += other.vals[i][j]
            }
        }
    }

    /**
     * Performs a matrix-matrix subtraction
     * @param other Second minuend
     * @throws DimensionsMismatchException if the two matrix have mismatching dimensions.
     * @return A copy of calling matrix in which every entry is now subtracted with the corresponding [other]'s entry.
     */
    operator fun minus(other: Matrix): Matrix{
        try{
            matrixDimensionsMatchForSum(other)
        } catch (_: DimensionsMismatchException) {
            return this
        }
        val toRet = copy()
        for (i in 0 until rows){
            for (j in 0 until cols){
                toRet.vals[i][j] -= other.vals[i][j]
            }
        }
        return toRet
    }


    /**
     * Performs a matrix-matrix subtraction in place.
     * @param other Second minuend
     * @throws DimensionsMismatchException if the two matrix have mismatching dimensions.
     */
    @Throws(DimensionsMismatchException::class)
    operator fun minusAssign(other: Matrix){
        try{
            matrixDimensionsMatchForSum(other)
        } catch (_: DimensionsMismatchException) {
            return
        }
        for (i in 0 until rows){
            for (j in 0 until cols){
                vals[i][j] -= other.vals[i][j]
            }
        }
    }

    /**
     * Performs a dot product between this matrix (actually, a row vector) and a column
     * vector.
     * @param otherVector The second factor
     * @throws NotDottableException if this matrix (a vector) and [otherVector] aren't compatible to perform
     *                              a dot product.
     */
    @Throws(NotDottableException::class)
    infix fun dot(otherVector: Matrix): Double{
        try{
            if (!isDottable(otherVector)){
                throw NotDottableException(this, otherVector)
            }
        } catch (_: NotDottableException) {
            return 0.0
        }
        var toRet = 0.0
        for (i in 0 until rows){
            toRet += vals[0][i] * otherVector[i]
        }
        return toRet
    }
    /**
     * Performs an algebraic matrix-matrix product.
     * @param other Second factor
     * @throws DimensionsMismatchException if the two matrix have mismatching dimensions.
     */
    @Throws(
        NotAMatrixException::class,
        DimensionsMismatchException::class
    )
    operator fun times(other: Matrix): Matrix{
        if (matrixDimensionsMatchForDiagProduct(other))
            return diagMultiplyByVec(other)
        try{
            if (isDottable(other))
                throw NotAMatrixException(other)
        } catch(_: NotAMatrixException) {
            return this
        }
        try {
            matrixDimensionsMatchForProduct(other)
        } catch (_: DimensionsMismatchException) {
            return this
        }
        val toRet = Matrix(rows, other.cols)
        val toRetVals = toRet.vals
        val bColJ = DoubleArray(cols)
        for (j in 0 until other.cols){
            for (k in 0 until cols){
                bColJ[k] = other.vals[k][j]
            }

            for (i in 0 until rows){
                val aRowI = vals[i]
                var s = 0.0
                for (k in 0 until cols){
                    s += aRowI[k] * bColJ[k]
                }
                toRetVals[i][j] = s
            }
        }
        return toRet
    }



    /**
     * Performs a scalar-matrix product.
     * @param scalar Value to scale this matrix by.
     */
    operator fun times(scalar: Double): Matrix{
        val toRet = copy()
        for (i in 0 until rows){
            for (j in 0 until cols){
                toRet.vals[i][j] *= scalar
            }
        }
        return toRet
    }

    /**
     * Performs an in-place scalar-matrix product.
     * @param scalar Value to scale this matrix by.
     */
    operator fun timesAssign(scalar: Double){
        for (i in 0 until rows){
            for (j in 0 until cols){
                vals[i][j] *= scalar
            }
        }
    }


    /**
     * Overrides the semantic of not(), by inverting the matrix.
     * @return The inverse of this matrix
     */
    operator fun not(): Matrix{
        return if (isDiag()) invDiag() else inverse()
    }


    /**
     * Optimized matrix-vector multiplication in O(n) that works if matrix is diagonal.
     * That's called at runtime by the operator fun [times].
     * @param vec The vector to be multiplied to
     * @throws NotDiagonalException if the calling matrix is not diagonal.
     */
    fun diagMultiplyByVec(vec: Matrix): Matrix{
        if (!isDiag())
            throw NotDiagonalException()
        val toRet = Matrix(rows, 1)
        for (i in 0 until rows){
            toRet.vals[i][0] = vals[i][i] * vec[i]
        }
        return toRet
    }

    /**
     * Inverts this matrix, relying on legacy Jama matrix methods which perform a LU decomposition to solve
     * a linear system with identity matrix under the hood.
     * @return The inverse of this matrix
     * @throws NotSquareException if this matrix is not square
     */
    @Throws(NotSquareException::class)
    fun inverse(): Matrix{
        if (!isSquare)
            throw NotSquareException()
        return Matrix(toJamaMatrix(this).inverse())
    }


    /**
     * Invert a diagonal matrix in O(n²).
     * That's called at runtime by the operator fun [not] (inverse)
     * @return The inverse of this matrix
     * @throws NotDiagonalException if this matrix is not diagonal
     */
    fun invDiag(): Matrix{
        if (!isDiag())
            throw NotDiagonalException()
        val toRet = Matrix(rows, cols)

        for (i in 0 until rows) {
            if (vals[i][i] == 0.0)
                throw SingularMatrixException()
            toRet[i, i] = 1.0 / vals[i][i]
        }
        return toRet
    }

    /**
     * Computes the determinant of this matrix, relying on legacy Jama matrix methods which performs a LU
     * decomposition under the hood.
     * @return The determinant of this matrix.
     * @throws NotSquareException if this matrix is not square
     */
    fun det(): Double{
        if (!isSquare)
            throw NotSquareException()
        return toJamaMatrix(this).det()
    }

    /**
     * @return A diagonal matrix with the diagonal entries of this matrix
     * @throws NotSquareException if this matrix is not square
     */

    fun createDiag(): Matrix{
        if (!isSquare)
            throw NotSquareException()
        val toRet = Matrix(rows, cols)
        for (i in 0 until rows)
            toRet[i, i] = vals[i][i]
        return toRet
    }




    /**
     * Checks whether this matrix and [other] have matching dimensions. If not, an exception is thrown.
     * @throws DimensionsMismatchException
     * @param other The other matrix
     */
    @Throws(DimensionsMismatchException::class)
    private fun matrixDimensionsMatchForSum(other: Matrix, exceptionKeepsThisUnchanged: Boolean = true) {
        if (rows == other.rows && cols == other.cols)
            return
        else
            throw DimensionsMismatchException(
                this,
                other,
                exceptionKeepsThisUnchanged
            )
    }

    /**
     * Checks whether this matrix and [other] have the right dimensions to perform algebraic multiplication.
     * If not, an exception is thrown.
     * @throws DimensionsMismatchException
     * @param other The other matrix
     */
    @Throws(DimensionsMismatchException::class)
    private fun matrixDimensionsMatchForProduct(other: Matrix, exceptionKeepsThisUnchanged: Boolean = true) {
        if (cols == other.rows)
            return
        else
            throw DimensionsMismatchException(
                this,
                other,
                exceptionKeepsThisUnchanged
            )
    }

    /**
     * Checks whether this matrix and [vec] are compatible for the O(n) product between a diagonal and
     * a column vector.
     * @param vec A column vector [cols] = [rows] long
     * @return True if this matrix is diagonal and [vec] is a column vector, false otherwise.
     */
    private fun matrixDimensionsMatchForDiagProduct(vec: Matrix): Boolean {
        return isDiag() && vec.isColumnVector
    }


    /**
     * Checks whether the calling structure and [vec] are both suitable for a dot product.
     * @param vec A column vector as long as the calling structure.
     * @return True if a dot product is feasible, false otherwise
     */
    private fun isDottable(vec: Matrix): Boolean {
        return this.isRowVector && vec.isColumnVector && this.cols == vec.rows
    }


    /**
     * Checks whether this matrix is diagonal (e.g. all entries are zero, except for every
     * (i, i)).
     * @return True if this matrix is diagonal, false otherwise.
     */
    fun isDiag(): Boolean{
        if (!isSquare)
            return false
        for (i in 0 until rows){
            for (j in 0 until cols){
                if (vals[i][j] != 0.0 && i != j)
                    return false
            }
        }
        return true
    }

    /**
     * Checks whether this matrix is diagonal dominant (e.g. for every row, must hold that
     * the (i,i) entry is greater than the sum of the entire row (leaving out the (i,i) entry),
     * @return True if this matrix is diagonal dominant, false otherwise.
     */
    fun isDiagonalDominant(): Boolean{
        for (i in 0 until rows){
            var s = 0.0
            for (j in 0 until cols){
                s += vals[i][j]
            }
            if ((s - vals[i][i]) > vals[i][i])
                return false
        }
        return true
    }

    /**
     * @return the lower triangular part of this matrix.
     * @throws NotSquareException if this matrix is not square
     */
    @Throws(NotSquareException::class)
    fun tril(): Matrix{
        try{
            if (!isSquare)
                throw NotSquareException()
        } catch (e: NotSquareException) {
            return this
        }
        val toRet = copy()
        for (i in 0 until rows){
            for (j in i + 1 until cols){
                toRet[i, j] = 0.0
            }
        }
        return toRet
    }


    fun isTril(): Boolean{
        if (!isSquare)
            return false
        for (i in 0 until rows){
            for (j in i + 1 until cols){
                if (vals[i][j] != 0.0)
                    return false
            }
        }
        return true
    }
    /**
     * [toString] override that prints this matrix smartly.
     * @return A string representation of this matrix (or vector).
     */
    override fun toString(): String {
         if (isRowVector){
            return "\nRow vector(1, $cols): " + vals[0].toString(true)
        } else {
            if (isColumnVector){
                return "\nColumn vector($rows, 1): " + transpose().vals[0].toString(true)
            } else {
                val toRet = StringBuilder().append("\nMatrix ($rows, $cols):\n{")
                for (i in 0 until rows){
                    toRet.append("\n")
                    toRet.append(vals[i].toString(true) + ",")
                }
                return toRet.deleteCharAt(toRet.lastIndex).append("\n}").toString()
            }
        }


    }

    /**
     * Extension helper function which print [vals] in a tidy way.
     * @param custom Technical parameter (ignored by the function) which allows to not shadow the
     * original [DoubleArray.toString].
     * @return A comma separated representation of this DoubleArray
     */
    private fun DoubleArray.toString(custom: Boolean): String {
        val sb = StringBuilder()
        sb.append('[')
        for (i in indices){
            sb.append(this[i])
            sb.append(", ")
        }
        sb.deleteCharAt(sb.lastIndex).deleteCharAt(sb.lastIndex)
        sb.append(']')
        return sb.toString()
    }




}