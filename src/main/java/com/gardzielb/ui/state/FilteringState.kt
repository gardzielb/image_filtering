package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.ImageFilter
import com.gardzielb.imgproc.filters.NullPixelCollection
import com.gardzielb.imgproc.filters.PixelCollection
import com.gardzielb.ui.ImageController
import javafx.scene.input.MouseEvent

abstract class FilteringState(protected val imageController: ImageController) {

	private var filter: ImageFilter? = null

	protected val isFilterActive get() = filter != null

	fun applyFilter(filter: ImageFilter) {

		this.filter = filter
		applyFilter()
	}

	protected fun applyFilter() {

		filter!!.let {

			val img = it.apply(pixelRange)
			it.img = img
			imageController.updateImage(it.img)
		}
	}

	open fun onMouseClicked(event: MouseEvent) {}

	open fun onMouseMoved(event: MouseEvent) {}

	open fun onMouseDragged(event: MouseEvent) {}

	open fun updateSelection() {}

	abstract val pixelRange: PixelCollection
}


class NullFilteringState(imageController: ImageController) : FilteringState(imageController) {

	override val pixelRange get() = NullPixelCollection()
}