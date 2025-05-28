package it.matteobarbera.charts

import javax.swing.WindowConstants

abstract class ChartOptions() {
    abstract var seriesName: String
    abstract var windowTitle: String
    abstract var plotTitle: String
    abstract var xAxisLabel: String
    abstract var yAxisLabel: String
    abstract var closeOperation: Int
    abstract var stroke: Float
    abstract var showLegend: Boolean


    companion object OnCloseActions{
        const val EXIT_PROGRAM = WindowConstants.EXIT_ON_CLOSE
        const val HIDE = WindowConstants.HIDE_ON_CLOSE
        const val DISPOSE = WindowConstants.DISPOSE_ON_CLOSE
        const val DO_NOTHING = WindowConstants.DO_NOTHING_ON_CLOSE
    }

    abstract fun copy(): ChartOptions
}