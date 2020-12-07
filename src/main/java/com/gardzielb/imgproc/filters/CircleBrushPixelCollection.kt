package com.gardzielb.imgproc.filters

import javafx.geometry.Point2D
import kotlin.math.*


// Represents a collection of pixels selected with a circle brush
class CircleBrushPixelCollection(private val imgWidth: Int, private val imgHeight: Int, center: Point2D, radius: Int,
								 exCenter: Point2D? = null, exRadius: Int = 0) : PixelCollection {

	private val pixels = MutableList(0) { Pixel(0, 0) }

	init {
		addPixels(center, radius, exCenter, exRadius)
	}

	override fun iterator(): Iterator<Pixel> = pixels.iterator()

	// adds pixels to the collection following the rules:
	// 1) every pixel from the bounding box of a circle given by 'center' and 'radius' is considered
	// 2) only pixels inside the circle are added
	// 3) pixels inside a circle given by 'exCenter' and 'exRadius' are not added (if exCenter is not null)
	private fun addPixels(center: Point2D, radius: Int, exCenter: Point2D? = null, exRadius: Int = 0) {

		val xLeft = max(0.0, center.x - radius)
		val yTop = max(0.0, center.y - radius)
		val xRight = min(imgWidth - 1.0, center.x + radius)
		val yBottom = min(imgHeight - 1.0, center.y + radius)

		var itPoint = Point2D(xLeft, yTop)
		val xMoveVec = Point2D(1.0, 0.0)
		val yMoveVec = Point2D(xLeft - xRight, 1.0)

		while (itPoint.y <= yBottom) {
			while (itPoint.x <= xRight) {

				val isInCircle = itPoint.distance(center) <= radius
				val isOutExCircle = if (exCenter != null) itPoint.distance(exCenter) > exRadius else true

				if (isInCircle && isOutExCircle)
					pixels.add(Pixel(itPoint.x.toInt(), itPoint.y.toInt()))

				itPoint = itPoint.add(xMoveVec)
			}

			itPoint = itPoint.add(yMoveVec)
		}
	}
}