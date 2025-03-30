package it.matteobarbera.io

import Jama.Matrix
import it.matteobarbera.model.MyMatrix
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

object MtxFileParser {

    @Throws(IOException::class)
    fun parse(path: String): MyMatrix {
        val matrix: MyMatrix
        try {
            BufferedReader(InputStreamReader(FileInputStream(path))).use { br ->
                val firstLine = br.readLine()
                val headers = firstLine.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val rows = headers[0].toInt()
                val cols = headers[1].toInt()
                matrix = MyMatrix(rows, cols)
                populate(matrix, br)
                return matrix
            }
        } catch (e: IOException) {
            throw IOException("Failed to parse from $path: invalid file.")
        }
    }

    @Throws(IOException::class)
    private fun populate(matrix: Matrix, br: BufferedReader) {
        var line = br.readLine()
        while (line != null) {
            val entry = line.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val i = entry[0].toInt() - 1
            val j = entry[1].toInt() - 1
            val v = entry[2].toDouble()
            matrix[i, j] = v
            line = br.readLine()
        }
    }
}