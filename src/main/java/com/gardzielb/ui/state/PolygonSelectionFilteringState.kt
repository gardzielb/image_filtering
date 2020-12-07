package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.PolygonPixelCollection
import com.gardzielb.ui.ImageController
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon


// In this state filter is applied to polygon selection
class PolygonSelectionFilteringState(private val selection: Polygon, imageController: ImageController)
	: FilteringState(imageController) {

	override val pixelRange = PolygonPixelCollection(selection)

	init {
		selection.stroke = Color.BLUE
		selection.strokeDashArray.addAll(5.0, 5.0)
		selection.fill = Color.TRANSPARENT
	}

	override fun updateSelection() = imageController.updateSelection(selection)
}