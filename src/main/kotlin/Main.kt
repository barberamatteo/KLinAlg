import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.model.Matrix
import it.matteobarbera.solvers.AlgorithmResult
import it.matteobarbera.solvers.ConjugatedGradient
import it.matteobarbera.solvers.GaussSeidel
import it.matteobarbera.solvers.Gradient
import it.matteobarbera.solvers.Jacobi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.measureTime

val log: Logger = LoggerFactory.getLogger("Main")
const val spa1 = "spa1"
const val spa2 = "spa2"
const val vem1 = "vem1"
const val vem2 = "vem2"
/**
 * Validates the iterative methods of this library on four symmetric & positive defined
 * matrices in .mtx format (spa1.mtx, spa2.mtx, vem1.mtx, vem2.mtx).
 * This script sequentially runs for each matrix, for each solver, for each tolerance
 * (64 runs).
 *
 */
fun main(){

    // Reads the matrices from the respective .mtx files and puts them into a map
    log.info("Reading matrices from the respective .mtx files ...")

    val matricesMap = mapOf(
            spa1 to MtxFileParser.parse("src/test/resources/dati/spa1.mtx"),
            spa2 to MtxFileParser.parse("src/test/resources/dati/spa2.mtx"),
            vem1 to MtxFileParser.parse("src/test/resources/dati/vem1.mtx"),
            vem2 to MtxFileParser.parse("src/test/resources/dati/vem2.mtx")
    )
    //---------------------------------------------------------------------------



    // Instantiates the solvers and puts them in an Array
    log.info("Instantiating solvers ...")
    val solvers = arrayOf(
        Jacobi,
        GaussSeidel,
        Gradient,
        ConjugatedGradient
    )
    //---------------------------------------------------------------------------



    // Creates the exact solutions, e.g. 1-filled vectors, and puts them into a map
    log.info("Creating the exact solutions (1-filled column vectors) ...")
    val exactSolutionsMap = mapOf(
            spa1 to Matrix.xFilledVector(matricesMap[spa1]!!.cols, 1.0, true),
            spa2 to Matrix.xFilledVector(matricesMap[spa2]!!.cols, 1.0, true),
            vem1 to Matrix.xFilledVector(matricesMap[vem1]!!.cols, 1.0, true),
            vem2 to Matrix.xFilledVector(matricesMap[vem2]!!.cols, 1.0, true)
        )
    //---------------------------------------------------------------------------



    // Creates the right hand sides, calculating A*x, and puts them into a map
    log.info("Creating the right hand sides ...")
    val rhsMap = mapOf(
            spa1 to matricesMap[spa1]!! * exactSolutionsMap[spa1]!!,
            spa2 to matricesMap[spa2]!! * exactSolutionsMap[spa2]!!,
            vem1 to matricesMap[vem1]!! * exactSolutionsMap[vem1]!!,
            vem2 to matricesMap[vem2]!! * exactSolutionsMap[vem2]!!
    )
    //---------------------------------------------------------------------------



    // Required tolerances
    val tolerances = doubleArrayOf(1e-4, 1e-6, 1e-8, 1e-10)
    //---------------------------------------------------------------------------



    // Setting the maximum iterations to 20.000 for all methods
    val maximumIterations = 20000
    //---------------------------------------------------------------------------



    // Instantiates the tables to be populated with the solutions, puts them in a map

    val solutionsMap = mapOf(
        Jacobi to createTable<AlgorithmResult>(),
        GaussSeidel to createTable<AlgorithmResult>(),
        Gradient to createTable<AlgorithmResult>(),
        ConjugatedGradient to createTable<AlgorithmResult>(),
    )
    //---------------------------------------------------------------------------



    // Instantiates the tables with the relative errors for each solver, puts them in a map
    val relativeErrorsMap = mapOf(
        Jacobi to createTable<Double>(),
        GaussSeidel to createTable<Double>(),
        Gradient to createTable<Double>(),
        ConjugatedGradient to createTable<Double>()
    )
    //---------------------------------------------------------------------------


    //Main loop
    measureTime {
        for (matrix in matricesMap) {
            log.info("Running all methods on matrix ${matrix.key} ...")
            for (solver in solvers) {
                log.info("${solver.javaClass.simpleName} started ...")
                for (tolerance in tolerances) {
                    log.info("Tolerance = $tolerance")
                    val tempSol = solver.solve(
                        coefficientMatrix = matrix.value,
                        tolerance = tolerance,
                        maximumIterations = maximumIterations,
                        rightHandSide = rhsMap[matrix.key]!!
                    )
                    val tempRelativeError = tempSol.relativeError(exactSolutionsMap[matrix.key]!!)

                    solutionsMap[solver]!![matrix.key]!!.add(tempSol)
                    relativeErrorsMap[solver]!![matrix.key]!!.add(tempRelativeError)
                    println(tempSol)
                    println("Relative error: $tempRelativeError")
                    println("\n\n")
                }
                log.info("... ${solver.javaClass.simpleName} method finished!")
            }
            log.info("... run on ${matrix.key} finished!")
        }
    }.also {
        println("\n")
        log.info("Complexive execution time: $it ")
    }
    log.info("Returning with exit code 0")
    //---------------------------------------------------------------------------



}

/**
 * Utility function that provides a typed-table
 * @param T The type of the entry of the table
 * @return A [T] type table (e.g. a [Map] with a string key and a [MutableList] as a value)
 */
fun <T> createTable(): Map<String, MutableList<T>>{
    return mapOf(
        spa1 to mutableListOf(),
        spa2 to mutableListOf(),
        vem1 to mutableListOf(),
        vem2 to mutableListOf()
    )
}

