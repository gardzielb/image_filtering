package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.EntireImagePixelCollection
import com.gardzielb.ui.ImageController
import javafx.scene.image.Image
import javafx.scene.shape.Polygon


class EntireImageFilteringState(img: Image, imageController: ImageController)
	: FilteringState(imageController) {

	override val pixelRange = EntireImagePixelCollection(img.width.toInt(), img.height.toInt())

	override fun updateSelection() = imageController.updateSelection(Polygon())
}