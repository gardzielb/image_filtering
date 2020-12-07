package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.FilterData
import com.gardzielb.imgproc.filters.FilterRange
import com.gardzielb.ui.*
import javafx.geometry.Point2D

abstract class FilterStateBuilder {

	private lateinit var next: FilterStateBuilder

	fun setNext(next: FilterStateBuilder): FilterStateBuilder {

		this.next = next
		return this.next
	}

	fun buildState(filterData: FilterData, imageController: ImageController): FilteringState {

		val state = tryBuildState(filterData, imageController)
		return state ?: next.buildState(filterData, imageController)
	}

	abstract fun tryBuildState(filterData: FilterData, imageController: ImageController): FilteringState?
}


class EntireImageFilterStateBuilder : FilterStateBuilder() {

	override fun tryBuildState(filterData: FilterData, imageController: ImageController): FilteringState? {

		if (filterData.filterRange != FilterRange.IMAGE) return null
		return EntireImageFilteringState(filterData.img!!, imageController)
	}
}


class CircleBrushFilterStateBuilder : FilterStateBuilder() {

	override fun tryBuildState(filterData: FilterData, imageController: ImageController): FilteringState? {

		if (filterData.filterRange != FilterRange.CIRCLE) return null
		val radius = filterData.brushRadiusProperty.get()
		val img = filterData.img!!
		return CircleBrushFilteringState(
				img, Point2D(img.width / 2, img.height / 2), radius.toDouble(), imageController
		)
	}
}


class PolygonSelectionFilterStateBuilder(private val filterMachine: FilterStateMachine) : FilterStateBuilder() {

	override fun tryBuildState(filterData: FilterData, imageController: ImageController): FilteringState? {

		if (filterData.filterRange != FilterRange.POLYGON) return null
		return PolygonConstructionFilteringState(filterData.img!!, filterMachine, imageController)
	}
}