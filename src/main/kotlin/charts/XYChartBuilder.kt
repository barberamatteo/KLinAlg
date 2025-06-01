package it.matteobarbera.charts

import charts.ChartBuilder
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.axis.LogAxis
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.AbstractRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.BasicStroke
import java.awt.Panel
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.SwingUtilities


class XYChartBuilder(
    private val values: MutableList<Double>,
    private val options: XYChartOptions = XYChartOptions()
): ChartBuilder {
    private lateinit var series: XYSeries
    private lateinit var chartPanel: ChartPanel
    private lateinit var frame: JFrame
    private val dataset = XYSeriesCollection()
    private var panelX: Int = 0
    private var panelY: Int = 0

    private var panelWidth: Int = Toolkit.getDefaultToolkit().screenSize.width / 2
    private var panelHeight: Int = Toolkit.getDefaultToolkit().screenSize.height / 2
    private var mustSetLogAxis = false




    override fun seriesName(seriesName: String): XYChartBuilder {
        options.seriesName = seriesName
        return this
    }

    override fun windowTitle(windowTitle: String): XYChartBuilder {
        options.windowTitle = windowTitle
        return this
    }
    override fun plotTitle(plotTitle: String): XYChartBuilder {
        options.plotTitle = plotTitle
        return this
    }
    override fun xAxisLabel(xAxisLabel: String): XYChartBuilder {
        options.xAxisLabel = xAxisLabel
        return this
    }
    override fun yAxisLabel(yAxisLabel: String): XYChartBuilder {
        options.yAxisLabel = yAxisLabel
        return this
    }

    override fun closeOperations(closeOperations: Int): XYChartBuilder {
        options.closeOperation = closeOperations
        return this
    }
    override fun showLegend(showLegend: Boolean): XYChartBuilder {
        options.showLegend = showLegend
        return this
    }

    override fun location(x: Int, y: Int): XYChartBuilder {
        this.panelX = x
        this.panelY = y
        return this
    }

    override fun stroke(thickness: Float): XYChartBuilder {
        options.stroke = thickness
        return this
    }

    override fun panelSize(width: Int, height: Int): XYChartBuilder {
        this.panelWidth = width
        this.panelHeight = height
        return this
    }



    override fun getOptions(): XYChartOptions {
        return options.copy()
    }

    override fun build(): XYChartBuilder{
        this.series = XYSeries(options.seriesName)
        this.values.forEachIndexed(series::add)
        dataset.addSeries(series)


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
        if (mustSetLogAxis) {
            val logAxis = LogAxis(options.yAxisLabel)
            logAxis.base = 10.0
            logAxis.isMinorTickMarksVisible = true

            logAxis.isAutoRange = true
            chartPanel.chart.xyPlot.rangeAxis = logAxis
        }
        return this
    }

    override fun setLogAxis(logAxis: Boolean): ChartBuilder {
        mustSetLogAxis = logAxis
        return this
    }

    fun getPanel(): ChartPanel {
        return chartPanel
    }

    override fun show(){

        SwingUtilities.invokeLater {
            frame = JFrame(options.windowTitle)
            frame.setLocation(panelX, panelY)
            frame.add(chartPanel)
            frame.defaultCloseOperation = options.closeOperation
            frame.setSize(panelWidth, panelHeight)
            frame.isVisible = true
        }
    }
}