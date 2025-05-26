package it.matteobarbera.charts

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.AbstractRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.BasicStroke
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


class ChartBuilder(
    private val values: MutableList<Double>,
    private val options: ChartOptions = ChartOptions()
) {
    private lateinit var series: XYSeries
    private lateinit var chartPanel: ChartPanel
    private lateinit var frame: JFrame
    private val dataset = XYSeriesCollection()
    private var panelX: Int = 0
    private var panelY: Int = 0

    private var panelWidth: Int = Toolkit.getDefaultToolkit().screenSize.width / 2
    private var panelHeight: Int = Toolkit.getDefaultToolkit().screenSize.height / 2





    fun seriesName(seriesName: String): ChartBuilder {
        options.seriesName = seriesName
        return this
    }

    fun windowTitle(windowTitle: String): ChartBuilder {
        options.windowTitle = windowTitle
        return this
    }
    fun plotTitle(plotTitle: String): ChartBuilder {
        options.plotTitle = plotTitle
        return this
    }
    fun xAxisLabel(xAxisLabel: String): ChartBuilder {
        options.xAxisLabel = xAxisLabel
        return this
    }
    fun yAxisLabel(yAxisLabel: String): ChartBuilder {
        options.yAxisLabel = yAxisLabel
        return this
    }

    fun closeOperations(closeOperations: Int): ChartBuilder {
        options.closeOperation = closeOperations
        return this
    }
    fun showLegend(showLegend: Boolean): ChartBuilder {
        options.showLegend = showLegend
        return this
    }

    fun location(x: Int, y: Int): ChartBuilder {
        this.panelX = x
        this.panelY = y
        return this
    }

    fun stroke(thickness: Float): ChartBuilder {
        options.stroke = thickness
        return this
    }

    fun panelSize(width: Int, height: Int): ChartBuilder {
        this.panelWidth = width
        this.panelHeight = height
        return this
    }



    fun getOptions(): ChartOptions {
        return options.copy()
    }

    fun build(): ChartBuilder{
        this.series = XYSeries(options.seriesName)
        this.values.forEachIndexed(series::add)
        dataset.addSeries(series)
        this.frame = JFrame(options.windowTitle)
        this.frame.setLocation(panelX, panelY)

        val chart = ChartFactory.createXYLineChart(
            options.plotTitle,
            options.xAxisLabel,
            options.yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            options.showLegend,
            true,
            false
        )

        chart.xyPlot.renderer.defaultStroke = BasicStroke(options.stroke)
        (chart.xyPlot.renderer as AbstractRenderer).autoPopulateSeriesStroke = false


        chartPanel = ChartPanel(chart)
        return this
    }

    fun show(){
        SwingUtilities.invokeLater {
            frame.add(chartPanel)
            frame.defaultCloseOperation = options.closeOperation
            frame.setSize(panelWidth, panelHeight)
            frame.isVisible = true
        }
    }
}