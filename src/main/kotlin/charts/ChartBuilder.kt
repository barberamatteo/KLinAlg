package charts

import it.matteobarbera.charts.ChartOptions
import org.jfree.chart.ChartPanel

interface ChartBuilder {
    fun seriesName(seriesName: String): ChartBuilder
    fun windowTitle(windowTitle: String): ChartBuilder
    fun plotTitle(plotTitle: String): ChartBuilder
    fun xAxisLabel(xAxisLabel: String): ChartBuilder
    fun yAxisLabel(yAxisLabel: String): ChartBuilder
    fun closeOperations(closeOperations: Int): ChartBuilder
    fun showLegend(showLegend: Boolean): ChartBuilder
    fun location(x: Int, y: Int): ChartBuilder
    fun stroke(thickness: Float): ChartBuilder
    fun panelSize(width: Int, height: Int): ChartBuilder
    fun getOptions(): ChartOptions
    fun build(): ChartBuilder
    fun setLogAxis(logAxis: Boolean): ChartBuilder
    fun show()
}