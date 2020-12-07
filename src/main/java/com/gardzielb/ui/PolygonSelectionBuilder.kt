package com.gardzielb.ui

import javafx.geometry.Point2D
import javafx.scene.shape.*

// Manages creation of a polygon
class PolygonSelectionBuilder {

	private val vertexRadius = 3.0
	private val vertices = MutableList(0) { Point2D.ZERO }

	val isFinished get() = vertices.size > 2 && vertices.last().distance(vertices[0]) <= vertexRadius

	fun clear() = vertices.clear()

	fun addVertex(x: Double, y: Double) {

		if (isFinished) return

		if (vertices.isEmpty()) {
			vertices.add(Point2D(x, y))
		}
		else {
			moveLast(x, y)
		}

		if (isFinished) return

		vertices.add(Point2D(x, y))
	}

	fun moveLast(x: Double, y: Double) {

		if (vertices.isEmpty()) return

		vertices.removeLast()
		vertices.add(Point2D(x, y))
	}

	fun buildPolygon(): Polygon {

		vertices.removeLast()
		val points = DoubleArray(2 * vertices.size) { i ->
			if (i and 1 == 0) vertices[i / 2].x else vertices[i / 2].y
		}
		return Polygon(*points)
	}

	// creates and returns path consisting of current vertices
	fun getPath(): Shape {

		val path = Path()
		if (vertices.size == 0) return path

		path.elements.add(MoveTo(vertices[0].x, vertices[0].y))

		for (i in 1 until vertices.size) {
			path.elements.add(LineTo(vertices[i].x, vertices[i].y))
		}

		return path
	}
}