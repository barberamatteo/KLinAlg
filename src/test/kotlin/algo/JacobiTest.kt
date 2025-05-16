package algo

import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.io.MtxFileParser.parse
import it.matteobarbera.model.MyMatrix
import it.matteobarbera.solvers.AlgorithmResult
import it.matteobarbera.solvers.Jacobi
import it.matteobarbera.solvers.JacobiSolver
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertTrue

class JacobiTest {
    val solver = Jacobi

    @Test
    fun spa1_10en4(){
        val m = parse("src/test/resources/dati/spa1.mtx")
        val sol = solver.solve(m, 10e-4, 20000)
        println(sol)
    }

    @Test
    fun spa2_10en4(){
        val m = parse("src/test/resources/dati/spa2.mtx")
        val sol = solver.solve(m, 10e-4, 20000)
        println(sol)
    }

    @Test
    fun vem1_10en4(){
        val m = parse("src/test/resources/dati/vem1.mtx")
        val sol = solver.solve(m, 10e-4, 20000)
        println(sol)
    }

    @Test
    fun vem2_10en4(){
        val m = parse("src/test/resources/dati/vem2.mtx")
        val sol = solver.solve(m, 10e-4, 20000)
        println(sol)
    }

    @Test
    fun spa1_10en6(){
        val m = parse("src/test/resources/dati/spa1.mtx")
        val sol = solver.solve(m, 10e-6, 20000)
        println(sol)
    }

    @Test
    fun spa2_10en6(){
        val m = parse("src/test/resources/dati/spa2.mtx")
        val sol = solver.solve(m, 10e-6, 20000)
        println(sol)
    }

    @Test
    fun vem1_10en6(){
        val m = parse("src/test/resources/dati/vem1.mtx")
        val sol = solver.solve(m, 10e-6, 20000)
        println(sol)
    }

    @Test
    fun vem2_10en6(){
        val m = parse("src/test/resources/dati/vem2.mtx")
        val sol = solver.solve(m, 10e-6, 20000)
        println(sol)
    }
    @Test
    fun spa1_10en8(){
        val m = parse("src/test/resources/dati/spa1.mtx")
        val sol = solver.solve(m, 10e-8, 20000)
        println(sol)
    }

    @Test
    fun spa2_10en8(){
        val m = parse("src/test/resources/dati/spa2.mtx")
        val sol = solver.solve(m, 10e-8, 20000)
        println(sol)
    }

    @Test
    fun vem1_10en8(){
        val m = parse("src/test/resources/dati/vem1.mtx")
        val sol = solver.solve(m, 10e-8, 20000)
        println(sol)
    }

    @Test
    fun vem2_10en8(){
        val m = parse("src/test/resources/dati/vem2.mtx")
        val sol = solver.solve(m, 10e-8, 20000)
        println(sol)
    }

    @Test
    fun spa1_10en10(){
        val m = parse("src/test/resources/dati/spa1.mtx")
        val sol = solver.solve(m, 10e-10, 20000)
        println(sol)
    }

    @Test
    fun spa2_10en10(){
        val m = parse("src/test/resources/dati/spa2.mtx")
        val sol = solver.solve(m, 10e-10, 20000)
        println(sol)
    }

    @Test
    fun vem1_10en10(){
        val m = parse("src/test/resources/dati/vem1.mtx")
        val sol = solver.solve(m, 10e-10, 20000)
        println(sol)
    }

    @Test
    fun vem2_10en10(){
        val m = parse("src/test/resources/dati/vem2.mtx")
        val sol = solver.solve(m, 10e-10, 20000)
        println(sol)
    }

}