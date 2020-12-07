package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.EntireImagePixelCollection
import com.gardzielb.imgproc.filters.PixelCollection
import com.gardzielb.ui.ImageController
import com.gardzielb.ui.PolygonSelectionBuilder
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color


// In this state, user can create polygon selection on the image;
// Filter applies to entire image
class PolygonConstructionFilteringState(img: Image, private val stateMachine: FilterStateMachine,
										imageController: ImageController)
	: FilteringState(imageController) {

	private val selectionBuilder = PolygonSelectionBuilder()

	override val pixelRange: PixelCollection = EntireImagePixelCollection(img.width.toInt(), img.height.toInt())

	// mouse click adds new vertex to polygon
	override fun onMouseClicked(event: MouseEvent) {

		if (event.button == MouseButton.PRIMARY) {

			selectionBuilder.addVertex(event.x, event.y)
			if (selectionBuilder.isFinished) {
				stateMachine.state = PolygonSelectionFilteringState(selectionBuilder.buildPolygon(), imageController)
			}
		}
		else selectionBuilder.clear()
	}

	// mouse move makes new vertex move
	override fun onMouseMoved(event: MouseEvent) {

		selectionBuilder.moveLast(event.x, event.y)
		updateSelection()
	}

	override fun updateSelection() {
		val path = selectionBuilder.getPath()
		path.fill = Color.TRANSPARENT
		path.stroke = Color.BLUE
		path.strokeDashArray.addAll(5.0, 5.0)
		imageController.updateSelection(path)
	}
}