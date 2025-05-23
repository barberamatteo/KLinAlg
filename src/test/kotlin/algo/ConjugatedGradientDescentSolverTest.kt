package algo

import it.matteobarbera.io.MtxFileParser.parse
import it.matteobarbera.solvers.ConjugatedGradient
import kotlin.test.Test

class ConjugatedGradientDescentSolverTest {
    val solver = ConjugatedGradient

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