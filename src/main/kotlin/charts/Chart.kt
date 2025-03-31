package it.matteobarbera.charts

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import javax.swing.JFrame
import javax.swing.SwingUtilities


object Chart {
    fun plot(values: MutableList<Double>){
        val series = XYSeries("Errors")
        values.forEachIndexed(series::add)
        val dataset = XYSeriesCollection()
        dataset.addSeries(series)
        val chart = ChartFactory.createXYLineChart(
            "Error over iterations",
            "Iteration number",
            "Error",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        )

        val chartPanel = ChartPanel(chart)
        SwingUtilities.invokeLater {
            val frame = JFrame("Grafico con JFreeChart")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.add(chartPanel)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}