import it.matteobarbera.model.MyMatrix
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GenericTests {

    @Test
    fun basicOps(){
        val vals = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0)
        )
        val m = MyMatrix.constructWithCopy(vals)
        val vals1 = arrayOf(
            doubleArrayOf(5.0, 1.0, 3.0),
            doubleArrayOf(2.0, 5.0, 1.0),
            doubleArrayOf(7.0, 1.0, 9.0)
        )
        val m1 = MyMatrix.constructWithCopy(vals1)

        val valp = arrayOf(
            doubleArrayOf(6.0, 3.0, 6.0),
            doubleArrayOf(6.0, 10.0, 7.0),
            doubleArrayOf(14.0, 9.0, 18.0),
        )

        val mp = MyMatrix.constructWithCopy(valp)


        assertEquals(mp, m1 + m)
    }


    @Test
    fun diagonalDominantTest() {
        val vals = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0)
        )
        val m = MyMatrix(MyMatrix.constructWithCopy(vals))
        assertFalse(m.isDiagonalDominant())


        val vals1 = arrayOf(
            doubleArrayOf(6.0, 1.0, 2.0),
            doubleArrayOf(3.0, 4.0, 1.0),
            doubleArrayOf(2.0, 1.0, 3.0)
        )
        val m1 = MyMatrix(MyMatrix.constructWithCopy(vals1))
        assertTrue(m1.isDiagonalDominant())
    }

    @Test
    fun diagonalInv() {
        val notDiagonalVals = arrayOf(
            doubleArrayOf(1.0, 2.0, 0.0),
            doubleArrayOf(0.0, 5.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )
        val notDiagonal = MyMatrix(MyMatrix.constructWithCopy(notDiagonalVals))
        assertFalse(notDiagonal.isDiagonal())

        val diagonalVals = arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(0.0, 9.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.5)
        )
        val diagonal = MyMatrix(MyMatrix.constructWithCopy(diagonalVals))
        assertTrue(diagonal.isDiagonal())
    }

    @Test
    fun diagonalInverseTest() {
        val diagonalVals = arrayOf(
            doubleArrayOf(0.5, 0.0, 0.0),
            doubleArrayOf(0.0, 9.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0 / 8)
        )

        val invDiagonalVals = arrayOf(
            doubleArrayOf(2.0, 0.0, 0.0),
            doubleArrayOf(0.0, 1.0 / 9, 0.0),
            doubleArrayOf(0.0, 0.0, 8.0)
        )

        val diagonal = MyMatrix(MyMatrix.constructWithCopy(diagonalVals))

        val diagonalInverse = diagonal.invDiag()
        val expected = MyMatrix(MyMatrix.constructWithCopy(invDiagonalVals))

        assertEquals(diagonalInverse, expected)
    }
}