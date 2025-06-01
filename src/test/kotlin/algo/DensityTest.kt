package algo

import it.matteobarbera.io.MtxFileParser
import org.junit.jupiter.api.Test

class DensityTest {

    @Test
    fun spa1_density(){
        val spa1 = MtxFileParser.parse("src/test/resources/dati/spa1.mtx")
        var cont = 0
        for (row in 0 until spa1.rows){
            for (col in 0 until spa1.cols){
                if (spa1[row, col] != 0.0)
                    cont++
            }
        }
        println(cont)
        println((cont.toDouble()/(spa1.rows * spa1.cols)))

    }

}