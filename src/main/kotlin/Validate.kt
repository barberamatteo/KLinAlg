import charts.MultiChartPanel
import it.matteobarbera.charts.BarsChartBuilder
import it.matteobarbera.charts.BarsChartOptions
import it.matteobarbera.io.MtxFileParser
import it.matteobarbera.model.Matrix
import it.matteobarbera.solvers.AlgorithmResult
import it.matteobarbera.solvers.ConjugatedGradient
import it.matteobarbera.solvers.GaussSeidel
import it.matteobarbera.solvers.Gradient
import it.matteobarbera.solvers.Jacobi
import org.jfree.chart.ChartPanel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Point
import java.awt.Toolkit
import kotlin.system.exitProcess
import kotlin.time.DurationUnit
import kotlin.time.measureTime

val log: Logger = LoggerFactory.getLogger("Main")
/**
 * Validates the iterative methods of this library on symmetric & positive defined
 * matrices in .mtx format.
 * This script sequentially runs for each matrix, for each solver, for each tolerance
 * (64 runs).
 *
 * @author Matteo Barbera (UniMiB)
 */
fun main(args: Array<String>) {

    if (args.isEmpty() || !allFilesMatchMtx(args)){
        log.error("Invalid input file(s). Returning with exit code 1")
        exitProcess(1)
    }

    // Reads the matrices from the respective .mtx files and puts them into a map
    log.info("Reading matrices from the respective .mtx files ...")

    val simpleFileNames = args.map { simpleFileName(it) }
    val parsedMatrices = args.map { MtxFileParser.parse(it) }
    val matricesMap = simpleFileNames.zip(parsedMatrices).toMap()

    for (matrix in matricesMap){
        log.info("Correctly parsed ${matrix.key}.mtx as a (${matrix.value.rows}, ${matrix.value.cols}) Matrix!")
    }
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
    val exactSolutions = simpleFileNames.map {
        Matrix.xFilledVector(matricesMap[it]!!.cols, 1.0, true)
    }
    val exactSolutionsMap = simpleFileNames.zip(exactSolutions).toMap()

    //---------------------------------------------------------------------------



    // Creates the right hand sides, calculating A*x, and puts them into a map
    log.info("Creating the right hand sides ...")
    val rhss = simpleFileNames.map { matricesMap[it]!! * exactSolutionsMap[it]!! }
    val rhsMap = simpleFileNames.zip(rhss).toMap()

    //---------------------------------------------------------------------------



    // Required tolerances
    val tolerances = doubleArrayOf(1e-4, 1e-6, 1e-8, 1e-10)
    //---------------------------------------------------------------------------



    // Setting the maximum iterations to 20.000 for all methods
    val maximumIterations = 20000
    //---------------------------------------------------------------------------



    // Instantiates the tables to be populated with the solutions, puts them in a map

    val solutionsMap = mapOf(
        Jacobi to createTable<AlgorithmResult>(simpleFileNames),
        GaussSeidel to createTable<AlgorithmResult>(simpleFileNames),
        Gradient to createTable<AlgorithmResult>(simpleFileNames),
        ConjugatedGradient to createTable<AlgorithmResult>(simpleFileNames),
    )
    //---------------------------------------------------------------------------



    // Instantiates the tables with the relative errors for each solver, puts them in a map
    val relativeErrorsMap = mapOf(
        Jacobi to createTable<Double>(simpleFileNames),
        GaussSeidel to createTable<Double>(simpleFileNames),
        Gradient to createTable<Double>(simpleFileNames),
        ConjugatedGradient to createTable<Double>(simpleFileNames)
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

    //---------------------------------------------------------------------------

    // Plot generation

    /* Creates a table, in which each entry maps a matrix to a set of three charts
     * showing iterations, execution time, relative errors.
     */

    val chartTable = createTable<ChartPanel>(simpleFileNames).toMutableMap()

    // Creates all the charts from the data obtained by the methods, then adds them to the list

    for (simpleFileName in simpleFileNames) {
        val iterationsBarsChart = BarsChartBuilder()
            .plotTitle("Iterations required to reach convergence (for each tolerance) - $simpleFileName")
            .xAxisLabel("Method")
            .yAxisLabel("Iterations")
            .setLogAxis(true)
        val executionTimesBarChart = BarsChartBuilder()
            .plotTitle("Execution time in milliseconds (for each tolerance) - $simpleFileName")
            .xAxisLabel("Method")
            .yAxisLabel("Time (ms)")
            .setLogAxis(true)
        val relativeErrorsBarChart = BarsChartBuilder()
            .plotTitle("Relative errors in [0, 1] (for each tolerance) - $simpleFileName")
            .xAxisLabel("Method")
            .yAxisLabel("Relative Error")
            .setLogAxis(true)

        solvers.map {
            iterationsBarsChart.addCategory(it.javaClass.simpleName)
            executionTimesBarChart.addCategory(it.javaClass.simpleName)
            relativeErrorsBarChart.addCategory(it.javaClass.simpleName)
        }
        tolerances.map {
            iterationsBarsChart.addEntity(it.toString())
            executionTimesBarChart.addEntity(it.toString())
            relativeErrorsBarChart.addEntity(it.toString())
        }

        for ((solver, _) in solutionsMap) {
            for (tolerance in tolerances.indices) {
                iterationsBarsChart.addData(
                    tolerances[tolerance].toString(),
                    solver.javaClass.simpleName,
                    solutionsMap[solver]!![simpleFileName]!![tolerance].iterations.toDouble()
                )
                executionTimesBarChart.addData(
                    tolerances[tolerance].toString(),
                    solver.javaClass.simpleName,
                    solutionsMap[solver]!![simpleFileName]!![tolerance].executionTime.toDouble(DurationUnit.MILLISECONDS)
                )
                relativeErrorsBarChart.addData(
                    tolerances[tolerance].toString(),
                    solver.javaClass.simpleName,
                    relativeErrorsMap[solver]!![simpleFileName]!![tolerance]
                )
            }
        }
        chartTable[simpleFileName]!!.add(
            iterationsBarsChart
                .build()
                .getPanel()
        )
        chartTable[simpleFileName]!!.add(executionTimesBarChart.build().getPanel())
        chartTable[simpleFileName]!!.add(relativeErrorsBarChart.build().getPanel())
    }

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val framePointsArray = arrayOf(
        Point(0, 0),
        Point(screenSize.width / 2, 0),
        Point(0, screenSize.height / 2),
        Point(screenSize.width / 2, screenSize.height / 2)
    )
    var generatedGraphs = 0
    for ((simpleFileName, listOfCharts) in chartTable){
        MultiChartPanel().show3(
            listOfCharts,
            simpleFileName,
            framePointsArray[generatedGraphs].x,
            framePointsArray[generatedGraphs].y
        )
        generatedGraphs ++
    }



    log.info("Returning with exit code 0")



}

/**
 * Utility function that provides a typed-table
 * @param T The type of the entry of the table
 * @return A [T] type table (e.g. a [Map] with a string key and a [MutableList] as a value)
 */
fun <T> createTable(simpleFileNames: List<String>): Map<String, MutableList<T>>{

    return simpleFileNames.zip(
        simpleFileNames.map { mutableListOf<T>() }
    ).toMap()
}

/**
 * Checks if all input string arguments end with .mtx
 * @param args The args given as the input of MainKt
 * @return True if each arg in args end with .mtx, false otherwise
 */
fun allFilesMatchMtx(args: Array<String>): Boolean{
    for (arg in args) {
        if (!arg.endsWith(".mtx"))
            return false
    }
    return true
}

/**
 * Takes the fileName (ending in .mtx) and removes the extension from it
 * @param fileName complete file name (with .mtx extension)
 * @return The prefix (before .mtx) of [fileName]
 */
fun simpleFileName(fileName: String): String{
    return fileName.substringBefore(".mtx")
}
