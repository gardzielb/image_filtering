package com.gardzielb.imgproc.filters

import javafx.scene.shape.Polygon


// Represents a collection of pixels inside a polygon selection
class PolygonPixelCollection(polygon: Polygon) : PixelCollection {

	private val pixels = MutableList(0) { Pixel(0, 0) }

	init {

		// traverse all pixels from the polygon bounding box and add to collection those inside the polygon
		val bounds = polygon.boundsInParent
		for (x in bounds.minX.toInt()..bounds.maxX.toInt()) {
			for (y in bounds.minY.toInt()..bounds.maxY.toInt()) {

				if (polygon.contains(x.toDouble(), y.toDouble())) {
					pixels.add(Pixel(x, y))
				}
			}
		}
	}

	override val size get() = pixels.size

	override fun contains(element: Pixel) = pixels.contains(element)

	override fun containsAll(elements: Collection<Pixel>) = pixels.containsAll(elements)

	override fun isEmpty() = pixels.isEmpty()

	override fun iterator(): Iterator<Pixel> = pixels.iterator()
}