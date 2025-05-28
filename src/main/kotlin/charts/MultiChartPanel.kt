package charts

import org.jfree.chart.ChartPanel
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class MultiChartPanel {

    val screenDimensions: Dimension = Toolkit.getDefaultToolkit().screenSize

    fun show3(
        chartsList: List<ChartPanel>,
        windowTitle: String,
        panelX: Int,
        panelY: Int
    ){
        assert(chartsList.size == 3)
        val frame = JFrame(windowTitle)
        SwingUtilities.invokeLater {
            frame.setLocation(panelX, panelY)
            chartsList.forEach { chartPanel -> chartPanel.layout = null }
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            for (panel in chartsList){
                frame.add(panel)
            }
            frame.addComponentListener(object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent?) {
                    val w = frame.contentPane.getWidth()
                    val h = frame.contentPane.getHeight()
                    val halfW = w / 2
                    val quarterW = w / 4
                    val halfH = h / 2

                    chartsList[0].setBounds(0, 0, halfW, halfH) // Top-left
                    chartsList[1].setBounds(halfW, 0, halfW, halfH) // Top-right
                    chartsList[2].setBounds(quarterW, halfH, halfW, halfH) // Bottom-left
                }
            })
            frame.setSize(screenDimensions.width / 2, screenDimensions.height / 2)
            frame.isVisible = true

        }
    }
    fun show4(
        chartsArray: MutableList<ChartPanel>,
        windowTitle: String,
        panelX: Int,
        panelY: Int,
        ){
        assert(chartsArray.size == 4)
        val frame = JFrame(windowTitle)
        SwingUtilities.invokeLater {
            frame.setLocation(panelX, panelY)
            chartsArray.map { it.layout = null }
            frame.defaultCloseOperation = WindowConstants.HIDE_ON_CLOSE
            for (panel in chartsArray) {
                frame.add(panel)
            }

            frame.addComponentListener(object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent?) {
                    val w = frame.contentPane.getWidth()
                    val h = frame.contentPane.getHeight()
                    val halfW = w / 2
                    val halfH = h / 2

                    chartsArray[0].setBounds(0, 0, halfW, halfH) // Top-left
                    chartsArray[1].setBounds(halfW, 0, halfW, halfH) // Top-right
                    chartsArray[2].setBounds(0, halfH, halfW, halfH) // Bottom-left
                    chartsArray[3].setBounds(halfW, halfH, halfW, halfH) // Bottom-right
                }
            })

            frame.setSize(screenDimensions.width / 2, screenDimensions.height / 2)
            frame.isVisible = true
        }
    }
}