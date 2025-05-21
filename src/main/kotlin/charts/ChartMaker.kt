package it.matteobarbera.charts

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import javax.swing.JFrame
import javax.swing.SwingUtilities


object ChartMaker {
    fun simpleDiscretePlot(
        values: MutableList<Double>,
        seriesName: String,
        windowTitle: String,
        plotTitle: String,
        xAxisLabel: String,
        yAxisLabel: String
    ){
        val series = XYSeries(seriesName)
        values.forEachIndexed(series::add)
        val dataset = XYSeriesCollection()
        dataset.addSeries(series)
        val chart = ChartFactory.createXYLineChart(
            plotTitle,
            xAxisLabel,
            yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        )

        val chartPanel = ChartPanel(chart)
        SwingUtilities.invokeLater {
            val frame = JFrame(windowTitle)
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.add(chartPanel)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}