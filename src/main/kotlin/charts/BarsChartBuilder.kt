package it.matteobarbera.charts

import charts.ChartBuilder
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.axis.LogAxis
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.xy.XYSeries
import java.awt.Toolkit
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.text.DecimalFormat
import java.text.FieldPosition
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.log10


class BarsChartBuilder(
    private val options: BarsChartOptions = BarsChartOptions()
): ChartBuilder {

    private lateinit var series: XYSeries
    private lateinit var chartPanel: ChartPanel
    private lateinit var frame: JFrame
    private val dataset = DefaultCategoryDataset()
    private var panelX: Int = 0
    private var panelY: Int = 0
    private var panelWidth: Int = Toolkit.getDefaultToolkit().screenSize.width / 2
    private var panelHeight: Int = Toolkit.getDefaultToolkit().screenSize.height / 2

    private var categories = mutableSetOf<String>()
    private var dataMap = mutableMapOf<String, MutableMap<String, Double>>()
    private var mustSetLogAxis = false
    override fun seriesName(seriesName: String): BarsChartBuilder {
        options.seriesName = seriesName
        return this
    }

    override fun windowTitle(windowTitle: String): BarsChartBuilder {
        options.windowTitle = windowTitle
        return this
    }
    override fun plotTitle(plotTitle: String): BarsChartBuilder {
        options.plotTitle = plotTitle
        return this
    }
    override fun xAxisLabel(xAxisLabel: String): BarsChartBuilder {
        options.xAxisLabel = xAxisLabel
        return this
    }
    override fun yAxisLabel(yAxisLabel: String): BarsChartBuilder {
        options.yAxisLabel = yAxisLabel
        return this
    }

    override fun closeOperations(closeOperations: Int): BarsChartBuilder {
        options.closeOperation = closeOperations
        return this
    }
    override fun showLegend(showLegend: Boolean): BarsChartBuilder {
        options.showLegend = showLegend
        return this
    }

    override fun location(x: Int, y: Int): BarsChartBuilder {
        this.panelX = x
        this.panelY = y
        return this
    }

    override fun stroke(thickness: Float): BarsChartBuilder {
        options.stroke = thickness
        return this
    }

    override fun panelSize(width: Int, height: Int): BarsChartBuilder {
        this.panelWidth = width
        this.panelHeight = height
        return this
    }



    override fun getOptions(): BarsChartOptions {
        return options.copy()
    }


    fun addCategory(category: String): BarsChartBuilder {
        this.categories.add(category)
        return this
    }
    fun addEntity(entity: String): BarsChartBuilder {
        this.dataMap.putIfAbsent(entity, mutableMapOf())
        return this
    }
    fun addData(entity: String, category: String, value: Double): BarsChartBuilder {
        this.dataMap[entity]!![category] = value
        return this
    }

    override fun build(): BarsChartBuilder {

        for ((entity, categoryValueMap) in dataMap) {
            for ((category, value) in categoryValueMap) {
                dataset.addValue(value, entity, category)
            }
        }
        val barChart = ChartFactory.createBarChart(
            options.plotTitle,
            options.xAxisLabel,
            options.yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            options.showLegend,
            true,
            false
        )

        chartPanel = ChartPanel(barChart)
        if (mustSetLogAxis) {
            val logAxis = LogAxis(options.yAxisLabel)
            logAxis.base = 10.0
            logAxis.isMinorTickMarksVisible = true
            logAxis.isAutoRange = true
            logAxis.numberFormatOverride = object : DecimalFormat("0.##") {
                private val sciFormat = DecimalFormat("0E0") // Exponential form like 1E-1

                override fun format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                    return when {
                        number >= 1.0 -> {
                            val logValue = log10(number)
                            if (logValue % 1.0 == 0.0) {
                                toAppendTo.append(number.toInt()) // Show integer power of 10
                            } else {
                                super.format(number.toInt(), toAppendTo, pos) // Round and format normally
                            }
                        }
                        number == 0.0 -> super.format(number.toInt(), toAppendTo, pos)
                        number > 0.0 -> {
                            sciFormat.format(number, toAppendTo, pos) // Exponential format for numbers < 1
                        }
                        else -> {
                            super.format(number, toAppendTo, pos) // Just in case (though log scale skips zero)
                        }
                    }
                }
            }
            chartPanel.chart.categoryPlot.rangeAxis = logAxis
            return this
        }
        return this
    }

    override fun setLogAxis(logAxis: Boolean): BarsChartBuilder {
        this.mustSetLogAxis = logAxis
        return this
    }

    fun getPanel(): ChartPanel {
        return chartPanel
    }
    override fun show() {

        SwingUtilities.invokeLater {
            frame = JFrame(options.windowTitle)
            frame.setLocation(panelX, panelY)
            chartPanel.layout = null

            frame.add(chartPanel)
            frame.defaultCloseOperation = options.closeOperation
            frame.setSize(panelWidth, panelHeight)

            frame.isVisible = true
        }
    }
}