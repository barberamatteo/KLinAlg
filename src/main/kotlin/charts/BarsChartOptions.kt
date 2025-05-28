package it.matteobarbera.charts

import javax.swing.WindowConstants.HIDE_ON_CLOSE

class BarsChartOptions: ChartOptions() {
    override var seriesName: String = "Series"
    override var windowTitle: String = "Chart"
    override var plotTitle: String = "Simple bars chart"
    override var xAxisLabel: String = "x-Axis"
    override var yAxisLabel: String = "y-Axis"
    override var closeOperation: Int = HIDE_ON_CLOSE
    override var stroke: Float = 3.0f
    override var showLegend: Boolean = true


    override fun copy(): BarsChartOptions {
        return BarsChartOptions().also {
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