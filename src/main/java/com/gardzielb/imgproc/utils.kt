package com.gardzielb.imgproc

import javafx.scene.chart.AreaChart
import javafx.scene.image.Image
import java.io.File


// convert color value from range [0,1] to [0,255]
fun color255(color: Double) = (color * 255).toInt()


// narrow color value to range [0,1]
fun limitColor01(color: Double) = when {
	color < 0 -> 0.0
	color > 1 -> 1.0
	else -> color
}


// set fill color of area chart
fun styleChart(chart: AreaChart<Int, Int>, color: String) {

	for (i in 0..7) {
		for (node in chart.lookupAll(".default-color${i}.chart-series-area-fill")) {
			node.style = "-fx-fill: ${color};"
		}
	}
}


// load from file and scale image so that one of its dimensions is equal to the matching requested value
// and the other one is greater or equal than the requested value
fun loadScaledImage(imgFile: File, width: Double, height: Double): Image {

	val img = Image(imgFile.inputStream())
	val heightAttitude = img.height / height
	val widthAttitude = img.width / width

	return if (heightAttitude < widthAttitude) {
		Image(imgFile.inputStream(), img.width / heightAttitude, height, true, false)
	}
	else {
		Image(imgFile.inputStream(), width, img.height / widthAttitude, true, false)
	}
}

// utility class for integer matrix with square brackets accessor
class IntMatrix(private val array: Array<Array<Int>>) {

	val rowNum = array.size
	val colNum = array[0].size

	operator fun get(i: Int, j: Int) = array[i][j]
}