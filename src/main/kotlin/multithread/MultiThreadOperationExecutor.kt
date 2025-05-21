package it.matteobarbera.multithread

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*

object MultiThreadOperationExecutor /*{

    fun mul(a: MyMatrix, b: MyMatrix, blockSize: Int = 128): MyMatrix {
        if (a.columnDimension != b.rowDimension)
            throw IllegalArgumentException("Matrices dimension mismatch")

        val jobs = Array<Job?>(a.rowDimension) { null }
        val arr = Array(a.rowDimension) { MyMatrix.onesVec(0) }

        var sol = MyMatrix(b.rowDimension, b.columnDimension)
        runBlocking {
            /*for (i in 0..<a.rowDimension) {
                jobs[i] = launch {
                    arr[i] = MyMatrix(a.array[i]) * b
                }
            }
           */
            launch { sol = a * b}.join()
        }
        //return MyMatrix(arr)
        return sol
    }

    suspend fun parallelMatrixMultiply(a: MyMatrix, b: MyMatrix) = coroutineScope {
        val chunkSize = 100
        val jobs = mutableListOf<Job>()
        val matrixA = a.array
        val matrixB = b.array
        val result = Array (matrixB.size){ DoubleArray(matrixA.size) { 0.0 } }
        for (i in matrixA.indices step chunkSize) {
            jobs.add(launch(Dispatchers.Default) {
                for (j in i until (i + chunkSize).coerceAtMost(matrixA.size)) {
                    for (k in matrixB[0].indices) {
                        result[j][k] = 0.0
                        for (l in matrixA[0].indices) {
                            result[j][k] += matrixA[j][l] * matrixB[l][k]
                        }
                    }
                }
            })
        }
        jobs.forEach { it.join() }
        return@coroutineScope MyMatrix.constructWithCopy(result)
    }
}*/