package model

import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.model.Matrix
import org.junit.jupiter.api.Assertions.assertArrayEquals
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.measureTime

class MatrixTest {



    @Test
    fun checkValidityOfAllMethods(){
        val a = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        val b = Matrix(a.vals)

        assertEquals(a[0, 0], b[0, 0])
        assertArrayEquals(a.vals, b.vals)
        assertEquals(a[34, 34], b[34, 34])
        assertArrayEquals((+a).vals, b.transpose().vals)
        //assertArrayEquals((-a).vals, b.uminus().arrayCopy)
        assertArrayEquals(a.copy().vals, b.copy().vals)
        assertEquals(a norm 2.0, b norm 2.0)
        assertEquals(a norm 2.0, b norm 2.0)
        assertEquals(a norm POSITIVE_INFINITY, b norm POSITIVE_INFINITY)
        assertArrayEquals((a + a).vals, b.plus(b).vals)
        a+= a
        b += b

        assertArrayEquals(a.vals, b.vals)
        a -= a
        b -= b

        assertArrayEquals(a.vals, b.vals)


        assertArrayEquals((a * a).vals, b.times(b).vals)
        a *= 17.0
        b *= 17.0
        assertArrayEquals(a.vals, b.vals)

        assertEquals(a.det(), b.det())
        if (a.det() != 0.0)
            assertArrayEquals((!a).vals, b.inverse().vals)

    }

    @Test
    fun inverseBenchmark(){
        val a = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        val b = Matrix(a.vals)

        measureTime {
            !a
        }.also { println(it) }

        measureTime {
            b.inverse()
        }.also { println(it) }
    }

    @Test
    fun inverseOfInverse(){
        val a = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        assertEquals(a, !!!!!!!!!!!!!!!!!!!!a)
    }

    @Test
    fun isDiagonalBenchmark(){
        val a = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        val diag = a.createDiag()
        measureTime {
            diag.isDiag()
        }.also { println(it) }
    }


    @Test
    fun minusTest(){
        val val1 = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0)
        )

        val val2 = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 8.0)
        )

        val val3 = arrayOf(
            doubleArrayOf(0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )
        val m1 = Matrix(val1)
        val m2 = Matrix(val2)
        val m3 = Matrix(val3)


        assertEquals(m3, m1 - m2)


    }

    @Test
    fun copyTest(){
        val val1 = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0)
        )

        val m = Matrix(val1)
        val n = m.copy()

        m[0, 0] = 0.0

        assertNotEquals(m, n)
    }

    @Test
    fun diagonalDominant(){
        val val1 = arrayOf(
            doubleArrayOf(4.0, 1.0, 2.0),
            doubleArrayOf(2.0, 5.0, 1.0),
            doubleArrayOf(3.0, 4.0, 8.0)
        )

        val m = Matrix(val1)
        assertTrue(m.isDiagonalDominant())
    }

    @Test
    fun tril(){
        val val1 = arrayOf(
            doubleArrayOf(4.0, 1.0, 2.0, 8.0),
            doubleArrayOf(2.0, 5.0, 1.0, 1.0),
            doubleArrayOf(3.0, 4.0, 8.0, 8.0),
            doubleArrayOf(3.0, 4.0, 8.0, 12.0)
        )

        val val2 = arrayOf(
            doubleArrayOf(4.0, 0.0, 0.0, 0.0),
            doubleArrayOf(2.0, 5.0, 0.0, 0.0),
            doubleArrayOf(3.0, 4.0, 8.0, 0.0),
            doubleArrayOf(3.0, 4.0, 8.0, 12.0)
        )


        val m = Matrix(val1)
        val expected = Matrix(val2)

        assertEquals(m.tril(), expected)
    }

    @Test
    fun dot(){
        val vals = doubleArrayOf(1.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val m1 = Matrix(vals = Array(1){ vals })
        val m2 = Matrix(vals = Array(1){ vals }).transpose()
        val valExpected = 14.0

        assertEquals(m1 dot m2, valExpected)
    }

    /*@Test
    fun conditionNumber(){
        //val spa1 = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        val spa2 = MtxFileParser.parse("src/test/resources/dati/spa2.mtx")
        //val vem1 = MtxFileParser.parse("src/test/resources/dati/vem1.mtx")
        //val vem2 = MtxFileParser.parse("src/test/resources/dati/vem2.mtx")

        val a = Matrix.toJamaMatrix(spa2)
        a.eig()

        //println("Cond spa1 : ${spa1.conditionNumber()}")
        //println("Cond vem1 : ${vem1.conditionNumber()}")
        //println("Cond vem2 : ${vem2.conditionNumber()}")

    }*/
}