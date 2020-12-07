package com.gardzielb.ui

import com.gardzielb.imgproc.histogram.HistogramPresenter
import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Shape


// Displays image and selection
class ImageController(private val imgGroup: Group, private val imgView: ImageView,
					  private val histogramPresenter: HistogramPresenter) {

	val img: Image? get() = imgView.image

	fun updateImage(img: Image, selection: Shape? = null) {

		imgView.image = img
		selection?.let { updateSelection(it) }
		histogramPresenter.updateHistogram(img)
	}

	fun updateSelection(shape: Shape) {

		if (imgGroup.children.size > 1)
			imgGroup.children.removeLast()

		imgGroup.children.add(shape)
	}
}