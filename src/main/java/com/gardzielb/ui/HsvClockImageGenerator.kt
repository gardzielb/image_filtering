package com.gardzielb.ui

import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class HsvClockImageGenerator(private val frameSize: Double, private val radius: Double, private val count: Int)
	: ImageGenerator {

	override fun createImage(width: Double, height: Double): Image {

		val canvas = Canvas(width, height)
		val gc = canvas.graphicsContext2D

		drawFrame(gc, width, height, frameSize)

		val center = Point2D(width / 2, height / 2)
		val bigRadius = min((width - 2 * frameSize) / 2, (height - 2 * frameSize) / 2) - frameSize - radius
		drawCircles(gc, radius, count, bigRadius, center)

		return canvas.snapshot(null, null)
	}

	private fun drawCircles(gc: GraphicsContext, radius: Double, count: Int, bigRadius: Double, centerOffset: Point2D) {

		val angle = (2 * PI) / count

		for (i in 0 until count) {

			val alpha = i * angle
			val dx = bigRadius * sin(alpha)
			val dy = bigRadius * cos(alpha)
			val center = Point2D(dx, dy).add(centerOffset)

			val alphaDeg = (alpha / PI) * 180
			val h = if (alphaDeg <= 180)
				alphaDeg + 180
			else
				alphaDeg - 180

			gc.fill = Color.web("hsl(${h},100%,100%)")
			gc.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
		}
	}

	private fun drawFrame(gc: GraphicsContext, width: Double, height: Double, frameSize: Double) {

		gc.fill = Color.BLACK
		gc.fillRect(0.0, 0.0, width, height)

		gc.fill = Color.WHITE
		gc.fillRect(frameSize, frameSize, width - 2 * frameSize, height - 2 * frameSize)
	}
}