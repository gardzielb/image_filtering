package com.gardzielb.imgproc.filters

import com.gardzielb.imgproc.IntMatrix
import com.gardzielb.imgproc.limitColor01
import javafx.scene.image.Image
import javafx.scene.image.PixelReader
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class MatrixImageFilter(img: Image, private val matrix: IntMatrix, offset: Int = 0, private val factor: Int? = null)
	: ImageFilter(img) {

	private val offset = offset / 255
	private val midRow = matrix.rowNum / 2
	private val midCol = matrix.colNum / 2

	override fun apply(pixels: PixelCollection): Image {

		val pixelReader = img.pixelReader
		val filteredImg = WritableImage(pixelReader, img.width.toInt(), img.height.toInt())
		val pixelWriter = filteredImg.pixelWriter

		pixels.parallelStream().forEach {
			val color = filterPixel(it, pixelReader)
			pixelWriter.setColor(it.x, it.y, color)
		}

		return filteredImg
	}

	private fun filterPixel(pixel: Pixel, pixelReader: PixelReader): Color {

		var rSum = 0.0
		var gSum = 0.0
		var bSum = 0.0
		var weightSum = 0

		for (i in 0 until matrix.rowNum) {

			val y = pixel.y + i - midRow
			if (y < 0 || y >= img.height)
				continue

			for (j in 0 until matrix.colNum) {

				val x = pixel.x + j - midCol
				if (x < 0 || x >= img.width)
					continue

				// use matrix filtering pattern
				val color = pixelReader.getColor(pixel.x + j - midCol, pixel.y + i - midRow)
				rSum += matrix[i, j] * color.red
				gSum += matrix[i, j] * color.green
				bSum += matrix[i, j] * color.blue
				weightSum += matrix[i, j]
			}
		}

		// if given a specific factor, use it; else use weightSum or 1 if sum is 0
		val factor = this.factor ?: (if (weightSum > 0) weightSum else 1)
		val r = limitColor01(offset + rSum / factor)
		val g = limitColor01(offset + gSum / factor)
		val b = limitColor01(offset + bSum / factor)

		return Color(r, g, b, 1.0)
	}
}