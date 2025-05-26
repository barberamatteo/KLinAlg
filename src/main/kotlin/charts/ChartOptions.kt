package it.matteobarbera.charts

import javax.swing.WindowConstants
import javax.swing.WindowConstants.HIDE_ON_CLOSE

class ChartOptions {
    var seriesName: String = "Series"
    var windowTitle: String = "Chart"
    var plotTitle: String = "Simple plot"
    var xAxisLabel: String = "x-Axis"
    var yAxisLabel: String = "y-Axis"
    var closeOperation: Int = HIDE_ON_CLOSE
    var stroke: Float = 3.0f
    var showLegend: Boolean = true

    companion object OnCloseActions{
        const val EXIT_PROGRAM = WindowConstants.EXIT_ON_CLOSE
        const val HIDE = WindowConstants.HIDE_ON_CLOSE
        const val DISPOSE = WindowConstants.DISPOSE_ON_CLOSE
        const val DO_NOTHING = WindowConstants.DO_NOTHING_ON_CLOSE
    }

    fun copy(): ChartOptions {
        return ChartOptions().also {
            it.seriesName = seriesName
            it.windowTitle = windowTitle
            it.plotTitle = plotTitle
            it.xAxisLabel = xAxisLabel
            it.yAxisLabel = yAxisLabel
            it.closeOperation = closeOperation
            it.stroke = stroke
            it.showLegend = showLegend
        }
    }
}
