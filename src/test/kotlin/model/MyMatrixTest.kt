package model

import Jama.LUDecomposition
import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.model.MyMatrix
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class MyMatrixTest {
/*
    @Test
    fun instantiationFromParser(){
        measureTime {
            val m = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        }.also { println("Execution time (parsing and instantiating spa1.mtx): $it") }

        measureTime {
            val m = MtxFileParser.parse("src/test/resources/dati/spa2.mtx")
        }.also { println("Execution time (parsing and instantiating spa2.mtx): $it") }

        measureTime {
            val m = MtxFileParser.parse("src/test/resources/dati/vem1.mtx")
        }.also { println("Execution time (parsing and instantiating vem1.mtx): $it") }

        measureTime {
            val m = MtxFileParser.parse("src/test/resources/dati/vem2.mtx")
        }.also { println("Execution time (parsing and instantiating vem2.mtx): $it") }

    }

    @Test
    fun copy(){
        val m = MtxFileParser.parse("src/test/resources/dati/spa2.mtx")
        val arrcpy = m.arrayCopy
        measureTime {
            val m2 = MyMatrix(arrcpy)
        }.also { println("Execution time (copying spa2): $it") }
    }

    @Test
    fun trilVsGetl(){
        val m = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        val decomposer = LUDecomposition(m)
        measureTime {
            val getl = MyMatrix(decomposer.l)
        }.also { println("Execution time (getL()): $it") }
        measureTime {
            val tril = m.tril()
        }.also { println("Execution time (tril()): $it") }

    }



*/
}