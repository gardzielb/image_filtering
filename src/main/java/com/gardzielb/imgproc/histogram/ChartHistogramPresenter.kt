package com.gardzielb.imgproc.histogram

import com.gardzielb.imgproc.color255
import com.gardzielb.imgproc.styleChart
import javafx.scene.chart.AreaChart
import javafx.scene.chart.XYChart
import javafx.scene.image.Image
import javafx.scene.shape.Rectangle

class ChartHistogramPresenter(private val chartRed: AreaChart<Int, Int>, private val chartGreen: AreaChart<Int, Int>,
							  private val chartBlue: AreaChart<Int, Int>) : HistogramPresenter {

	override fun updateHistogram(img: Image) {

		val dataRed = Array(256) { 0 }
		val dataGreen = Array(256) { 0 }
		val dataBlue = Array(256) { 0 }
		val pixelReader = img.pixelReader

		for (x in 0 until img.width.toInt()) {
			for (y in 0 until img.height.toInt()) {

				val color = pixelReader.getColor(x, y)
				dataRed[color255(color.red)]++
				dataGreen[color255(color.green)]++
				dataBlue[color255(color.blue)]++
			}
		}

		updateChart(chartRed, dataRed, "red")
		updateChart(chartGreen, dataGreen, "green")
		updateChart(chartBlue, dataBlue, "blue")
	}

	private fun updateChart(chart: AreaChart<Int, Int>, data: Array<Int>, color: String) {

		val series = XYChart.Series<Int, Int>()
		series.name = color

		for (i in data.indices) {
			val record = XYChart.Data(i, data[i])
			val pointRect = Rectangle()
			pointRect.isVisible = false
			record.node = pointRect
			series.data.add(record)
		}

		chart.data.clear()
		chart.data.add(series)
		styleChart(chart, color)
		chart.isLegendVisible = false
	}
}