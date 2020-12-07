package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.CircleBrushPixelCollection
import com.gardzielb.ui.ImageController
import javafx.geometry.Point2D
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle


// In this state, filter is applied to pixels selected with a circle brush
class CircleBrushFilteringState(img: Image, private var center: Point2D, radius: Double,
								imageController: ImageController) : FilteringState(imageController) {

	private val imgHeight = img.height.toInt()
	private val imgWidth = img.width.toInt()
	private val selection = Circle(center.x, center.y, radius)
	private val radius = radius.toInt()
	private var prevCenter: Point2D? = null
	private var pixels = CircleBrushPixelCollection(imgWidth, imgHeight, center, this.radius)

	override val pixelRange get() = pixels

	init {
		selection.fill = Color.TRANSPARENT
		selection.strokeDashArray.addAll(5.0, 5.0)
		selection.stroke = Color.BLUE
	}

	// brush can be dragged around the image
	override fun onMouseDragged(event: MouseEvent) {

		prevCenter = center
		center = Point2D(event.x, event.y)

		// if filter has already been applied, dragging the brush extends filtered area and updates image
		if (isFilterActive) {
			pixels = CircleBrushPixelCollection(
					imgWidth, imgHeight, center, this.radius, exCenter = prevCenter, exRadius = radius
			)
			applyFilter()
		}
		else {
			pixels = CircleBrushPixelCollection(imgWidth, imgHeight, center, this.radius)
		}

		selection.centerX = event.x
		selection.centerY = event.y
		updateSelection()
	}

	override fun updateSelection() = imageController.updateSelection(selection)
}