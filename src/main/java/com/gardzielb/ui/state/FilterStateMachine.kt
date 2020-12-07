package com.gardzielb.ui.state

import com.gardzielb.imgproc.filters.*
import javafx.scene.input.MouseEvent


// Manages filtering image;
// every state corresponds to another filtering area
class FilterStateMachine(state: FilteringState) {

	private val buildFilterChain = IdentityMatrixFilterBuilder()

	init {
		buildFilterChain
				.setNext(BlurMatrixFilterBuilder())
				.setNext(SharpenMatrixFilterBuilder())
				.setNext(ReliefMatrixFilterBuilder())
				.setNext(EdgeDetectMatrixFilterBuilder())
	}

	var state = state
		set(value) {
			field = value
			field.updateSelection()
		}

	fun applyFilter(filterData: FilterData) {

		val filter = buildFilterChain.buildFilter(filterData)
		state.applyFilter(filter)
	}

	fun applyFilter(filter: ImageFilter) = state.applyFilter(filter)

	fun onMouseClicked(event: MouseEvent) = state.onMouseClicked(event)

	fun onMouseDragged(event: MouseEvent) = state.onMouseDragged(event)

	fun onMouseMoved(event: MouseEvent) = state.onMouseMoved(event)
}